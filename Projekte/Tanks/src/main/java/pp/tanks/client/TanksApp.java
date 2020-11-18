package pp.tanks.client;

import pp.network.Connection;
import pp.network.IConnection;
import pp.network.MessageReceiver;
import pp.tanks.message.client.IClientMessage;
import pp.tanks.message.client.ClientReadyMessage;
import pp.tanks.message.client.PingResponse;
import pp.tanks.message.server.IServerInterpreter;
import pp.tanks.message.server.IServerMessage;
import pp.tanks.message.server.PingMessage;
import pp.tanks.message.server.ServerTankUpdateMessage;
import pp.tanks.message.server.SetPlayerMessage;
import pp.tanks.message.server.StartingMultiplayerMessage;
import pp.tanks.message.server.SynchronizeMessage;

import javafx.application.Application;
import javafx.stage.Stage;

import pp.tanks.controller.Engine;
import pp.tanks.controller.MainMenuController;
import pp.tanks.model.item.PlayerEnum;
import pp.tanks.server.GameMode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.net.Socket;

/**
 * Main class of the Tank app
 */
public class TanksApp extends Application implements MessageReceiver<IServerMessage, IConnection<IClientMessage>>, IServerInterpreter {
    private static final String PROPERTIES_FILE = "tanks.properties";
    private static final Logger LOGGER = Logger.getLogger(TanksApp.class.getName());
    private Connection<IClientMessage, IServerMessage> connection;
    public final Sounds sounds = new Sounds();
    private PlayerEnum player;

    private long offset;

    private final Properties properties = new Properties();
    private Engine engine;

    private Stage stage;
    private MainMenuController mainMenuControl;

    /**
     * create a new TankApp
     */
    public TanksApp() {
        load(PROPERTIES_FILE);
    }

    /**
     * main method
     *
     * @param args input args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * start Method for JavaFX
     *
     * @param stage the stage to show
     */
    @Override
    public void start(Stage stage) {
        this.engine = new Engine(stage, this, properties);
        this.stage = stage;

        stage.setResizable(false);
        stage.setTitle("Tanks");
        stage.show();
        engine.gameLoop();
        //sounds.setMusic(sounds.mainMenu);
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
     * @return current player
     */
    public PlayerEnum getPlayer() {
        return player;
    }

    /**
     * This message is called whenever this client receives a message from the server.
     *
     * @param message the text of the received message
     */
    @Override
    public void receiveMessage(IServerMessage message, IConnection<IClientMessage> conn) {
        message.accept(this);
    }

    /**
     * Executed when a connection gets closed
     *
     * @param conn the connection that has been closed
     */
    @Override
    public void onConnectionClosed(IConnection<IClientMessage> conn) {
        //System.exit(0);
        System.out.println("quit connection");
        connection.shutdown();
        connection = null;
    }

    /**
     * Establishes a connection to an online server
     *
     * @param mode given Player-mode
     */
    public void joinGame(GameMode mode) {
        joinGame(mode, "127.0.0.1", "1234");
    }

    /**
     * Establishes a connection to an online server
     *
     * @param mode given Player-mode
     */
    public void joinGame(GameMode mode, String ipAddress, String portString) {
        if (connection != null) {
            LOGGER.severe("trying to join a game again"); //NON-NLS
            return;
        }
        try {
            final int port = Integer.parseInt(portString);
            final Socket socket = new Socket(ipAddress, port);
            socket.setSoTimeout(1000);
            connection = new Connection<>(socket, this);
            if (connection.isConnected()) {
                connection.send(new ClientReadyMessage(mode));
                new Thread(connection).start();
            }
            else {
                LOGGER.info("Establishing a server connection failed"); //NON-NLS
                connection = null;
            }
        }
        catch (IllegalArgumentException | IOException e) {
            LOGGER.info("when creating the Client: " + e.getMessage()); //NON-NLS
        }
    }

    public Connection<IClientMessage, IServerMessage> getConnection() {
        return connection;
    }

    public long getOffset() {
        return offset;
    }

    /**
     * method used by the visitor to react to this message
     *
     * @param msg the SynchronizeMsg to send
     */
    @Override
    public void visit(SynchronizeMessage msg) {
        this.offset = msg.nanoOffset;
        System.out.println(" offset: " + msg.nanoOffset);
    }

    /**
     * method used by the visitor to react to this message
     *
     * @param msg the PingMsg to send
     */
    @Override
    public void visit(PingMessage msg) {
        connection.send(new PingResponse(System.nanoTime()));
    }

    @Override
    public void visit(SetPlayerMessage msg) {
        engine.setPlayerEnum(msg.player);
        this.player = msg.player;
        System.out.println(msg.player);
    }

    @Override
    public void visit(ServerTankUpdateMessage msg) {
        engine.tankConfigMPController.playerConnected();
        engine.tankConfigMPController.serverUpdate(msg);
    }

    @Override
    public void visit(StartingMultiplayerMessage msg) {
        engine.tankConfigMPController.startGame(msg);
    }
}

