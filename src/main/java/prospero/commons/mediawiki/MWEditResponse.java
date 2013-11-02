package prospero.commons.mediawiki;

public final class MWEditResponse extends MWBaseResponse {
    private Edit edit;
    
    public Edit getEdit() {
        return edit;
    }
    
    public void setEdit(final Edit edit) {
        this.edit = edit;
    }
}
