package pp.tanks.controller;

import pp.tanks.TanksImageProperty;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller displaying the settings of the game.
 */
public class SettingsController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(SettingsController.class.getName());
    private static final String SETTINGS_FXML = "Settings.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new SettingsController
     * @param engine the engine this controller belongs to
     */
    public SettingsController(Engine engine) {
        super(engine);
    }

    /**
     * the button to get back
     */
    @FXML
    private Button back;

    /**
     * the button to turn the sound on/off
     */
    @FXML
    private Button sound;

    /**
     * the button to turn the music on/off
     */
    @FXML
    private Button music;

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
     * Create the scene displaying the settings of the game.
     */
    public Scene makeScene()  {
        return new Scene(engine.getViewForController(SETTINGS_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated,
     * i.e., when the user clicked on settings in the main menu.
     */
    @Override
    public void entry(){
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    /**
     * This method is called whenever this controller is deactivated,
     * i.e., when the the user returned to the main menu.
     */
    @Override
    public void exit(){
        LOGGER.log(Level.INFO, "EXIT SettingsController");
    }

    /**
     * @return the name of the used file as a String
     */
    public String getString(){
        return SETTINGS_FXML;
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
     * method for the sound button
     */
    @FXML
    private void sound() {
        // TODO fix this when TankSoundProperty is done
        if(engine.getTankApp().sounds.getMuted()) {
            engine.getTankApp().sounds.mute(false);
        } else {
            engine.getTankApp().sounds.mute(true);
        }

        if(engine.getTankApp().sounds.getMuted()){
            soundImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOff));
        } else {
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
        if(engine.getTankApp().sounds.getMuted()){
            musicImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOff));
        } else {
            musicImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOn));
        }
        LOGGER.log(Level.INFO, "clicked MUSIC");
    }
}
