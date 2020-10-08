package pp.chat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;

/**
 * A class representing a user defined JavaFX control for sending chat messages and visualizing a chat protocol. The
 * control is specified by an FXML file as defined in {@link #CHAT_CONTROL_FXML}.
 */
class ChatControl extends GridPane {
    /**
     * The name of the FXML file defining this user defined control
     */
    public static final String CHAT_CONTROL_FXML = "chat_control.fxml";
    private final ChatApp chat;

    /**
     * The message field.
     */
    @FXML
    private TextField messageTf;
    /**
     * The chat protocol.
     */
    @FXML
    private TextArea chatTa;

    /**
     * Creates a new chat control for the specified chat application.
     */
    public ChatControl(ChatApp chat) {
        this.chat = chat;
        final URL location = getClass().getResource(CHAT_CONTROL_FXML);
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends the message within the message field to the server.
     */
    @FXML
    private void send() {
        chat.send();
    }

    /**
     * Disconnects this client from the server.
     */
    @FXML
    private void disconnect() {
        chat.connect();
    }

    /**
     * Returns the message typed into the message field.
     */
    String getMessageText() {
        return messageTf.getText();
    }

    /**
     * Sets the contents of the message field.
     */
    void setMessageText(String text) {
        messageTf.setText(text);
    }

    /**
     * Clears the entire chat protocol.
     */
    void clearChatText() {
        chatTa.clear();
    }

    /**
     * Appends the specified text to the chat protocol.
     */
    void appendChatMessage(String text) {
        chatTa.appendText(text + "\n");
    }
}
