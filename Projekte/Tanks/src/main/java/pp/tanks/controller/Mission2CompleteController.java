package pp.tanks.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller displaying that mission 2 is completed.
 */
public class Mission2CompleteController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(Mission2CompleteController.class.getName());
    private static final String MISSION_2_COMPLETE_SP_FXML = "Mission2Complete.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new Mission2CompleteController
     *
     * @param engine the engine this controller belongs to
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
     * Create the scene shown when mission 2 is completed.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(MISSION_2_COMPLETE_SP_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated, i.e., when the second mission is completed.
     */
    @Override
    void entry() {
        LOGGER.log(Level.INFO, "ENTRY Mission2CompleteController");
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    /**
     * This method is called whenever this controller is deactivated, i.e., when the the user clicked on next.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT Mission2CompleteController");
    }

    /**
     * @return the name of the used file as a String
     */
    public String getFileName() {
        return MISSION_2_COMPLETE_SP_FXML;
    }

    /**
     * method for the "main menu" button
     */
    @FXML
    private void mainMenu() {
        LOGGER.log(Level.INFO, "clicked MAIN_MENU");
        engine.activateMainMenuController();
    }
}

