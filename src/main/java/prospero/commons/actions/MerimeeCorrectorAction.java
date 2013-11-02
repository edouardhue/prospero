package prospero.commons.actions;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RecursiveAction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prospero.commons.ProsperoBot;
import prospero.commons.mediawiki.Page;
import prospero.commons.mediawiki.Revision;
import prospero.commons.merimee.Protection;
import prospero.commons.merimee.Protection.Type;

public final class MerimeeCorrectorAction extends RecursiveAction {
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MerimeeCorrectorAction.class);
    
    static final Pattern MÉRIMÉE_PATTERN = Pattern.compile("\\{{2}Mérimée(.+?)\\}{2}", Pattern.DOTALL);
    
    static final Pattern ID_PATTERN = Pattern.compile("[PIE]A\\d{8}");
    
    private final ProsperoBot bot;
    
    private final Page page;
    
    public MerimeeCorrectorAction(final ProsperoBot bot, final Page page) {
        this.bot = bot;
        this.page = page;
    }
    
    @Override
    protected void compute() {
        LOGGER.debug("Looking for Mérimée in page {}", page.getTitle());
        if (!page.getRevisions().isEmpty()) {
            final Revision rev = page.getRevisions().get(0);
            final Matcher mériméeMatcher = MÉRIMÉE_PATTERN.matcher(rev.getContent());
            final StringBuffer nextRevision = new StringBuffer(rev.getContent().length());
            while (mériméeMatcher.find()) {
                LOGGER.debug("Found template {}", mériméeMatcher.group(0));
                final String templateContent = mériméeMatcher.group(1);
                mériméeMatcher.appendReplacement(nextRevision, updateTemplate(templateContent));
            }
            mériméeMatcher.appendTail(nextRevision);
            final PageUpdate update = new PageUpdate(page, nextRevision.toString());
            if (bot.getPageUpdates().offer(update)) {
                LOGGER.debug("Submitted update for page {}", page.getTitle());
            } else {
                LOGGER.error("Could not submit update of page {}", page.getTitle());
            }
        }
    }
    
    private String updateTemplate(final String templateContent) {
        final Set<String> ids = new HashSet<>();
        final Set<Protection.Type> types = EnumSet.noneOf(Protection.Type.class);
        final Matcher idsMatcher = ID_PATTERN.matcher(templateContent);
        while (idsMatcher.find()) {
            final String id = idsMatcher.group();
            LOGGER.debug("Found ID {}", id);
            ids.add(id);
            if (bot.getMonuments().containsKey(id)) {
                final Set<Type> monumentTypes = bot.getMonuments().get(id).getProtectionTypes();
                LOGGER.debug("Types are {}", monumentTypes);
                types.addAll(monumentTypes);
            }
        }
        final StringBuilder buffer = new StringBuilder("{{Mérimée");
        for (final String id : ids) {
            buffer.append('|');
            buffer.append(id);
        }
        if (!types.isEmpty()) {
            buffer.append("|type=");
            if (types.contains(Type.CLASSE) && types.contains(Type.INSCRIT)) {
                buffer.append("lesdeux");
            } else if (types.contains(Type.CLASSE)) {
                buffer.append("classé");
            } else if (types.contains(Type.INSCRIT)) {
                buffer.append("inscrit");
            }
        }
        buffer.append("}}");
        
        return buffer.toString();
    }
}
