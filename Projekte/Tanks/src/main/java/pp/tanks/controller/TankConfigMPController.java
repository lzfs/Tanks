package pp.tanks.controller;

import pp.tanks.TanksImageProperty;
import pp.tanks.message.client.StartGameMessage;
import pp.tanks.message.data.TankData;
import pp.tanks.model.item.Armor;
import pp.tanks.model.item.HeavyArmor;
import pp.tanks.model.item.HeavyTurret;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.LightArmor;
import pp.tanks.model.item.LightTurret;
import pp.tanks.model.item.NormalArmor;
import pp.tanks.model.item.NormalTurret;
import pp.tanks.model.item.PlayersTank;
import pp.tanks.model.item.Turret;
import pp.tanks.server.GameMode;
import pp.util.DoubleVec;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller displaying the tank configuration in the multiplayer mode.
 */
public class TankConfigMPController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(TankConfigMPController.class.getName());
    private static final String TANK_CONFIG_MP_FXML = "TankConfigMP.fxml"; //NON-NLS

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
     * create a new TankConfigMPController
     *
     * @param engine the engine this controller belongs to
     */
    public TankConfigMPController(Engine engine) {
        super(engine);
    }

    /**
     * the button to go back
     */
    @FXML
    private Button back;

    /**
     * the button to confirm the configuration
     */
    @FXML
    private Button ready;

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
     * the image to display the chosen turret
     */
    @FXML
    private ImageView ownTurretImage;

    /**
     * the image to display the chosen armor
     */
    @FXML
    private ImageView ownArmorImage;

    /**
     * the text to display the magazine size (Player 1)
     */
    @FXML
    private Text magazineSizeTextPlayer1;

    /**
     * the text to display the cadence (Player 1)
     */
    @FXML
    private Text cadenceTextPlayer1;

    /**
     * the text to display if the second player is ready or not
     */
    @FXML
    private Text player2ReadyText;

    /**
     * the image to display the harm of the configuration in a chart (Player 1)
     */
    @FXML
    private ImageView harmChartPlayer1;

    /**
     * the image to display the armor strength of the configuration in a chart (Player 1)
     */
    @FXML
    private ImageView armorChartPlayer1;

    /**
     * the image to display the speed of the configuration in a chart (Player 1)
     */
    @FXML
    private ImageView speedChartPlayer1;

    /**
     * the text to display the magazine size (Player 2)
     */
    @FXML
    private Text magazineSizeTextPlayer2;

    /**
     * the text to display the cadence (Player 2)
     */
    @FXML
    private Text cadenceTextPlayer2;

    /**
     * the image to display the harm of the configuration in a chart (Player 2)
     */
    @FXML
    private ImageView harmChartPlayer2;

    /**
     * the image to display the armor strength of the configuration in a chart (Player 2)
     */
    @FXML
    private ImageView armorChartPlayer2;

    /**
     * the image to display the speed of the configuration in a chart (Player 2)
     */
    @FXML
    private ImageView speedChartPlayer2;

    /**
     * Create the scene shown to configure the player tank.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(TANK_CONFIG_MP_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated,
     * i.e., when the the player chose a level in the Level-Selection-Menu.
     */
    @Override
    public void entry() {
        LOGGER.log(Level.INFO, "ENTRY TankConfigMPController");
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
     * i.e., when player 1 and 2 both clicked on confirm.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT TankConfigMPController");
    }

    /**
     * @return the name of the file as a String
     */
    public String getFileName() {
        return TANK_CONFIG_MP_FXML;
    }

    /**
     * method for the back button
     */
    @FXML
    private void back() {
        LOGGER.log(Level.INFO, "clicked BACK");
        engine.activateLobbyController();
    }

    /**
     * method for the ready button
     */
    @FXML
    private void ready() {
        LOGGER.log(Level.INFO, "clicked READY");

        DoubleVec position = new DoubleVec(5, 5);
        PlayersTank tank = new PlayersTank(engine.getModel(), 1, armorList.get(counterArmor), turretsList.get(counterTurret), new TankData(position, 1000, 20)); //TODO id
        engine.getTankApp().getConnection().send(new StartGameMessage(getCountTurret(counterTurret), getCountArmor(counterArmor), GameMode.SINGLEPLAYER, engine.getPlayerEnum()));
        engine.setSaveTank(tank);
        engine.setMapCounter(1);
        counterArmor = 0;
        counterTurret = 0;
        engine.activateStartGameSPController(); // TODO wrong controller
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

        harmChartPlayer1.setImage(charts.get(counterTurret));
        ownTurretImage.setImage(turrets.get(counterTurret));
        magazineSizeTextPlayer1.setText(magazine.get(counterTurret).toString());
        cadenceTextPlayer1.setText(cadence.get(counterTurret).toString() + "s");
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
        ownTurretImage.setImage(turrets.get(counterTurret));
        magazineSizeTextPlayer1.setText(magazine.get(counterTurret).toString());
        cadenceTextPlayer1.setText(cadence.get(counterTurret).toString() + "s");
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
        ownArmorImage.setImage(armors.get(counterArmor));

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

        ownArmorImage.setImage(armors.get(counterArmor));

        changeCharts();
    }

    /**
     * change the displayed charts
     * used in the methods for the armor buttons
     */
    private void changeCharts() {
        if (counterArmor == 0) {
            armorChartPlayer1.setImage(charts.get(0));
            speedChartPlayer1.setImage(charts.get(2));
        }
        else if (counterArmor == 1) {
            armorChartPlayer1.setImage(charts.get(1));
            speedChartPlayer1.setImage(charts.get(1));
        }
        else {
            armorChartPlayer1.setImage(charts.get(2));
            speedChartPlayer1.setImage(charts.get(0));
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
