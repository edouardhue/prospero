package prospero.commons.actions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.concurrent.RecursiveAction;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prospero.commons.ProsperoBot;
import prospero.commons.mediawiki.Continue;
import prospero.commons.mediawiki.MWQueryResponse;
import prospero.commons.mediawiki.Page;


public final class CategoryMembersAction extends RecursiveAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryMembersAction.class);
    
    private static final long serialVersionUID = 1L;

    private final ProsperoBot bot;
    
    private final String apiUrl;

    private final String categoryTitle;
    
    private final Continue qContinue;
    
    public CategoryMembersAction(final ProsperoBot bot) {
        this(bot, new Continue());
    }
    
    private CategoryMembersAction(final ProsperoBot bot, final Continue qContinue) {
        this.bot = bot;
        this.apiUrl = bot.getArguments().getApiUri();
        this.categoryTitle = bot.getArguments().getCategoryTitle();
        this.qContinue = qContinue;
    }

    @Override
    protected void compute() {
        try {
            try (final InputStream is = query().asStream()) {
                final MWQueryResponse response = ProsperoBot.MAPPER.readValue(is, MWQueryResponse.class);
                for (final Page page : response.getQuery().getPages().values()) {
                    new MerimeeCorrectorAction(bot, page).fork();
                }
                if (response.getqContinue() == null) {
                    LOGGER.debug("No continue field in response, stopping");
                    bot.notifyExhausted();
                } else {
                    new CategoryMembersAction(bot, response.getqContinue()).fork();
                }
            }
        } catch (final IOException | URISyntaxException e) {
            LOGGER.warn("Can't fetch {} members from {}", categoryTitle, apiUrl, e);
        }
    }

    private Content query() throws IOException, URISyntaxException {
        return Request.Get(new URIBuilder(apiUrl)
                        .addParameter("action", "query")
                        .addParameter("generator", "categorymembers")
                        .addParameter("gcmtitle", categoryTitle)
                        .addParameter("prop", "revisions|info")
                        .addParameter("rvprop", "content|timestamp")
                        .addParameter("intoken", "edit")
                        .addParameter("format", "json")
                        .addParameter("continue", qContinue.getQueryContinue())
                        .addParameter("gcmcontinue", qContinue.getGcmcontinue())
                        .build())
                .addHeader("User-Agent", ProsperoBot.BUNDLE.getString("useragent"))
                .execute()
                .returnContent();
    }
}
