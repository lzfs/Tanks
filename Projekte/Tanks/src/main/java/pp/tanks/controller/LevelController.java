package pp.tanks.controller;

import pp.tanks.message.client.BackMessage;
import pp.tanks.message.data.TankData;
import pp.tanks.model.item.LightArmor;
import pp.tanks.model.item.LightTurret;
import pp.tanks.model.item.PlayersTank;
import pp.tanks.server.GameMode;
import pp.tanks.view.TanksMapView;
import pp.util.DoubleVec;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller displaying the levels the user can choose from.
 */
public class LevelController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(LevelController.class.getName());
    private static final String LEVEL_SP_FXML = "LevelSP.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new LevelController
     *
     * @param engine the engine this controller belongs to
     */
    public LevelController(Engine engine) {
        super(engine);
    }

    /**
     * the button to start the tutorial
     */
    @FXML
    private Button tutorial;

    /**
     * the button to start the game
     */
    @FXML
    private Button startGame;

    /**
     * the button to get back
     */
    @FXML
    private Button back;

    /**
     * Create the scene displaying all levels.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(LEVEL_SP_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated, i.e., when the user clicked "Singleplayer" in the main menu.
     */
    @Override
    public void entry() {
        LOGGER.log(Level.INFO, "ENTRY LevelController");
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    /**
     * This method is called whenever this controller is deactivated, i.e., when the user chose a level.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT LevelController");
    }

    /**
     * @return the name of the file as a String
     */
    public String getFileName() {
        return LEVEL_SP_FXML;
    }

    /**
     * method for the back button
     */
    @FXML
    private void back() {
        LOGGER.log(Level.INFO, "clicked BACK");
        engine.getTankApp().getConnection().send(new BackMessage());
        engine.getModel().setDebug(false);
        engine.activateMainMenuController();
    }

    /**
     * method for the tutorial button
     */
    @FXML
    private void tutorial() {
        LOGGER.log(Level.INFO, "clicked TUTORIAL");
        engine.activateTutorialOverviewController();
    }

    /**
     * method for the startGame button
     */
    @FXML
    private void startGame() {
        LOGGER.log(Level.INFO, "clicked START_GAME");
        engine.activateTankConfigSPController();
    }
}
