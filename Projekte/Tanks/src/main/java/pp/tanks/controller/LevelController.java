package pp.tanks.controller;

import pp.tanks.message.data.TankData;
import pp.tanks.model.item.LightArmor;
import pp.tanks.model.item.LightTurret;
import pp.tanks.model.item.PlayersTank;
import pp.tanks.model.item.Tank;
import pp.tanks.view.TanksMapView;
import pp.util.DoubleVec;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class LevelController extends Controller {

    private static final String MENU_CONTROL_FXML = "LevelSP.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new LevelController
     *
     * @param engine the engine of the game that switches between controllers
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

    /**
     * @return the name of the file as a String
     */
    public String getFileName() {
        return MENU_CONTROL_FXML;
    }

    /**
     * method for the back button
     */
    @FXML
    private void back() {
        System.out.println("BACK");
        engine.activateMainMenuController();
    }

    /**
     * method for the tutorial button
     */
    @FXML
    private void tutorial() throws IOException, XMLStreamException {

        engine.setMode("Tutorial");

        engine.getModel().loadMap("tutorialMap.xml");
        PlayersTank tank = new PlayersTank(engine.getModel(), 1, new LightArmor(), new LightTurret(), new TankData(new DoubleVec(5, 5), 1000, 20));
        engine.getModel().setTank(tank);
        TanksMapView mapview = new TanksMapView(engine.getModel(), engine.getImages());
        engine.setView(mapview);

        engine.activatePlayGameController();

        System.out.println("TUTORIAL");
    }

    /**
     * method for the startGame button
     */
    @FXML
    private void startGame() {
        System.out.println("START_GAME");
        engine.activateTankConfigSPController();
    }
}
