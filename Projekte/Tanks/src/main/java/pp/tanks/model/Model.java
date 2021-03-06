package pp.tanks.model;

import pp.tanks.controller.Engine;
import pp.tanks.model.item.PlayerEnum;
import pp.tanks.model.item.Tank;
import pp.tanks.notification.TanksNotification;
import pp.tanks.notification.TanksNotificationReceiver;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Represents the game model.
 */
public class Model {
    private static final Logger LOGGER = Logger.getLogger(Model.class.getName());
    private final Properties props;
    private final List<TanksNotificationReceiver> receivers = new ArrayList<>();
    private TanksMap map;
    private long latestUpdate;
    private Engine engine;

    /**
     * Creates a game model
     *
     * @param props The properties storing the configuration of this game.
     */
    public Model(Properties props) {
        this.props = props;
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
     * @param map tanks map
     */
    public void setTanksMap(TanksMap map) {
        this.map = map;
        notifyReceivers(TanksNotification.MAP_UPDATE);
    }

    /**
     * Loads a game map from the specified xml file and sets it as the current one.
     *
     * @param string xml file name representing a tanks map
     */
    public void loadMap(String string) {
        final InputStream stream = getClass().getResourceAsStream(string);

        try {
            setTanksMap(new TanksMapFileReader(this).readFile(stream));
        } catch (IOException | XMLStreamException ex) {
            System.out.println(ex.getMessage());
            System.out.println("APOKALYPSE");
        }
    }

    /**
     * updates tank
     *
     * @param tank new tank
     */
    public void setTank(Tank tank) {
        map.addPlayerTank(tank);
    }

    /**
     * Called once per frame. This method triggers any update of the game model based on the elapsed time.
     *
     * @param serverTime time in seconds since the last update call
     */
    public void update(long serverTime) {
        map.update(serverTime);
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
        return map.getTank(engine.getPlayerEnum()).isDestroyed();
    }

    /**
     * Returns true if and only if there are no tanks left.
     */
    public boolean gameWon() {
        if (map.getTank(PlayerEnum.PLAYER1).isDestroyed()) return false;
        for (Tank tanks : map.getCOMTanks()) {
            if (tanks != map.getTank(engine.getPlayerEnum()) && !tanks.isDestroyed()) return false;
        }
        return true;
    }

    /**
     * @return boolean-value if game has finished
     */
    public boolean gameFinished() {
        if (map.getTank(PlayerEnum.PLAYER1).isDestroyed()) return true;
        return map.getTank(PlayerEnum.PLAYER2).isDestroyed();
    }

    /**
     * @return latestUpdate
     */
    public long getLatestUpdate() {
        return latestUpdate;
    }

    /**
     * updates latestUpdate
     *
     * @param latestUpdate new latestUpdate
     */
    public void setLatestUpdate(long latestUpdate) {
        this.latestUpdate = latestUpdate;
    }

    /**
     * updates engine
     *
     * @param engine "new" engine
     */
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    /**
     * @return current engine
     */
    public Engine getEngine() {
        return this.engine;
    }
}
