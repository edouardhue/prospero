package prospero.commons.tasks;

import java.util.concurrent.RecursiveAction;
import java.util.regex.Pattern;

import prospero.commons.mediawiki.Page;

public final class MerimeeCorrectorTask extends RecursiveAction {
    private static final long serialVersionUID = 1L;
    
    static final Pattern MÉRIMÉE_PATTERN = Pattern.compile("\\{\\{Mérimée\\s*(\\|.+?)+\\}\\}", Pattern.DOTALL);
    
    static final Pattern ID_PATTERN = Pattern.compile("\\|(?:\\s*[123]\\s*=\\s*)([PIE]A\\d{8})\\s*");

    static final Pattern TYPE_PATTERN = Pattern.compile("\\|\\s*type\\s*=\\s*(.+)\\s*");
    
    private final Page page;
    
    public MerimeeCorrectorTask(final Page page) {
        this.page = page;
    }
    
    @Override
    protected void compute() {
    }

}
