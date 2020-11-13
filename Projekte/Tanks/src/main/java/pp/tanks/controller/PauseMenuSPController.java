package pp.tanks.controller;

import pp.tanks.TanksImageProperty;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class PauseMenuSPController extends Controller {

    private static final String PAUSE_MENU_SP_FXML = "PauseMenuSP.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new PauseMenuSPController
     *
     * @param engine the engine of the game that switches between controllers
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

    public Scene makeScene() {
        return new Scene(engine.getViewForController(PAUSE_MENU_SP_FXML, this));
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
        System.out.println("CONTINUE_GAME");
        engine.activatePlayGameController();
    }

    /**
     * method to go back to the main menu
     */
    @FXML
    private void mainMenu() {
        System.out.println("MAIN MENU");
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
