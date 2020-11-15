package pp.tanks.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller realizing the game state game is won (MP).
 */
public class GameWonMPController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(GameWonMPController.class.getName());
    private static final String GAME_WON_MP_FXML = "GameWonMP.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new GameWonMPController
     *
     * @param engine the engine this controller belongs to
     */
    public GameWonMPController(Engine engine) {
        super(engine);
    }

    /**
     * the button to go back to the lobby
     */
    @FXML
    private Button lobbyButton;

    /**
     * Create the scene shown when the game is won in multiplayer mode.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(GAME_WON_MP_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated, i.e., when the is won.
     */
    @Override
    public void entry() {
        LOGGER.log(Level.INFO, "ENTRY GameWonMPController");
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    /**
     * This method is called whenever this controller is deactivated, i.e., when the user returns to the lobby.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT GameWonMPController");
    }

    /**
     * @return the name of the file as a String
     */
    public String getFileName() {
        return GAME_WON_MP_FXML;
    }

    /**
     * method for the lobby button
     */
    @FXML
    private void lobby() {
        LOGGER.log(Level.INFO, "GO TO LobbyController");
        engine.activateLobbyController();
    }
}
