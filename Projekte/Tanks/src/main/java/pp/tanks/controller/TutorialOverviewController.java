package pp.tanks.controller;

import pp.tanks.message.client.StartGameMessage;
import pp.tanks.message.data.TankData;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.LightArmor;
import pp.tanks.model.item.LightTurret;
import pp.tanks.model.item.MoveDirection;
import pp.tanks.model.item.PlayersTank;
import pp.tanks.server.GameMode;
import pp.util.DoubleVec;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.List;
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
     * called when level one gets loaded
     */
    private void loadLevelTutorial() {
        TankData enemy1 = new TankData(new DoubleVec(18, 7), 1, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        engine.playGameController.constructionEnum.addAll(List.of(ItemEnum.ACP));
        engine.playGameController.constructionData.addAll(List.of(enemy1));
    }

    /**
     * method for the next button
     */
    @FXML
    private void next() {
        LOGGER.log(Level.INFO, "GO TO PlayGameController");
        engine.setMode(GameMode.TUTORIAL);
        engine.setMapCounter(0);
        loadLevelTutorial();
        PlayersTank tank = new PlayersTank(engine.getModel(), 3, new LightArmor(), new LightTurret(), new TankData(new DoubleVec(3, 6), 0, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false));
        engine.setSaveTank(tank);
        engine.activatePlayGameController();
    }
}

