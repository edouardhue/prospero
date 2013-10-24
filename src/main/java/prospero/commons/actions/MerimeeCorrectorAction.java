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
            while (mériméeMatcher.find()) {
                LOGGER.debug("Found template {}", mériméeMatcher.group(0));
                final String templateContent = mériméeMatcher.group(1);
                new TemplateAction(templateContent).fork();
            }
        }
    }

    private final class TemplateAction extends RecursiveAction {
        private static final long serialVersionUID = 1L;

        private final String templateContent;
        
        private TemplateAction(final String templateContent) {
            this.templateContent = templateContent;
        }
        
        @Override
        protected void compute() {
            final Set<String> ids = new HashSet<>();
            final Set<Protection.Type> types = EnumSet.noneOf(Protection.Type.class);
            final Matcher idsMatcher = ID_PATTERN.matcher(templateContent);
            while (idsMatcher.find()) {
                final String id = idsMatcher.group();
                ids.add(id);
                if (bot.getMonuments().containsKey(id)) {
                    types.addAll(bot.getMonuments().get(id).getProtectionTypes());
                }
                LOGGER.debug("Found ID {}\n\tMérimée type is {}", id, bot.getMonuments().get(id));
            }
        }
    }
}
