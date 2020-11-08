package pp.tanks.controller;

import pp.tanks.TanksImageProperty;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SettingsController extends Controller {

    private static final String MENU_CONTROL_FXML = "Settings.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new SettingsController
     * @param engine the engine of the game that switches between controllers
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


    public Scene makeScene()  {
        return new Scene(engine.getViewForController(MENU_CONTROL_FXML, this));
    }

    @Override
    public void entry(){
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    @Override
    public void exit(){
        System.out.println("EXIT");
    }

    public String getString(){
        return MENU_CONTROL_FXML;
    }

    /**
     * method for the back button
     */
    @FXML
    private void back() {
        System.out.println("BACK");
        engine.activateMainMenuController();
    }

    /**
     * method for the sound button
     */
    @FXML
    private void sound() {
        /*
        TODO fix this when TankSoundProperty is done
        engine.getTankApp().sounds.mute(!engine.getTankApp().sounds.getMuted());
        if(engine.getTankApp().sounds.getMuted()){
            soundImage.setImage(engine.getImages().getImage(TankImageProperty.soundOff));
        } else {
            soundImage.setImage(engine.getImages().getImage(TankImageProperty.soundOn));
        }
         */
        System.out.println("SOUND");
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
        System.out.println("MUSIC");
    }
}
