package prospero.commons.mediawiki;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Error {
    private String code;

    private String info;
    
    @JsonProperty(value = "*")
    private String other;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(final String info) {
        this.info = info;
    }

    public String getOther() {
        return other;
    }
    
    public void setOther(final String other) {
        this.other = other;
    }
}
