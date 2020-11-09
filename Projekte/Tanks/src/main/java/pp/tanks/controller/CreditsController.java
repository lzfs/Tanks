package pp.tanks.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 * controller class for the credits page
 */
public class CreditsController extends Controller {

    private static final String MENU_CONTROL_FXML = "Credits.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new CreditsController
     * @param engine the engine of the game that switches between controllers
     */
    public CreditsController(Engine engine) {
        super(engine);
    }

    /**
     * The button for getting back
     */
    @FXML
    private Button back;

    /**
     * make a new scene for JavaFX
     * @return the scene
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(MENU_CONTROL_FXML, this));
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
        return MENU_CONTROL_FXML;
    }

    @FXML
    private void back() {
        System.out.println("BACK");
        engine.activateMainMenuController();
    }
}

