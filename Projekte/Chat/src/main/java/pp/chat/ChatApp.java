package pp.chat;

import pp.network.IConnection;
import pp.network.MessageReceiver;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Main class of the chat application.
 */
public class ChatApp extends Application implements MessageReceiver<Message, IConnection<Message>> {
    private static final Logger LOGGER = Logger.getLogger(ChatApp.class.getName());
    private Stage stage;
    private PrefsControl prefsControl;
    private ChatControl chatControl;
    private Scene prefsScene;
    private Scene chatScene;
    private Communicator comm;

    /**
     * Starts the chat application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Actually starts the chat. This method is automatically called when JavaFX starts up.
     *
     * @param stage the main stage provided by JavaFX
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;

        prefsControl = new PrefsControl(this);
        prefsScene = new Scene(prefsControl);

        chatControl = new ChatControl(this);
        chatScene = new Scene(chatControl);

        stage.setResizable(false);
        stage.setTitle("Chat");
        stage.setOnCloseRequest(e -> shutdown());
        stage.setScene(prefsScene);
        stage.sizeToScene();
        stage.show();
    }

    /**
     * Sends a the message in the message field of the chat control if there is a connection to the chat server.
     */
    void send() {
        if (comm != null && comm.isConnected()) {
            comm.sendToServer(chatControl.getMessageText());
            chatControl.setMessageText("");
        }
        else {
            disconnect();
            setInfoText("Die Verbindung wurde unterbrochen.");
        }
    }

    /**
     * Handles a click to the connect button. Either starts the server (if required) and the
     * client, or disconnects the server and the client if they have already been started.
     */
    void connect() {
        chatControl.clearChatText();

        if (comm != null && comm.isConnected())
            disconnect();
        else
            try {
                comm = new Communicator(this);
                comm.start();
                setInfoText("");
                final String kind = isServer() ? "Server" : "Client";
                stage.setTitle(String.format("Chat - %s (%s)", kind, getUserName()));
                stage.setScene(chatScene);
                stage.setResizable(true);
            }
            catch (ChatException e) {
                setInfoText(e.getMessage());
                shutdown();
            }
    }

    /**
     * Disconnects this client from the chat server.
     */
    private void disconnect() {
        shutdown();
        stage.setTitle("Chat");
        stage.setScene(prefsScene);
        stage.setResizable(false);
    }

    /**
     * This message is called whenever this client receives a message from the server.
     *
     * @param message the text of the received message
     */
    @Override
    public void receiveMessage(Message message, IConnection<Message> conn) {
        Platform.runLater(() -> {
            StringBuilder sb = new StringBuilder();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            sb.append(sdf.format(date)).append(" ");
            if (getUserName().equals(message.getFrom()) && message.getType() != MessageType.CONNECTED)
                sb.append("Eigene Nachricht: ");
            sb.append(message.getBody());
            chatControl.appendChatMessage(sb.toString());
        });
    }

    /**
     * This message is called when the client detects that it got disconnected from the chat server.
     */
    @Override
    public void onConnectionClosed(IConnection<Message> conn) {
        LOGGER.info("connection closed: " + conn);
        Platform.runLater(() -> {
            disconnect();
            setInfoText("Die Verbindung wurde unterbrochen.");
        });
    }

    /**
     * Shuts down the communication line.
     */
    private void shutdown() {
        if (comm != null) comm.shutdown();
        comm = null;
    }

    /**
     * Sets the info text that is being displayed below the connect button
     *
     * @param text String info text
     */
    private void setInfoText(String text) {
        prefsControl.setInfoText(text);
    }

    /**
     * Returns the port of the server
     */
    public String getServerPort() {
        return prefsControl.getServerPort();
    }

    /**
     * Returns the name chosen by the user
     */
    public String getUserName() {
        return prefsControl.getUserName();
    }

    /**
     * Returns the address of the server
     */
    public String getServerAddress() {
        return prefsControl.getServerAddress();
    }

    /**
     * Returns whether this application does or shall act as a server.
     */
    public boolean isServer() {
        return prefsControl.isServer();
    }
}
