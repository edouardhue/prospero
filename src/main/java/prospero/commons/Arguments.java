package prospero.commons;

import java.io.File;

import org.kohsuke.args4j.Option;

final class Arguments {
    @Option(name = "-m", aliases = "--merimee", required = true)
    private File merimeeFile;

    @Option(name = "-u", aliases = "--url", required = true)
    private String apiUri;

    @Option(name = "-c", aliases = "--category", required = true)
    private String categoryTitle;

    public File getMerimeeFile() {
        return merimeeFile;
    }

    public void setMerimeeFile(final File merimeeFile) {
        this.merimeeFile = merimeeFile;
    }

    public String getApiUri() {
        return apiUri;
    }

    public void setApiUri(final String apiUri) {
        this.apiUri = apiUri;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(final String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
