package pp.tanks.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class StartGameSPController extends Controller {

    private static final String MENU_CONTROL_FXML = "StartGameSP.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new StartGameSPController
     * @param engine the engine of the game that switches between controllers
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

    public String getString() {
        return MENU_CONTROL_FXML;
    }

    /**
     * method for the startGameSP button
     */
    @FXML
    private void startGameSP() {
        System.out.println("START GAME SP");
    }
}
