package pp.tanks.controller;

import pp.tanks.TanksImageProperty;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller displaying the pause menu.
 */
public class PauseMenuSPController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(PauseMenuSPController.class.getName());
    private static final String PAUSE_MENU_SP_FXML = "PauseMenuSP.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new PauseMenuSPController
     *
     * @param engine the engine this controller belongs to
     */
    public PauseMenuSPController(Engine engine) {
        super(engine);
    }

    /**
     * the button to continue the game
     */
    @FXML
    private Button continueGame;

    /**
     * the image to display the status icon of the music preferences
     */
    @FXML
    private ImageView musicImage;

    /**
     * the image to display the status icon of the sound preferences
     */
    @FXML
    private ImageView soundImage;

    /**
     * the button to get back to the main menu
     */
    @FXML
    private Button mainMenu;

    /**
     * Create the scene displaying the pause menu in the singleplayer mode.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(PAUSE_MENU_SP_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated,
     * i.e., when the user pressed ESC during the game.
     */
    @Override
    public void entry() {
        LOGGER.log(Level.INFO, "ENTRY PauseMenuSPController");
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    /**
     * This method is called whenever this controller is deactivated,
     * i.e., when the the user clicked on continue game or main menu.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT PauseMenuSPController");
    }

    /**
     * @return the name of the file as a String
     */
    public String getFileName() {
        return PAUSE_MENU_SP_FXML;
    }

    /**
     * method to continue the game
     */
    @FXML
    private void continueGame() {
        LOGGER.log(Level.INFO, "clicked CONTINUE_GAME");
        engine.activatePlayGameController();
    }

    /**
     * method to go back to the main menu
     */
    @FXML
    private void mainMenu() {
        LOGGER.log(Level.INFO, "clicked MAIN_MENU");
        engine.getModel().setDebug(false);
        engine.activateMainMenuController();
    }

    /**
     * method for the sound button
     */
    @FXML
    private void sound() {
        // TODO fix this when TankSoundProperty is done
        if (engine.getTankApp().sounds.getMuted()) {
            engine.getTankApp().sounds.mute(false);
        }
        else {
            engine.getTankApp().sounds.mute(true);
        }

        if (engine.getTankApp().sounds.getMuted()) {
            soundImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOff));
        }
        else {
            soundImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOn));
        }
        LOGGER.log(Level.INFO, "clicked SOUND");
    }

    /**
     * method for the music button
     */
    @FXML
    private void music() {
        engine.getTankApp().sounds.mute(!engine.getTankApp().sounds.getMuted());
        if (engine.getTankApp().sounds.getMuted()) {
            musicImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOff));
        }
        else {
            musicImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOn));
        }
        LOGGER.log(Level.INFO, "clicked MUSIC");
    }
}
