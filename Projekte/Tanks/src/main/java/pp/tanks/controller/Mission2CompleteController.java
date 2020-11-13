package pp.tanks.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 * controller class for the mission complete page
 */
public class Mission2CompleteController extends Controller {

    private static final String MISSION_2_COMPLETE_SP_FXML = "Mission2Complete.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new Mission2CompleteSPController
     * @param engine the engine of the game that switches between controllers
     */
    public Mission2CompleteController(Engine engine) {
        super(engine);
    }

    /**
     * The button to get back to the main menu
     */
    @FXML
    private Button mainMenu;

    /**
     * make a new scene for JavaFX
     * @return the scene
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(MISSION_2_COMPLETE_SP_FXML, this));
    }

    @Override
    void entry() {
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    /**
     * @return the name of the used file as a String
     */
    public String getFileName() {
        return MISSION_2_COMPLETE_SP_FXML;
    }

    @FXML
    private void mainMenu() {
        System.out.println("MAIN_MENU");
        engine.activateMainMenuController();
    }
}

