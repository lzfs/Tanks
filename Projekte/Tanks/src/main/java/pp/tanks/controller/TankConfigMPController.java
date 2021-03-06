package pp.tanks.controller;

import pp.tanks.TanksImageProperty;
import pp.tanks.message.client.BackMessage;
import pp.tanks.message.client.StartGameMessage;
import pp.tanks.message.client.UpdateTankConfigMessage;
import pp.tanks.message.server.ServerTankUpdateMessage;
import pp.tanks.message.server.StartingMultiplayerMessage;
import pp.tanks.model.item.*;
import pp.tanks.server.GameMode;

import javafx.application.Platform;
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
    private int ownTurretCounter = 0;
    private int ownArmorCounter = 0;
    private int opponentTurretCounter = 0;
    private int opponentArmorCounter = 0;

    private final List<Image> turrets = new ArrayList<>();
    private final List<Image> armors = new ArrayList<>();
    private final List<Image> charts = new ArrayList<>();
    private final List<Image> minCharts = new ArrayList<>();

    private final List<Integer> magazine = new ArrayList<>(Arrays.asList(5, 3, 1));
    private final List<Integer> cadence = new ArrayList<>(Arrays.asList(1, 3, 5));

    private List<ItemEnum> turretList = new ArrayList(Arrays.asList(ItemEnum.LIGHT_TURRET, ItemEnum.NORMAL_TURRET, ItemEnum.HEAVY_TURRET));
    private List<ItemEnum> armorList = new ArrayList(Arrays.asList(ItemEnum.LIGHT_ARMOR, ItemEnum.NORMAL_ARMOR, ItemEnum.HEAVY_ARMOR));

    private final List<Integer> armorWeightList = new ArrayList<>(Arrays.asList(5, 10, 20));
    private final List<Integer> turretWeightList = new ArrayList<>(Arrays.asList(5, 10, 15));

    private ItemEnum currentTurret = ItemEnum.LIGHT_TURRET;
    private ItemEnum currentArmor = ItemEnum.LIGHT_ARMOR;

    private boolean playerConnected = false;

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
    private Button readyButton;

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
     * the image to display the turret of the opponent
     */
    @FXML
    private ImageView turretPlayer2;

    /**
     * the image to display the armor of the opponent
     */
    @FXML
    private ImageView armorPlayer2;

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
     * @return the name of the file as a String
     */
    public String getFileName() {
        return TANK_CONFIG_MP_FXML;
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
        readyButton.setDisable(!playerConnected);
        player2ReadyText.setText("");
        if (!playerConnected) player2ReadyText.setText("Warte auf einen zweiten Spieler");
        turretButtonLeft.setDisable(false);
        turretButtonRight.setDisable(false);
        armorButtonLeft.setDisable(false);
        armorButtonRight.setDisable(false);

        turrets.add(engine.getImages().getImage(TanksImageProperty.turret1));
        turrets.add(engine.getImages().getImage(TanksImageProperty.turret2));
        turrets.add(engine.getImages().getImage(TanksImageProperty.turret3));

        armors.add(engine.getImages().getImage(TanksImageProperty.armor1Small));
        armors.add(engine.getImages().getImage(TanksImageProperty.armor2Small));
        armors.add(engine.getImages().getImage(TanksImageProperty.armor3));

        charts.add(engine.getImages().getImage(TanksImageProperty.chart1));
        charts.add(engine.getImages().getImage(TanksImageProperty.chart2));
        charts.add(engine.getImages().getImage(TanksImageProperty.chart3));
        charts.add(engine.getImages().getImage(TanksImageProperty.chart4));
        charts.add(engine.getImages().getImage(TanksImageProperty.chart5));
        charts.add(engine.getImages().getImage(TanksImageProperty.chart6));

        minCharts.add(engine.getImages().getImage(TanksImageProperty.chart1));
        minCharts.add(engine.getImages().getImage(TanksImageProperty.chart3));
        minCharts.add(engine.getImages().getImage(TanksImageProperty.chart6));
       /* int chartIdx = (armorWeightList.get(ownArmorCounter) + turretWeightList.get(ownTurretCounter)) / 5 -2;
        speedChartPlayer1.setImage(charts.get((chartIdx-5)*(-1)));
        engine.getTankApp().getConnection().send(new UpdateTankConfigMessage(currentTurret, currentArmor, engine.getPlayerEnum()));
        */
        changeOwnCharts();
        changeOpponentCharts();
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
     * method for the back button
     */
    @FXML
    private void back() {
        LOGGER.log(Level.INFO, "clicked BACK");
        engine.activateLobbyController();
        playerConnected = false;
        engine.getTankApp().getConnection().send(new BackMessage());
    }

    /**
     * method for the ready button
     */
    @FXML
    private void ready() {
        LOGGER.log(Level.INFO, "clicked READY");

        engine.getTankApp().getConnection().send(new StartGameMessage(currentTurret, currentArmor, GameMode.MULTIPLAYER, engine.getPlayerEnum()));
        readyButton.setDisable(true);
        player2ReadyText.setText("Warten auf Spieler 2");
        turretButtonLeft.setDisable(true);
        turretButtonRight.setDisable(true);
        armorButtonLeft.setDisable(true);
        armorButtonRight.setDisable(true);
    }

    /**
     * method for the turretButtonRight button
     */
    @FXML
    private void turretButtonRight() {
        ownTurretCounter += 1;
        ownTurretCounter = ownTurretCounter % 3;

       changeOwnCharts();
    }

    /**
     * method for the turretButtonLeft button
     */
    @FXML
    private void turretButtonLeft() {
        ownTurretCounter -= 1;

        if (ownTurretCounter < 0) {
            ownTurretCounter = turretList.size() - 1;
        }

        changeOwnCharts();
    }

    /**
     * change the displayed charts
     */
    private void changeOwnCharts() {
        harmChartPlayer1.setImage(minCharts.get(ownTurretCounter));
        ownTurretImage.setImage(turrets.get(ownTurretCounter));
        currentTurret = turretList.get(ownTurretCounter);
        magazineSizeTextPlayer1.setText(magazine.get(ownTurretCounter).toString());
        cadenceTextPlayer1.setText(cadence.get(ownTurretCounter).toString() + "s");
        ownArmorImage.setImage(armors.get(ownArmorCounter));
        currentArmor = armorList.get(ownArmorCounter);
        armorChartPlayer1.setImage(minCharts.get(ownArmorCounter));

        int chartIdx = (armorWeightList.get(ownArmorCounter) + turretWeightList.get(ownTurretCounter)) / 5 -2;
        speedChartPlayer1.setImage(charts.get((chartIdx-5)*(-1)));
        engine.getTankApp().getConnection().send(new UpdateTankConfigMessage(currentTurret, currentArmor, engine.getPlayerEnum()));
    }

    /**
     * method for the armorButtonRight button
     */
    @FXML
    private void armorButtonRight() {
        ownArmorCounter += 1;
        ownArmorCounter = ownArmorCounter % 3;
        changeOwnCharts();
    }

    /**
     * method for the armorButtonLeft button
     */
    @FXML
    private void armorButtonLeft() {
        ownArmorCounter -= 1;
        if (ownArmorCounter < 0) {
            ownArmorCounter = armorList.size() - 1;
        }
        changeOwnCharts();
    }

    /**
     * change the displayed charts of the opponent
     * used in the methods for the armor buttons
     */
    private void changeOpponentCharts() {
        /*if (opponentArmorCounter == 0) {
            armorChartPlayer2.setImage(charts.get(0));
            speedChartPlayer2.setImage(charts.get(2));
        }
        else if (opponentArmorCounter == 1) {
            armorChartPlayer2.setImage(charts.get(1));
            speedChartPlayer2.setImage(charts.get(1));
        }
        else {
            armorChartPlayer2.setImage(charts.get(2));
            speedChartPlayer2.setImage(charts.get(0));
        }*/
        /*if (opponentTurretCounter == 0) {
            harmChartPlayer2.setImage(charts.get(0));
        }
        else if (opponentTurretCounter == 1) {
            harmChartPlayer2.setImage(charts.get(1));
        }
        else {
            harmChartPlayer2.setImage(charts.get(2));
        }*/
        int chartIdx = (armorWeightList.get(opponentArmorCounter) + turretWeightList.get(opponentTurretCounter)) / 5 -2;
        speedChartPlayer2.setImage(charts.get((chartIdx-5)*(-1)));
        harmChartPlayer2.setImage(minCharts.get(opponentTurretCounter));
        armorChartPlayer2.setImage(minCharts.get(opponentArmorCounter));
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

    /**
     * computes correct index for given Turret-Item
     *
     * @param item Turret-Item
     * @return correct index
     */
    private int getTurretIndex(ItemEnum item) {
        if (item == ItemEnum.LIGHT_TURRET) return 0;
        else if (item == ItemEnum.NORMAL_TURRET) return 1;
        else return 2;
    }

    /**
     * computes correct index for given Armor-Item
     *
     * @param item Armor-Item
     * @return correct index
     */
    private int getArmorIndex(ItemEnum item) {
        if (item == ItemEnum.LIGHT_ARMOR) return 0;
        else if (item == ItemEnum.NORMAL_ARMOR) return 1;
        else return 2;
    }

    /**
     * Is called when player connects and enables the ready Button
     */
    public void playerConnected() {
        if (playerConnected) return;
        Platform.runLater(() -> {
            player2ReadyText.setText("");
            playerConnected = true;
            readyButton.setDisable(false);
        });
    }

    /**
     * updates local view of opponent
     *
     * @param msg update message
     */
    public void serverUpdate(ServerTankUpdateMessage msg) {
        if (msg.turret == null) return;
        Platform.runLater(() -> {
            opponentTurretCounter = getTurretIndex(msg.turret);
            opponentArmorCounter = getArmorIndex(msg.armor);
            turretPlayer2.setImage(turrets.get(opponentTurretCounter));
            armorPlayer2.setImage(armors.get(opponentArmorCounter));
            magazineSizeTextPlayer2.setText(magazine.get(opponentTurretCounter).toString());
            cadenceTextPlayer2.setText(cadence.get(opponentTurretCounter) + " s");
            changeOpponentCharts();
        });
    }

    /**
     * starts the multiplayer game
     *
     * @param msg activation message
     */
    public void startGame(StartingMultiplayerMessage msg) {
        Platform.runLater(() -> {
            PlayersTank tank = PlayersTank.mkPlayersTank(engine.getModel(), currentTurret, currentArmor, msg.playerTank);
            Enemy enemy = Enemy.mkEnemyTank(engine.getModel(), msg.enemyTurret, msg.enemyArmor, msg.enemyTank);
            engine.setSaveTank(tank);
            engine.setSaveEnemyTank(enemy);
            engine.setMapCounter(1);
        });
    }

    /**
     * Activates PlayGameController when both players are ready and synchronized
     */
    @Override
    public void synchronizationFinished() {
        Platform.runLater(engine::activatePlayGameController);
    }

    /**
     * called when player is disconnected
     */
    @Override
    public void playerDisconnected() {
        playerConnected = false;
        readyButton.setDisable(true);
        player2ReadyText.setText("Warten auf einen zweiten Spieler");
        turretButtonLeft.setDisable(false);
        turretButtonRight.setDisable(false);
        armorButtonLeft.setDisable(false);
        armorButtonRight.setDisable(false);
    }

    /**
     * gives a player a disconnected-status
     */
    public void setPlayerDisconnected() {
        playerConnected = false;
    }

    /**
     * is called when the connection is lost
     */
    @Override
    public void lostConnection() {
        Platform.runLater(engine::activateConnectionLostController);
    }
}
