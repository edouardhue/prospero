package prospero.commons.actions;

import prospero.commons.mediawiki.Page;

public final class PageUpdate {
    private final Page page;

    private final String nextRevision;

    public PageUpdate(final Page page, final String nextRevision) {
        super();
        this.page = page;
        this.nextRevision = nextRevision;
    }

    public Page getPage() {
        return page;
    }

    public String getNextRevision() {
        return nextRevision;
    }

}
