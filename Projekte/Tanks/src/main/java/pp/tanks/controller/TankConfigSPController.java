package pp.tanks.controller;

import pp.tanks.TanksImageProperty;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TankConfigSPController extends Controller {

    private static final String MENU_CONTROL_FXML = "TankConfigSP.fxml"; //NON-NLS
    private Scene scene;
    private int counterTurret = 0;
    private int counterArmor = 0;
    private final List<Image> turrets = new ArrayList<>();
    private final List<Image> armors = new ArrayList<>();
    private final List<Image> charts = new ArrayList<>();
    private final List<Integer> magazine = new ArrayList<>(Arrays.asList(5, 3, 1));
    private final List<Integer> cadence = new ArrayList<>(Arrays.asList(1, 3, 5));

    /**
     * create a new TankConfigSPController
     * @param engine the engine of the game that switches between controllers
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

    public Scene makeScene() {
        return new Scene(engine.getViewForController(MENU_CONTROL_FXML, this));
    }

    @Override
    public void entry() {
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
        engine.activateLevelController();
    }

    /**
     * method for the confirm button
     */
    @FXML
    private void confirm() {
        System.out.println("CONFIRM");
        engine.activateSPController();
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

        System.out.println("TURRET_BUTTON_RIGHT");
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

        System.out.println("TURRET_BUTTON_LEFT");
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

        System.out.println("ARMOR_BUTTON_RIGHT");
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

        System.out.println("ARMOR_BUTTON_LEFT");
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
        else if (counterArmor == 2) {
            armorChart.setImage(charts.get(1));
            speedChart.setImage(charts.get(1));
        }
        else {
            armorChart.setImage(charts.get(2));
            speedChart.setImage(charts.get(0));
        }
    }
}
