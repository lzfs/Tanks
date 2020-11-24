package pp.tanks.controller;

import pp.tanks.message.data.TankData;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.MoveDirection;
import pp.tanks.view.TanksMapView;
import pp.util.DoubleVec;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller displaying that mission 1 is completed.
 */
public class Mission1CompleteController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(Mission1CompleteController.class.getName());
    private static final String MISSION_1_COMPLETE_SP_FXML = "Mission1Complete.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new Mission1CompleteController
     *
     * @param engine the engine this controller belongs to
     */
    public Mission1CompleteController(Engine engine) {
        super(engine);
    }

    /**
     * the button to continue
     */
    @FXML
    private Button next;

    /**
     * Create the scene shown when mission 1 is completed.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(MISSION_1_COMPLETE_SP_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated, i.e., when the first mission is completed.
     */
    @Override
    void entry() {
        LOGGER.log(Level.INFO, "ENTRY Mission1CompleteController");
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    /**
     * This method is called whenever this controller is deactivated, i.e., when the the user clicked on next.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT Mission1CompleteController");
    }

    /**
     * @return the name of the used file as a String
     */
    public String getFileName() {
        return MISSION_1_COMPLETE_SP_FXML;
    }

    /**
     * method for the next button
     */
    @FXML
    private void next() {
        LOGGER.log(Level.INFO, "clicked NEXT");

        engine.setMapCounter(2);
        loadLevelTwo();
        engine.activatePlayGameController();
    }

    private void loadLevelTwo() {
        TankData enemy1 = new TankData(new DoubleVec(20, 4), 1, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        TankData enemy2 = new TankData(new DoubleVec(20, 6), 2, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        TankData enemy3 = new TankData(new DoubleVec(20, 8), 3, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        engine.playGameController.constructionEnum.addAll(List.of(ItemEnum.ACP, ItemEnum.HOWITZER, ItemEnum.TANK_DESTROYER));
        engine.playGameController.constructionData.addAll(List.of(enemy1, enemy2, enemy3));
    }
}

