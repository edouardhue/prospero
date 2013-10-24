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
import prospero.commons.mediawiki.MWResponse;
import prospero.commons.mediawiki.Page;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class CategoryMembersAction extends RecursiveAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryMembersAction.class);
    
    private static final long serialVersionUID = 1L;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    private final ProsperoBot bot;
    
    private final String apiUrl;

    private final String categoryTitle;
    
    private final Continue qContinue;
    
    public CategoryMembersAction(final ProsperoBot bot, final String apiUrl, final String categoryTitle) {
        this(bot, apiUrl, categoryTitle, new Continue());
    }
    
    private CategoryMembersAction(final ProsperoBot bot, final String apiUrl, final String categoryTitle, final Continue qContinue) {
        this.bot = bot;
        this.apiUrl = apiUrl;
        this.categoryTitle = categoryTitle;
        this.qContinue = qContinue;
    }

    @Override
    protected void compute() {
        try {
            try (final InputStream is = query().asStream()) {
                final MWResponse response = MAPPER.readValue(is, MWResponse.class);
                if (response.getqContinue() == null) {
                    LOGGER.debug("No continue field in response, stopping");
                } else {
                    new CategoryMembersAction(bot, apiUrl, categoryTitle, response.getqContinue()).fork();
                }
                for (final Page page : response.getQuery().getPages().values()) {
                    new MerimeeCorrectorAction(bot, page).fork();
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
                        .addParameter("prop", "revisions")
                        .addParameter("rvprop", "content")
                        .addParameter("format", "json")
                        .addParameter("continue", qContinue.getQueryContinue())
                        .addParameter("gcmcontinue", qContinue.getGcmcontinue())
                        .build())
                .addHeader("User-Agent", ProsperoBot.BUNDLE.getString("useragent"))
                .execute()
                .returnContent();
    }
}
