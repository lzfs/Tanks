package pp.droids.model;

import pp.droids.DroidsIntProperty;
import pp.droids.notifications.DroidsNotification;
import pp.droids.notifications.DroidsNotificationReceiver;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * Represents the game model.
 */
public class DroidsGameModel {
    private static final Logger LOGGER = Logger.getLogger(DroidsGameModel.class.getName());
    public static final String DEBUG_MODE = "debug-mode";
    public static final String MUTED = "muted";

    private final Properties props;
    private final Preferences prefs = Preferences.userNodeForPackage(DroidsGameModel.class);
    private final List<DroidsNotificationReceiver> receivers = new ArrayList<>();

    private DroidsMap droidsMap;
    private boolean debugMode = prefs.getBoolean(DEBUG_MODE, false);
    private boolean muted = prefs.getBoolean(MUTED, false);

    private double showHintTime = 0.;

    /**
     * Creates a game model
     *
     * @param props The properties storing the configuration of this game.
     */
    public DroidsGameModel(Properties props) {
        this.props = props;
        setDroidsMap(new DroidsMapCreator(this).makeEmptyMap());
    }

    /**
     * Returns whether the debug mode is activated
     */
    public boolean isDebugMode() {
        return debugMode;
    }

    /**
     * Switches the debug mode on or off depending on the specified value.
     *
     * @param debugMode if debug mode is switched on if and only if this value is true
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        prefs.put(DEBUG_MODE, String.valueOf(debugMode));
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
     * Switches on the hint shown in the view for a certain number of seconds. This duration can be configured by
     * the {@linkplain pp.droids.DroidsIntProperty#hintTime} property.
     */
    public void setShowHint() {
        showHintTime = DroidsIntProperty.hintTime.value(props);
    }

    /**
     * Returns whether the hint shall be shown. This method returns true if and only if the time elapsed since the
     * last call of method {@link #setShowHint()} is less than the configured duration.
     *
     * @see pp.droids.DroidsIntProperty#hintTime
     */
    public boolean getShowHint() {
        return showHintTime > 0.;
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
    public DroidsMap getDroidsMap() {
        return droidsMap;
    }

    /**
     * Sets the specified game map as the current one.
     *
     * @param droidsMap droids map
     */
    public void setDroidsMap(DroidsMap droidsMap) {
        this.droidsMap = droidsMap;
        notifyReceivers(DroidsNotification.MAP_UPDATE);
    }

    /**
     * Generates and sets a random game map.
     */
    public void loadRandomMap() {
        setDroidsMap(new DroidsMapCreator(this).makeRandomMap());
    }

    /**
     * Loads a game map from the specified xml file and sets it as the current one.
     *
     * @param file xml file representing a droids map
     * @throws IOException        if the file doesn't exist, cannot be opened, or any other IO error occurred.
     * @throws XMLStreamException if the file is no valid xml file
     */
    public void loadMap(File file) throws IOException, XMLStreamException {
        setDroidsMap(new DroidsMapFileReader(this).readFile(file));
    }

    /**
     * Saves the current game map to the specified file.
     *
     * @param file xml file representing where the droids map is written to.
     * @throws FileNotFoundException if the file doesn't exist or cannot be opened
     * @throws XMLStreamException    if the file the produced file is not a valid xml file
     */
    public void saveMap(File file) throws FileNotFoundException, XMLStreamException {
        new DroidsMapFileWriter(this).writeFile(file);
    }

    /**
     * Called once per frame. This method triggers any update of the game model based on the elapsed time.
     *
     * @param deltaTime time in seconds since the last update call
     */
    public void update(double deltaTime) {
        if (showHintTime > 0.) showHintTime -= deltaTime;
        droidsMap.update(deltaTime);
    }

    /**
     * Adds the specified receiver to the list of all event notification subscribers.
     *
     * @see #notifyReceivers(pp.droids.notifications.DroidsNotification)
     */
    public void addReceiver(DroidsNotificationReceiver receiver) {
        LOGGER.fine(() -> "add receiver " + receiver);
        receivers.add(receiver);
    }

    /**
     * Removes the specified receiver from the list of all event notification subscribers.
     *
     * @see #notifyReceivers(pp.droids.notifications.DroidsNotification)
     */
    public void removeReceiver(DroidsNotificationReceiver receiver) {
        LOGGER.fine(() -> "remove receiver " + receiver);
        receivers.remove(receiver);
    }

    /**
     * Notifies every registered
     * {@linkplain pp.droids.notifications.DroidsNotificationReceiver}.
     *
     * @param notification The notification event communicated to every registered receiver.
     * @see #addReceiver(pp.droids.notifications.DroidsNotificationReceiver)
     */
    public void notifyReceivers(DroidsNotification notification) {
        for (DroidsNotificationReceiver receiver : new ArrayList<>(receivers))
            receiver.notify(notification);
    }

    /**
     * Returns true if amd only if droid is dead.
     */
    public boolean gameLost() {
        return droidsMap.getDroid().isDestroyed();
    }

    /**
     * Returns true if and only if there are no enemies left.
     */
    public boolean gameWon() {
        return droidsMap.getEnemies().isEmpty();
    }
}
