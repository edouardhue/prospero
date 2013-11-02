package prospero.commons.mediawiki;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public final class Revision {
    private String timestamp;
    
    @JsonProperty("*")
    private String content;

    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("timestamp", timestamp).add("content", content).toString();
    }

}
