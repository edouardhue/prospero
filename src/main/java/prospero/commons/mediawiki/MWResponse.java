package prospero.commons.mediawiki;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public final class MWResponse {
    private Query query;
    
    @JsonProperty("continue")
    private Continue qContinue;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Continue getqContinue() {
        return qContinue;
    }

    public void setqContinue(Continue qContinue) {
        this.qContinue = qContinue;
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("query", query)
                .add("continue", qContinue)
                .toString();
    }
}
