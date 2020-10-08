package pp.chat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;

/**
 * A class representing a user defined JavaFX control for setting the chat preferences. The
 * control is specified by an FXML file as defined in {@link #PREFS_CONTROL_FXML}.
 */
class PrefsControl extends GridPane {
    public static final String PREFS_CONTROL_FXML = "prefs_control.fxml";
    private final ChatApp chat;

    /**
     * The server address
     */
    @FXML
    private TextField serverAddressTf;
    /**
     * The server port
     */
    @FXML
    private TextField serverPortTf;
    /**
     * The name chosen by the user
     */
    @FXML
    private TextField usernameTf;
    /**
     * A checkbox indicating whether this application shall act as a server.
     */
    @FXML
    private CheckBox isServerCb;
    /**
     * The button for connecting to the server
     */
    @FXML
    private Button connectBtn;
    /**
     * A label for visualizing any message
     */
    @FXML
    private Label infoTextLbl;

    /**
     * Creates a new preferences control for the specified chat application.
     */
    public PrefsControl(ChatApp chat) {
        this.chat = chat;
        final URL location = getClass().getResource(PREFS_CONTROL_FXML);
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
     * Sets whether the instance is used as a host or not
     */
    @FXML
    void setServerMode() {
        serverAddressTf.setDisable(isServer());

        if (isServer())
            connectBtn.setText("Server starten");
        else
            connectBtn.setText("Verbindung zum Server herstellen");
    }

    /**
     * Handles a click to the connect button. Either starts the server (if required) and the
     * client, or disconnects the server and the client if they have already been started.
     */
    @FXML
    void handleConnect() {
        chat.connect();
    }

    /**
     * Emits a message as an information text.
     *
     * @param text String info text
     */
    void setInfoText(String text) {
        infoTextLbl.setText(text);
    }

    /**
     * Returns the port of the server
     */
    public String getServerPort() {
        return serverPortTf.getText().trim();
    }

    /**
     * Returns the name chosen by the user
     */
    public String getUserName() {
        return usernameTf.getText().trim();
    }

    /**
     * Returns the address of the server
     */
    public String getServerAddress() {
        return serverAddressTf.getText().trim();
    }

    /**
     * Returns whether this application does or shall act as a server.
     */
    public boolean isServer() {
        return isServerCb.isSelected();
    }
}
