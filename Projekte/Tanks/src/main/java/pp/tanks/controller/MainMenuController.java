package pp.tanks.controller;

import pp.tanks.message.client.ClientReadyMessage;
import pp.tanks.message.client.StartGameMessage;
import pp.tanks.server.GameMode;
import pp.tanks.server.TanksServer;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller realizing the main menu of the game.
 */
public class MainMenuController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(MainMenuController.class.getName());
    private static final String MENU_CONTROL_FXML = "MainMenu.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new MainMenuController
     * in order to start correct, the methode creates a new TankServer
     *
     * @param engine the engine this controller belongs to
     */
    public MainMenuController(Engine engine) {
        super(engine);
    }

    /**
     * the button for the Singleplayer
     */
    @FXML
    private Button singlePlayer;

    /**
     * the button for the Multiplayer
     */
    @FXML
    private Button multiPlayer;

    /**
     * the button for the settings
     */
    @FXML
    private Button settings;

    /**
     * the button for the credits
     */
    @FXML
    private Button credits;

    /**
     * the button to quit the game
     */
    @FXML
    private Button quitGame;

    /**
     * Create the scene displaying the main menu.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(MENU_CONTROL_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated, i.e., when the game is started.
     */
    @Override
    public void entry() {
        LOGGER.log(Level.INFO, "ENTRY MainMenuController");
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    /**
     * This method is called whenever this controller is deactivated,
     * i.e., when the users clicked any of the buttons.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT MainMenuController");
    }

    /**
     * @return the name of the file as a String
     */
    public String getFileName() {
        return MENU_CONTROL_FXML;
    }

    /**
     * method for the settings button
     */
    @FXML
    private void settings() {
        LOGGER.log(Level.INFO, "clicked Settings");
        engine.activateSettingsController();
    }

    /**
     * method for the multiPlayer button
     */
    @FXML
    private void multiPlayer() {
        engine.setMode(GameMode.MULTIPLAYER);
        LOGGER.log(Level.INFO, "clicked MULTIPLAYER");
        engine.activateLobbyController();
    }

    /**
     * method for the singlePlayer button
     */
    @FXML
    private void singlePlayer() {
        LOGGER.log(Level.INFO, "clicked SINGLEPLAYER");
        System.out.println("Client connected to SP");
        engine.activateLevelController();
    }

    /**
     * method for the credits button
     */
    @FXML
    private void credits() {
        LOGGER.log(Level.INFO, "clicked CREDITS");
        engine.activateCreditsController();
    }

    /**
     * method for the quitGame button
     */
    @FXML
    private void quitGame() {
        LOGGER.log(Level.INFO, "clicked QUIT_GAME");
        System.exit(0);
    }
}
