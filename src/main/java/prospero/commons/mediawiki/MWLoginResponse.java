package prospero.commons.mediawiki;

public final class MWLoginResponse extends MWBaseResponse {
    private Login login;

    public Login getLogin() {
        return login;
    }

    public void setLogin(final Login login) {
        this.login = login;
    }

}
