package pp.tanks.model;

import pp.tanks.model.item.*;
import pp.tanks.notification.TanksNotification;
import pp.tanks.notification.TanksNotificationReceiver;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * Represents the game model.
 */
public class Model {
    private static final Logger LOGGER = Logger.getLogger(Model.class.getName());
    public static final String MUTED = "muted";
    private final Properties props;
    private final Preferences prefs = Preferences.userNodeForPackage(Model.class);
    private final List<TanksNotificationReceiver> receivers = new ArrayList<>();
    private TanksMap map;
    private boolean muted = prefs.getBoolean(MUTED, false);

    /**
     * Creates a game model
     *
     * @param props The properties storing the configuration of this game.
     */
    public Model(Properties props) {
        this.props = props;
        //make tanks map
    }

    /**
     * Returns whether sound is muted.
     */
    public boolean isMuted() {
        return muted;
    }

    /**
     * Mutes or unmutes the sound depending on the specified value.
     *
     * @param muted the sound is muted if and only if this value is true.
     */
    public void setMuted(boolean muted) {
        this.muted = muted;
        prefs.put(MUTED, String.valueOf(muted));
    }

    /**
     * Returns the configuration of this game.
     */
    public Properties getProperties() {
        return props;
    }

    /**
     * Returns the current game map
     */
    public TanksMap getTanksMap() {
        return map;
    }

    /**
     * Sets the specified game map as the current one.
     *
     * @param map droids map
     */
    public void setTanksMap(TanksMap map) {
        this.map = map;
        notifyReceivers(TanksNotification.MAP_UPDATE);
    }

    /**
     * Loads a game map from the specified xml file and sets it as the current one.
     *
     * @param file xml file representing a droids map
     * @throws IOException        if the file doesn't exist, cannot be opened, or any other IO error occurred.
     * @throws XMLStreamException if the file is no valid xml file
     */
    public void loadMap(File file) throws IOException, XMLStreamException {
        setTanksMap(new TanksMapFileReader(this).readFile(file));
    }

    /**
     * Called once per frame. This method triggers any update of the game model based on the elapsed time.
     *
     * @param deltaTime time in seconds since the last update call
     */
    public void update(double deltaTime) {
        map.update(deltaTime);
    }

    /**
     * Adds the specified receiver to the list of all event notification subscribers.
     */
    public void addReceiver(TanksNotificationReceiver receiver) {
        LOGGER.fine(() -> "add receiver " + receiver);
        receivers.add(receiver);
    }

    /**
     * Removes the specified receiver from the list of all event notification subscribers.
     */
    public void removeReceiver(TanksNotificationReceiver receiver) {
        LOGGER.fine(() -> "remove receiver " + receiver);
        receivers.remove(receiver);
    }

    /**
     * Notifies every registered
     *
     * @param notification The notification event communicated to every registered receiver.
     */
    public void notifyReceivers(TanksNotification notification) {
        for (TanksNotificationReceiver receiver : new ArrayList<>(receivers))
            receiver.notify(notification);
    }

    /**
     * Returns true if amd only if own tank is dead.
     */
    public boolean gameLost() {
        //TODO
        return false;
    }

    /**
     * Returns true if and only if there are no tanks left.
     */
    public boolean gameWon() {
        //TODO
        return false;
    }
}
