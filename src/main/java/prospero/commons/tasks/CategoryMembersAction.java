package prospero.commons.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.concurrent.RecursiveAction;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prospero.commons.mediawiki.Continue;
import prospero.commons.mediawiki.MWResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class CategoryMembersAction extends RecursiveAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryMembersAction.class);
    
    private static final long serialVersionUID = 1L;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    private final String apiUrl;

    private final String categoryTitle;
    
    private final Continue qContinue;
    
    public CategoryMembersAction(final String apiUrl, final String categoryTitle) {
        this(apiUrl, categoryTitle, new Continue());
    }
    
    private CategoryMembersAction(final String apiUrl, final String categoryTitle, final Continue qContinue) {
        this.apiUrl = apiUrl;
        this.categoryTitle = categoryTitle;
        this.qContinue = qContinue;
    }

    @Override
    protected void compute() {
        try {
            try (final InputStream is = query().asStream()) {
                final MWResponse response = MAPPER.readValue(is, MWResponse.class);
                LOGGER.debug(response.toString());
                if (response.getqContinue() == null) {
                    LOGGER.debug("No continue, stopping");
                    // Done
                } else {
                    new CategoryMembersAction(apiUrl, categoryTitle, response.getqContinue()).fork();
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
                .execute()
                .returnContent();
    }
}
