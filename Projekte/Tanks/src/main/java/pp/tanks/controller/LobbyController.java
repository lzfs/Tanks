package pp.tanks.controller;

import pp.tanks.server.GameMode;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller displaying the lobby settings.
 */
public class LobbyController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(LobbyController.class.getName());
    private static final String LOBBY_FXML = "Lobby.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new LobbyController
     *
     * @param engine the engine this controller belongs to
     */
    public LobbyController(Engine engine) {
        super(engine);
    }

    /**
     * the button to search for a game
     */
    @FXML
    private Button searchForGame;

    /**
     * the button to create a new game
     */
    @FXML
    private Button createGame;

    /**
     * the button to get back
     */
    @FXML
    private Button back;

    /**
     * the text to display information for the user
     */
    @FXML
    private Text infoText;

    /**
     * Create the scene displaying the lobby settings.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(LOBBY_FXML, this));
    }

    /**
     * @return the name of the file as a String
     */
    public String getFileName() {
        return LOBBY_FXML;
    }

    /**
     * This method is called whenever this controller is activated,
     * i.e., when the user clicked "Multiplayer" in the main menu.
     * The default value for the infoText is an empty string.
     */
    @Override
    public void entry() {
        LOGGER.log(Level.INFO, "ENTRY LobbyController");
        if (scene == null)
            scene = makeScene();
        infoText.setText("");
        engine.setScene(scene);
    }

    /**
     * This method is called whenever this controller is deactivated, i.e., when the users chose a lobby.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT LobbyController");
    }

    /**
     * method for the back button
     */
    @FXML
    private void back() {
        LOGGER.log(Level.INFO, "clicked BACK");
        engine.activateMainMenuController();
    }

    /**
     * method for the "search for game" button
     */
    @FXML
    private void searchForGame() {
        LOGGER.log(Level.INFO, "clicked SEARCH_FOR_GAME");
        engine.activateSearchGameServerConfigController();
    }

    /**
     * method for the "create game" button
     */
    @FXML
    private void createGame() {
        LOGGER.log(Level.INFO, "clicked CREATE_GAME");
        try {
            engine.getTankApp().joinGame();
        }
        catch (IllegalArgumentException | IOException e) {
            infoText.setText("Auf ihrem System wurde kein Server gestartet.");
            return;
        }
        LOGGER.log(Level.INFO, "Client connected to MP");
        engine.activateTankConfigMPController();
    }
}
