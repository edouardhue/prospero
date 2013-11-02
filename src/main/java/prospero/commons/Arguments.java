package prospero.commons;

import java.io.File;

import org.kohsuke.args4j.Option;

public final class Arguments {
    @Option(name = "-m", aliases = "--merimee", required = true)
    private File merimeeFile;

    @Option(name = "-u", aliases = "--url", required = true)
    private String apiUri;

    @Option(name = "-c", aliases = "--category", required = true)
    private String categoryTitle;

    @Option(name = "-l", aliases = "--login", required = true)
    private String login;

    @Option(name = "-p", aliases = "--password", required = true)
    private String password;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
