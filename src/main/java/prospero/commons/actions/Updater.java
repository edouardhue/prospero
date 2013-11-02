package prospero.commons.actions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prospero.commons.Arguments;
import prospero.commons.ProsperoBot;
import prospero.commons.mediawiki.MWEditResponse;
import prospero.commons.mediawiki.MWLoginResponse;

import com.google.common.base.Charsets;

public final class Updater implements Runnable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Updater.class);

    private final BlockingQueue<PageUpdate> pageUpdates;
    
    private final Arguments arguments;
    
    private volatile boolean exhausted;
    
    private boolean loggedIn;
    
    public Updater(final ProsperoBot bot) {
        this.pageUpdates = bot.getPageUpdates();
        this.arguments = bot.getArguments();
        this.exhausted = false;
        this.loggedIn = false;
    }
    
    public void login() throws IOException {
        final MWLoginResponse firstLoginPhaseResponse = this.performFirstLoginPhase(arguments.getLogin(), arguments.getPassword());
        if (firstLoginPhaseResponse.getError() == null) {
            final String firstLoginPhaseResult =  firstLoginPhaseResponse.getLogin().getResult();
            if ("NeedToken".equals(firstLoginPhaseResult)) {
                final MWLoginResponse secondLoginPhaseResponse = this.performSecondLoginPhase(arguments.getLogin(), arguments.getPassword(), firstLoginPhaseResponse.getLogin().getToken());
                if (secondLoginPhaseResponse.getError() == null) {
                    final String secondLoginPhaseResult = secondLoginPhaseResponse.getLogin().getResult();
                    if ("Success".equals(secondLoginPhaseResult)) {
                        this.loggedIn = true;
                    } else {
                        LOGGER.error("Can't perform second login phase, Mediawiki said {}", secondLoginPhaseResult);
                        throw new IllegalStateException("Unable to login");
                    }
                } else {
                    LOGGER.error("Error while performing second login phase, Mediawiki said {}: {}", secondLoginPhaseResponse.getError().getCode(), secondLoginPhaseResponse.getError().getInfo());
                    throw new Error("Mediawiki error");
                }
            } else if (!"Success".equals(firstLoginPhaseResult)) {
                LOGGER.error("Can't perform first login phase, Mediawiki said {}", firstLoginPhaseResult);
                throw new IllegalStateException("Unable to login");
            }
        } else {
            LOGGER.error("Error while performing first login phase, Mediawiki said {}: {}", firstLoginPhaseResponse.getError().getCode(), firstLoginPhaseResponse.getError().getInfo());
            throw new Error("Mediawiki error");
        }
    }
    
    private MWLoginResponse performFirstLoginPhase(final String login, final String password) throws IOException {
        try (final InputStream is = Request.Post(arguments.getApiUri())
            .bodyForm(
                    Arrays.asList(
                        new BasicNameValuePair("action", "login"),
                        new BasicNameValuePair("lgname", login),
                        new BasicNameValuePair("lgpassword", password),
                        new BasicNameValuePair("format", "json")
                    ),
                    Charsets.UTF_8
            )
            .addHeader("User-Agent", ProsperoBot.BUNDLE.getString("useragent"))
            .execute()
            .returnContent()
            .asStream()) {
            return ProsperoBot.MAPPER.readValue(is, MWLoginResponse.class);
        }
    }
    
    private MWLoginResponse performSecondLoginPhase(final String login, final String password, final String token) throws IOException {
        try (final InputStream is = Request.Post(arguments.getApiUri())
            .bodyForm(
                    Arrays.asList(
                        new BasicNameValuePair("action", "login"),
                        new BasicNameValuePair("lgname", login),
                        new BasicNameValuePair("lgpassword", password),
                        new BasicNameValuePair("lgtoken", token),
                        new BasicNameValuePair("format", "json")
                    ),
                    Charsets.UTF_8
            )
            .addHeader("User-Agent", ProsperoBot.BUNDLE.getString("useragent"))
            .execute()
            .returnContent()
            .asStream()) {
            return ProsperoBot.MAPPER.readValue(is, MWLoginResponse.class);
        }
    }
    
    @Override
    public void run() {
        if (loggedIn) {
            while (!exhausted || !pageUpdates.isEmpty()) {
                try {
                    final PageUpdate update = pageUpdates.poll(2L, TimeUnit.SECONDS);
                    if (update != null) {
                        try {
                            try (final InputStream is = Request.Post(arguments.getApiUri())
                                .bodyForm(
                                        Arrays.asList(
                                            new BasicNameValuePair("action", "edit"),
                                            new BasicNameValuePair("title", update.getPage().getTitle()),
                                            new BasicNameValuePair("text", update.getNextRevision()),
                                            new BasicNameValuePair("summary", "Updating Mérimée template with missing parameters"),
                                            new BasicNameValuePair("bot", "1"),
                                            new BasicNameValuePair("basetimestamp", update.getPage().getRevisions().get(0).getTimestamp()),
                                            new BasicNameValuePair("starttimestamp", update.getPage().getStarttimestamp()),
                                            new BasicNameValuePair("nocreate", "1"),
                                            new BasicNameValuePair("watchlist", "nochange"),
                                            new BasicNameValuePair("format", "json"),
                                            new BasicNameValuePair("token", update.getPage().getEdittoken())
                                        ),
                                        Charsets.UTF_8
                                )
                                .addHeader("User-Agent", ProsperoBot.BUNDLE.getString("useragent"))
                                .execute()
                                .returnContent()
                                .asStream()) {
                                final MWEditResponse editResponse = ProsperoBot.MAPPER.readValue(is, MWEditResponse.class);
                                if (editResponse.getError() == null) {
                                    if ("Success".equals(editResponse.getEdit().getResult())) {
                                        LOGGER.info("Successfully updated page {}", update.getPage().getTitle());
                                    } else {
                                        LOGGER.warn("Update of page {} failed", update.getPage().getTitle());
                                    }
                                } else {
                                    LOGGER.error("Error while updating page {}, Mediawiki said {}: {} ; {}\n", update.getPage().getTitle(), editResponse.getError().getCode(), editResponse.getError().getInfo(), editResponse.getError().getOther(), update.getNextRevision());
                                    throw new Error("Mediawiki error");
                                }
                            }
                        } catch (final IOException e) {
                            LOGGER.warn("Can't update page {}", update.getPage().getTitle(), e);
                        }
                    }
                } catch (final InterruptedException e) {
                    Thread.interrupted();
                    break;
                }
            }
        } else {
            throw new IllegalStateException("Not logged in");
        }
    }

    public void notifyExhaustion() {
        this.exhausted = true;
    }
}
