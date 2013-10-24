package prospero.commons;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prospero.commons.merimee.MerimeeReader;
import prospero.commons.merimee.Monument;
import prospero.commons.tasks.CategoryMembersAction;

public final class ProsperoBot {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProsperoBot.class);

    public static void main(final String[] args) throws FileNotFoundException, IOException, InterruptedException {
        LOGGER.info("Hello");
        final long start = System.nanoTime();
        final Arguments arguments = new Arguments();
        final CmdLineParser parser = new CmdLineParser(arguments);
        try {
            parser.parseArgument(args);
            final MerimeeReader reader = new MerimeeReader();
            final Map<String, Monument> monuments = reader.read(arguments.getMerimeeFile());
            final long end = System.nanoTime();
            LOGGER.info("Found {} monuments in {} ms", monuments.size(), (end - start) / 1E6);
            final ForkJoinPool pool = new ForkJoinPool();
            final CategoryMembersAction task = new CategoryMembersAction(arguments.getApiUri(), arguments.getCategoryTitle());
            pool.invoke(task);
            pool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (final CmdLineException e) {
            parser.printUsage(System.err);
        }
    }

}
