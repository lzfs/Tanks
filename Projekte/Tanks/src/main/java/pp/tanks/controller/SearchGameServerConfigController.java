package pp.tanks.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller displaying the input fields to connect to an existing server.
 */
public class SearchGameServerConfigController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(SearchGameServerConfigController.class.getName());
    private static final String SEARCH_GAME_SERVER_CONFIG_FXML = "SearchGameServerConfig.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new SearchGameServerConfigController
     *
     * @param engine the engine this controller belongs to
     */
    public SearchGameServerConfigController(Engine engine) {
        super(engine);
    }

    /**
     * the button to search for to search for a server with the specified ip-address and port
     */
    @FXML
    private Button search;

    /**
     * the text-field for the ip-address
     */
    @FXML
    private TextField ipField;

    /**
     * the text-field for the port
     */
    @FXML
    private TextField portField;

    /**
     * text to display information for the user
     */
    @FXML
    private Text infoText;

    /**
     * the button to go back
     */
    @FXML
    private Button back;

    /**
     * Create the scene displaying the ServerConfig.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(SEARCH_GAME_SERVER_CONFIG_FXML, this));
    }

    /**
     * @return the name of the used file as a String
     */
    public String getFileName() {
        return SEARCH_GAME_SERVER_CONFIG_FXML;
    }

    /**
     * This method is called whenever this controller is activated,
     * i.e., when the the user clicks on "search game" in the lobby.
     */
    @Override
    void entry() {
        LOGGER.log(Level.INFO, "ENTRY SearchGameServerConfigController");
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    /**
     * This method is called whenever this controller is deactivated,
     * i.e., when the user the input fields contain valid values and the user clicks on "search"
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT SearchGameServerConfigController");
    }

    /**
     * method for the search button
     */
    @FXML
    private void search() {
        LOGGER.log(Level.INFO, "clicked SEARCH");
        try {
            engine.getTankApp().joinGame(getIpAddress(), getPort());
        }
        catch (NumberFormatException e) {
            infoText.setText("Falscher Port.");
            return;
        }
        catch (IllegalArgumentException | IOException e) {
            infoText.setText("Der Server existiert nicht.");
            return;
        }
        engine.activateTankConfigMPController();
    }

    /**
     * method for the back button
     */
    @FXML
    private void back() {
        engine.activateLobbyController();
    }

    /**
     * getter method for the port given in the text field of the GUI
     *
     * @return the port as a string
     */
    public String getPort() {
        return portField.getText().trim();
    }

    /**
     * getter method for the ip-address given in the text field of the GUI
     *
     * @return the ip-address as a string
     */
    public String getIpAddress() {
        return ipField.getText().trim();
    }
}

