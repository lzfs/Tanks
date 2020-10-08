package pp.battleship.server;

import pp.battleship.message.server.ModelMessage;
import pp.battleship.message.server.ServerMessage;
import pp.battleship.model.Battleship;
import pp.battleship.model.ClientState;
import pp.battleship.model.ShipMap;
import pp.network.IConnection;

import java.util.List;

/**
 * Class representing a player
 */
public class Player {
    private final Model model;
    private final ShipMap map;
    private final ShipMap harbor;
    private final String name;
    private final IConnection<ServerMessage> connection;
    private String infoText = "";
    private ClientState state = ClientState.WAIT;

    /**
     * Creates new Player
     *
     * @param connection the connection to the client represented by this player
     * @param name       the human readable name of this player
     * @param model      model holding the player
     */
    Player(IConnection<ServerMessage> connection, String name, Model model) {
        this.connection = connection;
        this.name = name;
        this.model = model;
        final Config config = model.getConfig();
        map = new ShipMap(config.getMapWidth(), config.getMapHeight());
        harbor = new ShipMap(config.getHarborWidth(), config.getHarborHeight());
        config.getShipYard().forEach((len, num) -> {
            for (int i = 0; i < num; i++)
                harbor.getShips().add(new Battleship(len));
        });
        harbor.orderShips();
    }

    /**
     * Returns the connection to the client represented by this player.
     *
     * @return connection
     */
    public IConnection<ServerMessage> getConnection() {
        return connection;
    }

    /**
     * Returns the other player in this game.
     *
     * @return opponent to this player
     */
    public Player opponent() {
        return model.getOpponent(this);
    }

    @Override
    public String toString() {
        return String.format("Player(%s,%s)", name, connection); //NON-NLS
    }

    /**
     * @return map containing own ships and shots
     */
    public ShipMap getMap() {
        return map;
    }

    /**
     * @return ships in the harbor
     */
    public ShipMap getHarbor() {
        return harbor;
    }

    /**
     * @return whether all ships are destroyed
     */
    public boolean hasLost() {
        return map.getShips().stream().allMatch(Battleship::isDestroyed);
    }

    /**
     * @return all ships
     */
    public List<Battleship> getShips() {
        return map.getShips();
    }

    /**
     * rotates the current preview
     */
    public void rotatePreview() {
        final Battleship preview = map.getPreview();
        preview.setRot(preview.getRot().rotate());
    }

    /**
     * Places the specified preview of a ship on this player's own map
     *
     * @param preview preview to be placed
     */
    public void setPreview(Battleship preview) {
        map.setPreview(preview);
    }

    /**
     * @return the current preview or null if no preview is shown.
     */
    public Battleship getPreview() {
        return map.getPreview();
    }

    /**
     * @return the info text to be displayed
     */
    public String getInfoText() {
        return infoText;
    }

    /**
     * sets the info text to be displayed
     */
    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    /**
     * @return the control state for the player client
     */
    public ClientState getState() {
        return state;
    }

    /**
     * sets the control state for the player client
     */
    public void setState(ClientState state) {
        this.state = state;
    }

    /**
     * Creates a message object that also contains all information known so far about the
     * opponent's ships  (i.e., no information about ships that have not been hit yet) to the client.
     */
    public ModelMessage makeModel() {
        return new ModelMessage(map, harbor, opponent().map.knownSoFar(), infoText, state);
    }

    /**
     * Creates a message object that also contains the complete information about the
     * opponent's ships to the client.
     */
    public ModelMessage makeGameOverMapModel() {
        return new ModelMessage(map, harbor, opponent().map, infoText, state);
    }
}
