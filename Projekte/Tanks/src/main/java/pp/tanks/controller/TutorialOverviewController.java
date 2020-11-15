package pp.tanks.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller displaying the game play information.
 */
public class TutorialOverviewController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(TutorialOverviewController.class.getName());
    private static final String TUTORIAL_OVERVIEW_FXML = "TutorialOverview.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new TutorialOverviewController
     *
     * @param engine the engine this controller belongs to
     */
    public TutorialOverviewController(Engine engine) {
        super(engine);
    }

    /**
     * the button to continue to the tutorial level
     */
    @FXML
    private Button next;

    /**
     * Create the scene displaying the game play information.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(TUTORIAL_OVERVIEW_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated,
     * i.e., when the user clicked on tutorial.
     */
    @Override
    void entry() {
        LOGGER.log(Level.INFO, "ENTRY TutorialOverviewController");
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    /**
     * @return the name of the used file as a String
     */
    public String getFileName() {
        return TUTORIAL_OVERVIEW_FXML;
    }

    /**
     * This method is called whenever this controller is deactivated,
     * i.e., when the user proceeds to the tutorial level
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT TutorialOverviewController");
    }

    /**
     * method for the next button
     */
    @FXML
    private void next() {
        LOGGER.log(Level.INFO, "GO TO PlayGameController");
        engine.setMode("Tutorial");
        engine.setMapCounter(0);
        engine.activatePlayGameController();
    }
}

