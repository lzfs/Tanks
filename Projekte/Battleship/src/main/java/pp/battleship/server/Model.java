package pp.battleship.server;

import pp.battleship.message.server.ServerMessage;
import pp.network.IConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Game model holding the players
 */
public class Model {
    private static final Logger LOGGER = Logger.getLogger(Model.class.getName());
    private final Config config;
    private final List<Player> players = new ArrayList<>(2);

    /**
     * Creates a new model
     *
     * @param config sets the parameters for the model
     */
    Model(Config config) {
        this.config = config;
    }

    /**
     * Returns the configuration of the model
     *
     * @return configuration holding parameters
     */
    Config getConfig() {
        return config;
    }

    /**
     * Returns Player n
     *
     * @param n the number which player to return
     * @return the player number n
     */
    public Player getPlayer(int n) {
        return players.get(n);
    }

    /**
     * Creates and adds a payer representing the client with the specified connection.
     *
     * @param conn the connection to the client
     * @return the player
     */
    public Player addPlayer(IConnection<ServerMessage> conn) {
        final int n = players.size() + 1;
        if (n >= 3)
            throw new RuntimeException("Trying to add a third player");
        final Player player = new Player(conn, "player " + n, this); //NON-NLS
        LOGGER.info("adding " + player); //NON-NLS
        players.add(player);
        return player;
    }

    /**
     * Returns the opponent of the specified player
     *
     * @param p player
     * @return opponent of p
     */
    Player getOpponent(Player p) {
        if (players.size() != 2)
            throw new RuntimeException("trying to find opponent without having 2 players");
        final int index = players.indexOf(p);
        if (index < 0)
            throw new RuntimeException("Nonexistent player " + p);
        return players.get(1 - index);
    }

    /**
     * Returns the player representing the client with the specified connection.
     *
     * @param conn the connection to the client
     */
    Player getPlayer(IConnection<ServerMessage> conn) {
        final Optional<Player> player = players.stream().filter(p -> p.getConnection() == conn).findAny();
        if (player.isPresent())
            return player.get();
        LOGGER.severe("no player found with connection " + conn); //NON-NLS
        return null;
    }
}
