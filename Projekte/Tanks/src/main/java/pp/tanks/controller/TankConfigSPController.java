package pp.tanks.controller;

import pp.tanks.TanksImageProperty;
import pp.tanks.message.client.StartGameMessage;
import pp.tanks.message.data.TankData;
import pp.tanks.message.server.StartingSingleplayerMessage;
import pp.tanks.model.item.Armor;
import pp.tanks.model.item.HeavyArmor;
import pp.tanks.model.item.HeavyTurret;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.LightArmor;
import pp.tanks.model.item.LightTurret;
import pp.tanks.model.item.MoveDirection;
import pp.tanks.model.item.NormalArmor;
import pp.tanks.model.item.NormalTurret;
import pp.tanks.model.item.PlayersTank;
import pp.tanks.model.item.Turret;
import pp.tanks.server.GameMode;
import pp.util.DoubleVec;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller displaying the tank configuration in the singleplayer mode.
 */
public class TankConfigSPController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(TankConfigSPController.class.getName());
    private static final String TANK_CONFIG_SP_FXML = "TankConfigSP.fxml"; //NON-NLS

    private Scene scene;
    private int counterTurret = 0;
    private int counterArmor = 0;

    private final List<Image> turrets = new ArrayList<>();
    private final List<Image> armors = new ArrayList<>();
    private final List<Image> charts = new ArrayList<>();

    private final List<Integer> magazine = new ArrayList<>(Arrays.asList(5, 3, 1));
    private final List<Integer> cadence = new ArrayList<>(Arrays.asList(1, 3, 5));

    private final List<Armor> armorList = new ArrayList<>(Arrays.asList(new LightArmor(), new NormalArmor(), new HeavyArmor()));

    private final List<Turret> turretsList = new ArrayList<>(Arrays.asList(new LightTurret(), new NormalTurret(), new HeavyTurret()));

    /**
     * create a new TankConfigSPController
     *
     * @param engine the engine this controller belongs to
     */
    public TankConfigSPController(Engine engine) {
        super(engine);
    }

    /**
     * the button to go back
     */
    @FXML
    private Button back;

    /**
     * the button to confirm
     */
    @FXML
    private Button confirm;

    /**
     * the button to change the turret (left)
     */
    @FXML
    private Button turretButtonLeft;

    /**
     * the button to change the turret (right)
     */
    @FXML
    private Button turretButtonRight;

    /**
     * the button to change the armor (left)
     */
    @FXML
    private Button armorButtonLeft;

    /**
     * the button to change the armor (right)
     */
    @FXML
    private Button armorButtonRight;

    /**
     * the text to display the magazine size
     */
    @FXML
    private Text magazineSizeText;

    /**
     * the text to display the cadence
     */
    @FXML
    private Text cadenceText;

    /**
     * the image to display the chosen turret
     */
    @FXML
    private ImageView image1;

    /**
     * the image to display the chosen armor
     */
    @FXML
    private ImageView image2;

    /**
     * the image to display the harm of the configuration in a chart
     */
    @FXML
    private ImageView harmChart;

    /**
     * the image to display the armor strength of the configuration in a chart
     */
    @FXML
    private ImageView armorChart;

    /**
     * the image to display the speed of the configuration in a chart
     */
    @FXML
    private ImageView speedChart;

    /**
     * Create the scene shown to configure the player tank.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(TANK_CONFIG_SP_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated,
     * i.e., when the the player chose a level in the Level-Selection-Menu.
     */
    @Override
    public void entry() {
        LOGGER.log(Level.INFO, "ENTRY TankConfigSPController");
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);

        turrets.add(engine.getImages().getImage(TanksImageProperty.turret1));
        turrets.add(engine.getImages().getImage(TanksImageProperty.turret2));
        turrets.add(engine.getImages().getImage(TanksImageProperty.turret3));

        armors.add(engine.getImages().getImage(TanksImageProperty.armor1));
        armors.add(engine.getImages().getImage(TanksImageProperty.armor2));
        armors.add(engine.getImages().getImage(TanksImageProperty.armor3));

        charts.add(engine.getImages().getImage(TanksImageProperty.chart1));
        charts.add(engine.getImages().getImage(TanksImageProperty.chart2));
        charts.add(engine.getImages().getImage(TanksImageProperty.chart3));
    }

    /**
     * This method is called whenever this controller is deactivated,
     * i.e., when the the user clicked on confirm.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT TankConfigSPController");
    }

    /**
     * @return the name of the file as a String
     */
    public String getFileName() {
        return TANK_CONFIG_SP_FXML;
    }

    /**
     * method for the back button
     */
    @FXML
    private void back() {
        LOGGER.log(Level.INFO, "clicked BACK");
        engine.activateLevelController();
    }

    /**
     * method for the confirm button
     */
    @FXML
    private void confirm() {
        LOGGER.log(Level.INFO, "clicked CONFIRM");

        DoubleVec position = new DoubleVec(5, 5);
        PlayersTank tank = new PlayersTank(engine.getModel(), 3, armorList.get(counterArmor), turretsList.get(counterTurret), new TankData(position, 0, 20, MoveDirection.STAY, 0.0, new DoubleVec(0, 0), false)); //TODO id
        engine.getTankApp().getConnection().send(new StartGameMessage(getCountTurret(counterTurret), getCountArmor(counterArmor), GameMode.SINGLEPLAYER, engine.getPlayerEnum()));
        engine.setSaveTank(tank);
        engine.setMapCounter(1);
        counterArmor = 0;
        counterTurret = 0;
        confirm.setDisable(true);
    }

    /**
     * starts a new game from incoming message
     *
     * @param msg incoming StartingSingleplayerMessage
     */
    public void startGame(StartingSingleplayerMessage msg) {
        Platform.runLater(() -> {
            engine.playGameController.constructionData.addAll(msg.dataList);
            engine.playGameController.constructionEnum.addAll(msg.comType);
            engine.activateStartGameSPController();
        });
    }

    /**
     * method for the turretButtonRight button
     */
    @FXML
    private void turretButtonRight() {
        counterTurret += 1;

        if (counterTurret >= turrets.size()) {
            counterTurret = 0;
        }

        harmChart.setImage(charts.get(counterTurret));
        image1.setImage(turrets.get(counterTurret));
        magazineSizeText.setText(magazine.get(counterTurret).toString());
        cadenceText.setText(cadence.get(counterTurret).toString() + "s");
    }

    /**
     * method for the turretButtonLeft button
     */
    @FXML
    private void turretButtonLeft() {
        counterTurret -= 1;

        if (counterTurret < 0) {
            counterTurret = turrets.size() - 1;
        }
        image1.setImage(turrets.get(counterTurret));
        magazineSizeText.setText(magazine.get(counterTurret).toString());
        cadenceText.setText(cadence.get(counterTurret).toString() + "s");
    }

    /**
     * method for the armorButtonRight button
     */
    @FXML
    private void armorButtonRight() {
        counterArmor += 1;

        if (counterArmor >= armors.size()) {
            counterArmor = 0;
        }
        image2.setImage(armors.get(counterArmor));

        changeCharts();
    }

    /**
     * method for the armorButtonLeft button
     */
    @FXML
    private void armorButtonLeft() {
        counterArmor -= 1;

        if (counterArmor < 0) {
            counterArmor = armors.size() - 1;
        }

        image2.setImage(armors.get(counterArmor));

        changeCharts();
    }

    /**
     * change the displayed charts
     * used in the methods for the armor buttons
     */
    private void changeCharts() {
        if (counterArmor == 0) {
            armorChart.setImage(charts.get(0));
            speedChart.setImage(charts.get(2));
        }
        else if (counterArmor == 1) {
            armorChart.setImage(charts.get(1));
            speedChart.setImage(charts.get(1));
        }
        else {
            armorChart.setImage(charts.get(2));
            speedChart.setImage(charts.get(0));
        }
    }

    /**
     * @param c id of turret
     * @return correct turret fitting to the id
     */
    private ItemEnum getCountTurret(int c) {
        if (c == 0) return ItemEnum.LIGHT_TURRET;
        else if (c == 1) return ItemEnum.NORMAL_TURRET;
        else return ItemEnum.HEAVY_TURRET;
    }

    /**
     * @param c id of armor
     * @return correct armor fitting to the id
     */
    private ItemEnum getCountArmor(int c) {
        if (c == 0) return ItemEnum.LIGHT_ARMOR;
        else if (c == 1) return ItemEnum.NORMAL_ARMOR;
        else return ItemEnum.HEAVY_ARMOR;
    }
}
