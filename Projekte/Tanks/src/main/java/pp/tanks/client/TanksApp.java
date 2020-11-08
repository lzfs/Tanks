package pp.tanks.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pp.network.Connection;
import pp.network.IConnection;
import pp.network.MessageReceiver;
import pp.tanks.message.client.ClientReadyMsg;
import pp.tanks.message.client.IClientMessage;
import pp.tanks.message.client.PingResponse;
import pp.tanks.message.server.IServerInterpreter;
import pp.tanks.message.server.IServerMessage;
import pp.tanks.message.server.PingMsg;
import pp.tanks.message.server.SynchronizeMsg;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Main class of the tanks app
 */
public class TanksApp extends Application implements MessageReceiver<IServerMessage, IConnection<IClientMessage>>, IServerInterpreter {
    private static final Logger LOGGER = Logger.getLogger(TanksApp.class.getName());
    private Stage stage;
    private Connection<IClientMessage, IServerMessage> connection;
    private long offset;

    /**
     * starts the tanks application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Actually starts the game within the menu. This method is automatically called when JavaFX starts up.
     *
     * @param stage the main stage provided by JavaFX
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        joinGame();
        stage.setScene(new Scene(new MiniController(this)));
        stage.show();
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
        System.exit(0);
    }

    /**
     * Establishes a connection to an online server
     */
    public void joinGame() {
        if (connection != null) {
            LOGGER.severe("trying to join a game again"); //NON-NLS
            return;
        }
        try {
            final int port = 1234;
            final Socket socket = new Socket("127.0.0.1", port);
            //final Socket socket = new Socket("137.193.138.79", port);
            socket.setSoTimeout(1000);
            connection = new Connection<>(socket, this);
            if (connection.isConnected()) {
                connection.send(new ClientReadyMsg("multiplayer"));
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
     * methode used by the visitor to react to this message
     * @param msg 
     */
    @Override
    public void visit(SynchronizeMsg msg) {
        System.out.println(msg.nanoOffset);
        this.offset = msg.nanoOffset;
    }

    @Override
    public void visit(PingMsg msg) {
        connection.send(new PingResponse(System.nanoTime()));
    }
}
