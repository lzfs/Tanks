package pp.tanks.controller;

import pp.tanks.message.data.TankData;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.MoveDirection;
import pp.tanks.server.GameMode;
import pp.util.DoubleVec;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller displaying the start screen of a level in the singleplayer mode.
 */
public class StartGameSPController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(StartGameSPController.class.getName());
    private static final String START_GAME_SP_FXML = "StartGameSP.fxml"; //NON-NLS
    private Scene scene;
    private boolean flag = false;

    /**
     * create a new StartGameSPController
     *
     * @param engine the engine this controller belongs to
     */
    public StartGameSPController(Engine engine) {
        super(engine);
    }

    /**
     * the button to start the game
     */
    @FXML
    private Button startGameSP;

    /**
     * the text to display the remaining lives
     */
    @FXML
    private Text livesCounter;

    /**
     * the text to display the level information
     */
    @FXML
    private Text levelText;

    /**
     * the text to display the amount of enemy tanks
     */
    @FXML
    private Text enemyTanksText;

    /**
     * Create the scene shown before the game starts.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(START_GAME_SP_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated,
     * i.e., when the first mission is completed.
     */
    @Override
    public void entry() {
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
        if (flag) {
            livesCounter.setText(engine.getSaveTank().getLives() + "x");
        }
        flag = true;

        String path = "Tanks/src/main/resources/pp/tanks/model/" + "map" + engine.getMapCounter() + ".xml";
        String absolutePath = FileSystems.getDefault().getPath(path).normalize().toAbsolutePath().toString();
        try {
            File currentFile = new File(absolutePath);
            setLevelInformation(currentFile);
        }
        catch (IOException | XMLStreamException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * This method is called whenever this controller is deactivated,
     * i.e., when the the user clicked on next.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT StartGameSPController");
    }

    /**
     * @return the name of the used file as a String
     */
    public String getString() {
        return START_GAME_SP_FXML;
    }

    /**
     * method for the startGameSP button
     */
    @FXML
    private void startGameSP() {

        engine.setMode(GameMode.SINGLEPLAYER);
        engine.getSaveTank().getArmor().setArmorPoints(engine.getSaveTank().getArmor().getMaxPoints());
        if (engine.getMapCounter() == 1) loadLevelOne();
        else loadLevelTwo();

        LOGGER.log(Level.INFO, "clicked START_GAME_SP");
        engine.activatePlayGameController();
    }

    /**
     * Reads an xml file and returns the represented game map.
     *
     * @param file xml file representing a game map
     * @throws IOException        if the file doesn't exist, cannot be opened, or any other IO error occurred.
     * @throws XMLStreamException if the file is no valid xml file
     */
    public void setLevelInformation(File file) throws IOException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        Reader reader = new FileReader(file);
        XMLStreamReader xtr = factory.createXMLStreamReader(reader);

        try {
            while (xtr.hasNext()) {
                if (xtr.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    String elemName = xtr.getLocalName();
                    switch (elemName) {
                        case "enemyCounter": {
                            if (enemyTanksText == null) {
                                System.out.println("text null");
                            }
                            enemyTanksText.setText(String.valueOf(getIntAttribute("v", 0, xtr)));
                        }
                        case "levelCounter": {
                            levelText.setText(String.valueOf(getIntAttribute("v", 0, xtr)));
                        }
                        default:
                            break;
                    }
                }
                xtr.next();
            }
        }
        finally {
            xtr.close();
        }
    }

    /**
     * Reads an integer value of an attribute of the current XML element.
     *
     * @param name         the name of the attribute
     * @param defaultValue the default value used when this attribute is missing or if it has a value
     *                     that does not represent an integer number.
     * @return the integer value of the attribute or null if the attribute is missing or has a non-integer value.
     */
    private int getIntAttribute(String name, int defaultValue, XMLStreamReader xtr) {
        String value = getAttribute(name, xtr);
        if (value == null)
            return defaultValue;
        else
            try {
                return Integer.parseInt(value);
            }
            catch (NumberFormatException e) {
                LOGGER.warning("Attribute " + name + " should be an int, but has value " + value);
                return defaultValue;
            }
    }

    /**
     * Reads the string value of an attribute of the current XML element.
     *
     * @param name the name of the attribute
     * @return the string value of the attribute or null if the attribute is missing.
     */
    private String getAttribute(String name, XMLStreamReader xtr) {
        for (int i = 0; i < xtr.getAttributeCount(); i++)
            if (xtr.getAttributeLocalName(i).equals(name))
                return xtr.getAttributeValue(i);
        return null;
    }

    /**
     * called when level one gets loaded
     */
    private void loadLevelOne() {
        TankData enemy1 = new TankData(new DoubleVec(18, 7), 1, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        TankData enemy2 = new TankData(new DoubleVec(20, 5), 3, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        engine.playGameController.constructionEnum.addAll(List.of(ItemEnum.ACP, ItemEnum.HOWITZER));
        engine.playGameController.constructionData.addAll(List.of(enemy1, enemy2));
    }

    /**
     * called when level two gets loaded
     */
    private void loadLevelTwo() {
        TankData enemy1 = new TankData(new DoubleVec(20, 4), 1, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        TankData enemy2 = new TankData(new DoubleVec(20, 6), 2, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        TankData enemy3 = new TankData(new DoubleVec(20, 8), 3, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        engine.playGameController.constructionEnum.addAll(List.of(ItemEnum.ACP, ItemEnum.HOWITZER, ItemEnum.TANK_DESTROYER));
        engine.playGameController.constructionData.addAll(List.of(enemy1, enemy2, enemy3));
    }
}
