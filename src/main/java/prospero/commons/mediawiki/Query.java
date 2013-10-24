package prospero.commons.mediawiki;

import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public final class Query {
    private Map<String, Page> pages;
    
    public Query() {
    }
    
    public Map<String, Page> getPages() {
        return pages;
    }
    
    public void setPages(Map<String, Page> pages) {
        this.pages = pages;
    }
    
    @Override
    public String toString() {
        final ToStringHelper helper = Objects.toStringHelper(this);
        for (final Map.Entry<String, Page> entry : pages.entrySet()) {
            helper.add(entry.getKey(), entry.getValue());
        }
        return helper.toString();
    }
}
