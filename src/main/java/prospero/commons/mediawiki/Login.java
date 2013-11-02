package prospero.commons.mediawiki;

public final class Login {
    private String result;

    private String token;

    private String lguserid;

    private String lgusername;

    private String lgtoken;

    private String cookieprefix;

    private String sessionid;

    public String getResult() {
        return result;
    }

    public void setResult(final String result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getLguserid() {
        return lguserid;
    }

    public void setLguserid(final String lguserid) {
        this.lguserid = lguserid;
    }

    public String getLgusername() {
        return lgusername;
    }

    public void setLgusername(final String lgusername) {
        this.lgusername = lgusername;
    }

    public String getLgtoken() {
        return lgtoken;
    }

    public void setLgtoken(final String lgtoken) {
        this.lgtoken = lgtoken;
    }

    public String getCookieprefix() {
        return cookieprefix;
    }

    public void setCookieprefix(final String cookieprefix) {
        this.cookieprefix = cookieprefix;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(final String sessionid) {
        this.sessionid = sessionid;
    }

}
