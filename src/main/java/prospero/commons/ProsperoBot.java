package prospero.commons;

import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prospero.commons.actions.CategoryMembersAction;
import prospero.commons.merimee.MerimeeReader;
import prospero.commons.merimee.Monument;

public final class ProsperoBot {
    public static final ResourceBundle BUNDLE = ResourceBundle.getBundle("prospero.prospero");

    private static final Logger LOGGER = LoggerFactory.getLogger(ProsperoBot.class);

    private final Arguments arguments;
    
    private Map<String, Monument> monuments;
    
    public ProsperoBot(final Arguments arguments) {
        this.arguments = arguments;
    }
    
    public Map<String, Monument> getMonuments() {
        return monuments;
    }
    
    public void run() throws IOException, InterruptedException {
        LOGGER.info("Hello");
        final long start = System.nanoTime();
        final MerimeeReader reader = new MerimeeReader();
        final Map<String, Monument> monuments = reader.read(arguments.getMerimeeFile());
        final long end = System.nanoTime();
        LOGGER.info("Found {} monuments in {} ms", monuments.size(), (end - start) / 1E6);
        final ForkJoinPool pool = new ForkJoinPool();
        final CategoryMembersAction task = new CategoryMembersAction(this, arguments.getApiUri(), arguments.getCategoryTitle());
        pool.invoke(task);
        pool.awaitTermination(10, TimeUnit.SECONDS);
    }
    
    public static void main(final String[] args) throws IOException, InterruptedException {
        final Arguments arguments = new Arguments();
        final CmdLineParser parser = new CmdLineParser(arguments);
        try {
            parser.parseArgument(args);
            new ProsperoBot(arguments).run();
        } catch (final CmdLineException e) {
            parser.printUsage(System.err);
        }
    }

}
