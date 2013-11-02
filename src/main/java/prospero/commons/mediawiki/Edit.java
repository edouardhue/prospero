package prospero.commons.mediawiki;

public final class Edit {
    private String result;

    private String pageid;

    private String title;

    private Integer oldrevid;

    private Integer newrevid;

    public String getResult() {
        return result;
    }

    public void setResult(final String result) {
        this.result = result;
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(final String pageid) {
        this.pageid = pageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Integer getOldrevid() {
        return oldrevid;
    }

    public void setOldrevid(final Integer oldrevid) {
        this.oldrevid = oldrevid;
    }

    public Integer getNewrevid() {
        return newrevid;
    }

    public void setNewrevid(final Integer newrevid) {
        this.newrevid = newrevid;
    }
}
