package pp.tanks.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller realizing the game state when the game is over (SP).
 */
public class GameOverSPController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(GameOverSPController.class.getName());
    private static final String GAME_OVER_SP_FXML = "GameOverSP.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new GameOverSPController
     *
     * @param engine the engine this controller belongs to
     */
    public GameOverSPController(Engine engine) {
        super(engine);
    }

    /**
     * the button to go back to the main menu
     */
    @FXML
    private Button mainMenuButton;

    /**
     * Create the scene shown when the game is lost in the singleplayer mode.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(GAME_OVER_SP_FXML, this));
    }

    /**
     * @return the name of the file as a String
     */
    public String getFileName() {
        return GAME_OVER_SP_FXML;
    }

    /**
     * This method is called whenever this controller is activated, i.e., when the game is over.
     */
    @Override
    public void entry() {
        LOGGER.log(Level.INFO, "ENTRY GameOverSPController");
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    /**
     * This method is called whenever this controller is deactivated, i.e., when the user returns to the main menu.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT GameOverSPController");
    }

    /**
     * method for the main menu button
     */
    @FXML
    private void mainMenu() {
        LOGGER.log(Level.INFO, "GO TO MainMenuController");
        engine.activateMainMenuController();
    }
}
