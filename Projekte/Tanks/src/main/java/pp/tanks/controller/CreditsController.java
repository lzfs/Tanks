package pp.tanks.controller;

import pp.tanks.message.client.StartGameMessage;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.PlayerEnum;
import pp.tanks.server.GameMode;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller realizing the game state when the credits list is shown.
 */
public class CreditsController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(CreditsController.class.getName());
    private static final String CREDITS_FXML = "Credits.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new CreditsController
     *
     * @param engine the engine this controller belongs to
     */
    public CreditsController(Engine engine) {
        super(engine);
    }

    /**
     * the button to get back
     */
    @FXML
    private Button back;

    /**
     * the button for debug (in credits)
     */
    @FXML
    private Button debug;

    /**
     * Create the scene displaying the credits.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(CREDITS_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated, i.e., when the credits are shown.
     */
    @Override
    void entry() {
        LOGGER.log(Level.INFO, "ENTRY CreditsController");
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    /**
     * @return the name of the used file as a String
     */
    public String getFileName() {
        return CREDITS_FXML;
    }

    /**
     * This method is called whenever this controller is deactivated,
     * i.e., when the user returns to the main menu
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT CreditsController");
    }

    /**
     * method for the back button
     */
    @FXML
    private void back() {
        LOGGER.log(Level.INFO, "GO TO MainMenuController");
        engine.activateMainMenuController();
    }

    /**
     * method for the debug button
     */
    @FXML
    private void debug() {
        engine.getTankApp().joinGame(GameMode.TUTORIAL);
        engine.setMode(GameMode.TUTORIAL);
        engine.setMapCounter(3);
        while (engine.getPlayerEnum() == null) {
            sleep();
        }
        engine.getTankApp().getConnection().send(new StartGameMessage(ItemEnum.LIGHT_TURRET, ItemEnum.LIGHT_ARMOR, GameMode.TUTORIAL, engine.getPlayerEnum()));
        engine.activatePlayGameController();
        //
    }

    /**
     * Thread is tired, he sleeps now.
     */
    private void sleep() {
        try {
            Thread.sleep(20);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

