package prospero.commons.mediawiki;

abstract class MWBaseResponse {
    private Error error;
    
    public Error getError() {
        return error;
    }
    
    public void setError(final Error error) {
        this.error = error;
    }
}
