package pp.battleship.client;

import pp.battleship.Resources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * A class representing a user defined JavaFX control for starting a game.
 * The control is specified by an FXML file.
 */
class MenuControl extends GridPane {
    private static final Logger LOGGER = Logger.getLogger(MenuControl.class.getName());

    private static final String MENU_CONTROL_FXML = "MenuControl.fxml"; //NON-NLS
    private final BattleshipApp app;

    /**
     * The Server IP
     */
    @FXML
    private TextField ipTf;
    /**
     * The server port
     */
    @FXML
    private TextField portTf;
    /**
     * The button for joining a game
     */
    @FXML
    private Button joinBtn;
    /**
     * A label for visualizing any message
     */
    @FXML
    private Label infoTextLbl;

    /**
     * Creates a new preferences control for the specified chat application.
     */
    MenuControl(BattleshipApp app) {
        this.app = app;
        final URL location = getClass().getResource(MENU_CONTROL_FXML);
        LOGGER.info(MENU_CONTROL_FXML + " -> " + location); //NON-NLS
        FXMLLoader fxmlLoader = new FXMLLoader(location, Resources.RESOURCE_BUNDLE);
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
     * Handles a click to the join game button. Starts the client and connect to the server, if possible.
     */
    @FXML
    private void handleJoinGame() {
        app.joinGame();
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
     * Method called if the connection has been established. Disables the controls.
     */
    void setJoined() {
        ipTf.setEditable(false);
        portTf.setEditable(false);
        joinBtn.setDisable(true);
    }

    /**
     * Returns the port of the server
     */
    String getPort() {
        return portTf.getText().trim();
    }

    /**
     * Returns the address of the server
     */
    String getIp() {
        return ipTf.getText().trim();
    }
}
