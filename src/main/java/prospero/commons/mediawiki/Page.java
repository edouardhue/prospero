package prospero.commons.mediawiki;

import java.util.List;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public final class Page {
    private String pageid;

    private Integer ns;

    private String title;

    private List<Revision> revisions;

    public Page(final String id, final Integer ns, final String title, final List<Revision> revisions) {
        super();
        this.pageid = id;
        this.ns = ns;
        this.title = title;
        this.revisions = revisions;
    }

    public Page() {
        super();
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(final String id) {
        this.pageid = id;
    }

    public Integer getNs() {
        return ns;
    }

    public void setNs(final Integer ns) {
        this.ns = ns;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }
    
    public List<Revision> getRevisions() {
        return revisions;
    }
    
    public void setRevisions(final List<Revision> revisions) {
        this.revisions = revisions;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(pageid);
    }
    
    @Override
    public boolean equals(final Object obj) {
        final boolean isEqual;
        if (obj == null || !(obj instanceof Page)) {
            isEqual = false;
        } else {
            isEqual = Objects.equal(this.pageid, ((Page) obj).pageid);
        }
        return isEqual;
    }
    
    @Override
    public String toString() {
        final ToStringHelper helper = Objects.toStringHelper(this).add("pageid", pageid).add("ns", ns).add("title", title);
        if (!revisions.isEmpty()) {
            helper.add("revision", revisions.get(0));
        }
        return helper.toString();
    }

}
