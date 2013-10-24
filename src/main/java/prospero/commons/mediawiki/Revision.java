package prospero.commons.mediawiki;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public final class Revision {
    private String contentformat;

    private String contentmodel;

    @JsonProperty("*")
    private String content;

    public String getContentformat() {
        return contentformat;
    }

    public void setContentformat(final String contentformat) {
        this.contentformat = contentformat;
    }

    public String getContentmodel() {
        return contentmodel;
    }

    public void setContentmodel(final String contentmodel) {
        this.contentmodel = contentmodel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("content", content).toString();
    }

}
