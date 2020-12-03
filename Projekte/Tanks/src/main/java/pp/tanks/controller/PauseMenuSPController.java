package pp.tanks.controller;

import pp.tanks.TanksImageProperty;
import pp.tanks.model.item.COMEnemy;
import pp.tanks.model.item.Projectile;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.io.FileOutputStream;
import java.io.IOException;
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
        engine.viewUpdate=false;
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
        changeMusic(engine.getTankApp().sounds.getMutedMusic());
        changeSound(engine.isSoundMuted());
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
        engine.viewUpdate=true;
        LOGGER.log(Level.INFO, "clicked CONTINUE_GAME");
        //engine.setScene(engine.playGameController.sceneBackup);
        for( Projectile p :  engine.getModel().getTanksMap().getProjectiles()){
            p.resetInterpolateTime();
        }
        for(COMEnemy  comEnemy : engine.getModel().getTanksMap().getCOMTanks()){
            comEnemy.resetInterpolateTime();
        }
        engine.resumeGame();
    }

    /**
     * method to go back to the main menu
     */
    @FXML
    private void mainMenu() {
        LOGGER.log(Level.INFO, "clicked MAIN_MENU");
        engine.setView(null);
        engine.activateMainMenuController();
        engine.viewUpdate=true;

    }

    /**
     * method for the sound button
     */
    @FXML
    private void soundClicked() {
        engine.setSoundMuted(!engine.isSoundMuted());
        changeSound(engine.isSoundMuted());
    }

    public void changeSound(boolean mute) {
        if (mute) {
            soundImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOff));
            engine.getTankApp().properties.setProperty("soundMuted", "1");
            try {
                engine.getTankApp().properties.store(new FileOutputStream("tanks.properties"), null);
            }
            catch (IOException e) {
                LOGGER.log(Level.INFO, e.getMessage());
            }
        }
        else {
            soundImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOn));
            engine.getTankApp().properties.setProperty("soundMuted", "0");
            try {
                engine.getTankApp().properties.store(new FileOutputStream("tanks.properties"), null);
            }
            catch (IOException e) {
                LOGGER.log(Level.INFO, e.getMessage());
            }
        }
        LOGGER.log(Level.INFO, "clicked SOUND");
    }

    /**
     * method for the music button
     */
    @FXML
    private void music() {
        engine.getTankApp().sounds.mute(!engine.getTankApp().sounds.getMutedMusic());
        changeMusic(engine.getTankApp().sounds.getMutedMusic());
    }

    public void changeMusic(boolean mute) {
        if (mute) {
            musicImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOff));
            engine.getTankApp().properties.setProperty("musicMuted", "1");
            try {
                engine.getTankApp().properties.store(new FileOutputStream("tanks.properties"), null);
            }
            catch (IOException e) {
                LOGGER.log(Level.INFO, e.getMessage());
            }
        }
        else {
            musicImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOn));
            engine.getTankApp().properties.setProperty("musicMuted", "0");
            try {
                engine.getTankApp().properties.store(new FileOutputStream("tanks.properties"), null);
            }
            catch (IOException e) {
                LOGGER.log(Level.INFO, e.getMessage());
            }
        }
        LOGGER.log(Level.INFO, "clicked MUSIC");
    }
}
