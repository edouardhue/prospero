package prospero.commons.merimee;

import java.util.Date;

public final class Protection {
    private Date date;

    private Type type;
    
    public Protection(final Date date, final Type type) {
        this.date = date;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public enum Type {
        CLASSE, INSCRIT
    }
}
