package pp.tanks.server.auto;

import pp.network.IConnection;
import pp.network.IServer;
import pp.tanks.client.TanksApp;
import pp.tanks.message.client.BackMessage;
import pp.tanks.message.client.ClientReadyMessage;
import pp.tanks.message.client.UpdateTankConfigMessage;
import pp.tanks.message.server.IServerMessage;
import pp.tanks.message.server.ServerTankUpdateMessage;
import pp.tanks.message.server.SetPlayerMessage;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.PlayerEnum;
import pp.tanks.model.item.Tank;
import pp.tanks.server.GameMode;
import pp.tanks.server.Player;
import pp.tanks.server.TanksServer;

import javafx.fxml.LoadException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TankAutomaton extends TankStateMachine {
    private final TanksServer ts;
    private final List<Player> players = new ArrayList<>();
    private GameMode gameMode;
    private final Properties properties = new Properties();
    public static final Logger LOGGER = Logger.getLogger(TankAutomaton.class.getName());

    public TankAutomaton(TanksServer ts) {
        this.ts = ts;
        load("tanks.properties");
        entry();
    }

    /**
     * the init state of this automaton
     */
    public final TankState init = new TankState() {
        @Override
        public void entry(){
            TankAutomaton.LOGGER.info("init State");
        }

        @Override
        public TankAutomaton containingState() {
            return TankAutomaton.this;
        }

        @Override
        public void playerConnected(ClientReadyMessage msg, IConnection<IServerMessage> conn) {
            players.add(new Player(conn, PlayerEnum.PLAYER1));
            gameMode = GameMode.MULTIPLAYER;
            conn.send(new SetPlayerMessage(PlayerEnum.PLAYER1));
            containingState().goToState(waitingFor2Player);
        }
    };

    /**
     * the state when a multiplayer game is started and a second player needs to connect
     */
    protected final TankState waitingFor2Player = new TankState() {
        @Override
        public void entry(){
            TankAutomaton.LOGGER.info("waiting for player 2");
        }

        private ItemEnum turret = ItemEnum.LIGHT_TURRET;
        private ItemEnum armor = ItemEnum.LIGHT_ARMOR;

        @Override
        public TankAutomaton containingState() {
            return TankAutomaton.this;
        }

        @Override
        public void playerConnected(ClientReadyMessage msg, IConnection<IServerMessage> conn) {
            players.add(new Player(conn, PlayerEnum.PLAYER2));
            players.get(0).setArmor(armor);
            players.get(0).setTurret(turret);
            conn.send(new SetPlayerMessage(PlayerEnum.PLAYER2));
            containingState().goToState(playerReady);
        }

        @Override
        public void back(IConnection<IServerMessage> conn) {
            players.forEach(p -> p.getConnection().shutdown());
            players.clear();
            containingState().goToState(init);
        }

        @Override
        public void playerDisconnected(IConnection<IServerMessage> conn) {
            if (conn == players.get(0).getConnection()) back(conn);
        }

        @Override
        public void updateTankConfig(UpdateTankConfigMessage msg) {
            turret = msg.turret;
            armor = msg.armor;
        }
    };

    /**
     * state for the synchronize of client and server
     */
    public final TankState synchronize = new SynchronizeState(this);

    /**
     * the state where the players choose their tank
     */
    public final TankState playerReady = new PlayerReadyState(this);

    /**
     * state for playing
     */
    public final PlayingState playingState = new PlayingState(this);

    // has to be in Model

    /**
     * Returns the player representing the client with the specified connection.
     *
     * @param conn the connection to the client
     */
    Player getPlayer(IConnection<IServerMessage> conn) {
        final Optional<Player> player = players.stream().filter(p -> p.getConnection() == conn).findAny();
        if (player.isPresent())
            return player.get();
        LOGGER.severe("no player found with connection " + conn); //NON-NLS
        //System.out.println("no player found with connection" + conn);
        return null;
    }

    /**
     * @return list of all players
     */
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public TankState containingState() {
        return null;
    }

    /**
     * @return automaton
     */
    @Override
    TankAutomaton getAuto() {
        return this;
    }

    @Override
    public TankState init() {
        return init;
    }

    /**
     * @return current gameMode
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * load a specified file
     *
     * @param fileName the name of the file as a String
     */
    private void load(String fileName) {
        // first load properties using class loader
        try {
            final InputStream resource = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
            if (resource == null)
                LOGGER.info("Class loader cannot find " + fileName);
            else
                try (Reader reader = new InputStreamReader(resource, StandardCharsets.UTF_8)) {
                    properties.load(reader);
                }
        }
        catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

        // and now try to read the properties file
        final File file = new File(fileName);
        if (file.exists() && file.isFile() && file.canRead()) {
            LOGGER.info("try to read file " + fileName);
            try (FileReader reader = new FileReader(file)) {
                properties.load(reader);
            }
            catch (FileNotFoundException e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }
            catch (IOException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        }
        else
            LOGGER.info("There is no file " + fileName);
        LOGGER.fine(() -> "properties: " + properties);
    }

    /**
     * @return current Properties
     */
    public Properties getProperties() {
        return this.properties;
    }

    public Logger getLogger(){
        return LOGGER;
    }
}
