package prospero.commons.mediawiki;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public final class Continue {
    @JsonProperty("continue")
    private String queryContinue;
    
    private String gcmcontinue;

    public Continue() {
        this.queryContinue = "";
        this.gcmcontinue = "";
    }
    
    public String getGcmcontinue() {
        return gcmcontinue;
    }
    
    public void setGcmcontinue(String gcmcontinue) {
        this.gcmcontinue = gcmcontinue;
    }
    
    public String getQueryContinue() {
        return queryContinue;
    }
    
    public void setQueryContinue(String queryContinue) {
        this.queryContinue = queryContinue;
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("continue", queryContinue).add("gcmcontinue", gcmcontinue).toString();
    }
}
