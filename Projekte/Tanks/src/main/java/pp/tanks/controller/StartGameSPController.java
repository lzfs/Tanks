package pp.tanks.controller;

import pp.tanks.view.TanksMapView;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller displaying the start screen of a level in the singleplayer mode.
 */
public class StartGameSPController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(StartGameSPController.class.getName());
    private static final String START_GAME_SP_FXML = "StartGameSP.fxml"; //NON-NLS
    private Scene scene;
    private boolean flag=false;

    /**
     * create a new StartGameSPController
     *
     * @param engine the engine this controller belongs to
     */
    public StartGameSPController(Engine engine) {
        super(engine);
    }

    /**
     * the button to start the game
     */
    @FXML
    private Button startGameSP;

    /**
     * the text to display the remaining lives
     */
    @FXML
    private Text livesCounter;

    /**
     * the text to display the level information
     */
    @FXML
    private Text levelText;

    /**
     * the text to display the amount of enemy tanks
     */
    @FXML
    private Text enemyTanksText;

    /**
     * Create the scene shown before the game starts.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(START_GAME_SP_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated,
     * i.e., when the first mission is completed.
     */
    @Override
    public void entry() {
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
        if( flag) {
            livesCounter.setText(engine.getSaveTank().getLives()+"x");
        }
        flag=true;
    }

    /**
     * This method is called whenever this controller is deactivated,
     * i.e., when the the user clicked on next.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT StartGameSPController");
    }

    /**
     * @return the name of the used file as a String
     */
    public String getString() {
        return START_GAME_SP_FXML;
    }

    /**
     * method for the startGameSP button
     */
    @FXML
    private void startGameSP() {

        engine.setMode("Singleplayer");

        LOGGER.log(Level.INFO, "clicked START_GAME_SP");
        engine.activatePlayGameController();

    }
}
