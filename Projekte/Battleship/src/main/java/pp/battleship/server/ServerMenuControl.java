package pp.battleship.server;


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

public class ServerMenuControl extends GridPane {
    private static final Logger LOGGER = Logger.getLogger(ServerMenuControl.class.getName());

    private static final String SERVER_MENU_CONTROL_FXML = "ServerMenuControl.fxml"; //NON-NLS
    private final ServerApp bs;


    @FXML
    private Button portConf;

    @FXML
    private Label portLabel;

    @FXML
    private TextField portF;

    /**
     * creates the Control Menu for the Server GUI
     * @param bs the App in which this class is used
     */
    ServerMenuControl(ServerApp bs) {
        this.bs = bs;
        final URL location = getClass().getResource(SERVER_MENU_CONTROL_FXML);
        LOGGER.info(SERVER_MENU_CONTROL_FXML + " -> " + location); //NON-NLS
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
     * calls the App to start the Server
     */
    @FXML
    private void confirmPort() {
        bs.startSever();
    }

    /**
     * Getter method for the port given in the text field of the GUI
     * @return the port as string
     */
    String getPort() {
        return portF.getText().trim();
    }

    /**
     * is called when the server is created to prohibit changes
     */
    void setCreated() {
        portF.setEditable(false);
        portConf.setDisable(true);
        portLabel.setText(Resources.getString("server.active"));
    }
}
