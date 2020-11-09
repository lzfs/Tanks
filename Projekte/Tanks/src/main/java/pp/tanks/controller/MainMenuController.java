package pp.tanks.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class MainMenuController extends Controller {

    private static final String MENU_CONTROL_FXML = "MainMenu.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new MainMenuController
     * @param engine the engine of the game that switches between controllers
     */
    public MainMenuController(Engine engine) {
        super(engine);
    }

    /**
     * the button for the singlePlayer
     */
    @FXML
    private Button singlePlayer;

    /**
     * the button for the multiPlayer
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
     * the button to leave the game
     */
    @FXML
    private Button quitGame;

    public Scene makeScene() {
        return new Scene(engine.getViewForController(MENU_CONTROL_FXML, this));
    }

    @Override
    public void entry() {
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    @Override
    public void exit() {
        System.out.println("EXIT");
    }

    public String getFileName() {
        return MENU_CONTROL_FXML;
    }

    /**
     * method for the settings button
     */
    @FXML
    private void settings() {
        System.out.println("SETTINGS");
        engine.activateSettingsController();
    }

    /**
     * method for the multiPlayer button
     */
    @FXML
    private void multiPlayer() {
        System.out.println("MULTIPLAYER");
        engine.activateLobbyController();
    }

    /**
     * method for the singlePlayer button
     */
    @FXML
    private void singlePlayer() {
        System.out.println("SINGLEPLAYER");
        engine.activateLevelController();
    }

    /**
     * method for the credits button
     */
    @FXML
    private void credits() {
        System.out.println("CREDITS");
        engine.activateCreditsController();
    }

    /**
     * method for the quitGame button
     */
    @FXML
    private void quitGame() {
        System.out.println("QUIT_GAME");
        System.exit(0);
    }
}
