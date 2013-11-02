package prospero.commons;

import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prospero.commons.actions.CategoryMembersAction;
import prospero.commons.actions.PageUpdate;
import prospero.commons.actions.Updater;
import prospero.commons.merimee.MerimeeReader;
import prospero.commons.merimee.Monument;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ProsperoBot {
    public static final ResourceBundle BUNDLE = ResourceBundle.getBundle("prospero.prospero");

    private static final Logger LOGGER = LoggerFactory.getLogger(ProsperoBot.class);

    public static final ObjectMapper MAPPER = new ObjectMapper();

    private final Arguments arguments;
    
    private final BlockingQueue<PageUpdate> pageUpdates;
    
    private Map<String, Monument> monuments;

    private final Updater updater;

    private final ForkJoinPool pool;
    
    public ProsperoBot(final Arguments arguments) {
        this.arguments = arguments;
        this.pageUpdates = new LinkedBlockingQueue<>(MerimeeReader.MAGIC_MONUMENT_COUNT);
        this.updater = new Updater(this);
        this.pool = new ForkJoinPool();
    }
    
    public Arguments getArguments() {
        return arguments;
    }
    
    public Map<String, Monument> getMonuments() {
        return monuments;
    }
    
    public BlockingQueue<PageUpdate> getPageUpdates() {
        return pageUpdates;
    }
    
    public void notifyExhausted() {
        new Thread() {
            @Override
            public void run() {
                try {
                    pool.shutdown();
                    pool.awaitTermination(15L, TimeUnit.SECONDS);
//                    Thread.sleep(3000L);
                    updater.notifyExhaustion();
                } catch (final InterruptedException e) {
                    Thread.interrupted();
                }
            }
        }.start();
    }
    
    public void run() throws IOException, InterruptedException {
        LOGGER.info("Hello");
        final long start = System.nanoTime();
        final MerimeeReader reader = new MerimeeReader();
        this.monuments = reader.read(arguments.getMerimeeFile());
        final long end = System.nanoTime();
        LOGGER.info("Found {} monuments in {} ms", monuments.size(), (end - start) / 1E6);
        updater.login();
        final Thread updaterThread = new Thread(updater, "updater-thread");
        final CategoryMembersAction task = new CategoryMembersAction(this);
        pool.invoke(task);
        updaterThread.start();
        updaterThread.join();
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
