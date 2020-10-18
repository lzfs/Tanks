package pp.battleship.client;

import pp.battleship.Resources;
import pp.battleship.message.client.ClientMessage;
import pp.battleship.message.client.ClientReadyMessage;
import pp.battleship.message.server.ModelMessage;
import pp.battleship.message.server.ServerInterpreter;
import pp.battleship.message.server.ServerMessage;
import pp.network.Connection;
import pp.network.IConnection;
import pp.network.MessageReceiver;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Main class of the battleship app
 */
public class BattleshipApp extends Application implements MessageReceiver<ServerMessage, IConnection<ClientMessage>>, ServerInterpreter {
    private static final Logger LOGGER = Logger.getLogger(BattleshipApp.class.getName());
    private Stage stage;
    private MenuControl menuControl;
    private BattlefieldControl battlefieldControl;
    private Connection<ClientMessage, ServerMessage> connection;

    /**
     * Starts the battleships application.
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
        menuControl = new MenuControl(this);
        stage.setResizable(false);
        stage.setTitle(Resources.getString("battleships"));
        stage.setOnCloseRequest(this::shutdown);
        stage.setScene(new Scene(menuControl));
        stage.sizeToScene();
        stage.show();
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
            final int port = Integer.parseInt(menuControl.getPort());
            final Socket socket = new Socket(menuControl.getIp(), port);
            socket.setSoTimeout(1000);
            connection = new Connection<>(socket, this);
            if (connection.isConnected()) {
                connection.send(new ClientReadyMessage());
                new Thread(connection).start();
                setInfoText(Resources.getString("wait.for.an.opponent"));
                menuControl.setJoined();
            }
            else {
                setInfoText(Resources.getString("failed.to.establish.a.server.connection"));
                LOGGER.info("Establishing a server connection failed"); //NON-NLS
                connection = null;
            }
        }
        catch (NumberFormatException e) {
            setInfoText(Resources.getString("port.number.must.be.an.integer"));
        }
        catch (IllegalArgumentException | IOException e) {
            LOGGER.info("when creating the Client: " + e.getMessage()); //NON-NLS
            setInfoText(e.getLocalizedMessage());
        }
    }

    /**
     * This message is called whenever this client receives a message from the server.
     *
     * @param message the text of the received message
     */
    @Override
    public void receiveMessage(ServerMessage message, IConnection<ClientMessage> conn) {
        assert connection == conn;
        Platform.runLater(() -> message.accept(this));
    }

    /**
     * Executed when a connection gets closed
     *
     * @param conn the connection that has been closed
     */
    @Override
    public void onConnectionClosed(IConnection<ClientMessage> conn) {
        assert connection == conn;
        Platform.runLater(() -> {
            setInfoText(Resources.getString("lost.connection.to.server"));
            if (connection != null)
                connection.shutdown();
            connection = null;
        });
    }

    /**
     * Method used by the visitor to react to the messages
     *
     * @param model the model to be displayed
     */
    @Override
    public void visit(ModelMessage model) {
        if (battlefieldControl == null) {
            battlefieldControl = new BattlefieldControl(this, model);
            stage.setScene(new Scene(battlefieldControl));
        }
        else
            battlefieldControl.setModel(model);
    }

    /**
     * Closes this Battleship client when the window has been requested to be closed.
     * A conformation is requested if the game is still running.
     *
     * @param e the event that requests closing the window.
     */
    private void shutdown(WindowEvent e) {
        if (connection == null)
            System.exit(0);
        final Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setContentText(Resources.getString("would.you.really.like.to.leave.the.game"));
        confirmation.initOwner(stage);
        final Optional<ButtonType> result = confirmation.showAndWait();
        if (result.orElse(ButtonType.NO) == ButtonType.OK) {
            connection.shutdown();
            System.exit(0);
        }
        // prevent closing
        e.consume();
    }

    /**
     * Sets the info text in the menu and, if existing, the battlefield control
     *
     * @param text the info text to be set
     */
    private void setInfoText(String text) {
        menuControl.setInfoText(text);
        if (battlefieldControl != null)
            battlefieldControl.setInfoText(text);
    }

    /**
     * Sends the specified message to the server.
     *
     * @param message message to be sent to the server
     */
    void send(ClientMessage message) {
        connection.send(message);
    }

    public void newGame() {
        if (connection != null) connection.send(new ClientReadyMessage());
    }

    public Stage getStage() {
        return stage;
    }

    public Connection<ClientMessage, ServerMessage> getConnection() {
        return connection;
    }

    public void toLobby() {
        if (connection != null) connection.shutdown();
        this.menuControl = new MenuControl(this);
        stage.setScene(new Scene(menuControl));
    }
}
