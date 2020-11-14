package pp.tanks.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller realizing the game state when the connection is lost.
 */
public class ConnectionLostController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(ConnectionLostController.class.getName());
    private static final String CONNECTION_LOST_FXML = "ConnectionLost.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new ConnectionLostController
     *
     * @param engine the engine this controller belongs to
     */
    public ConnectionLostController(Engine engine) {
        super(engine);
    }

    /**
     * the button to go back to the lobby
     */
    @FXML
    private Button lobbyButton;

    /**
     * Create the scene shown when the connection is lost.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(CONNECTION_LOST_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated, i.e., when the connection is lost.
     */
    @Override
    public void entry() {
        LOGGER.log(Level.INFO, "ENTRY ConnectionLostController");
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    /**
     * This method is called whenever this controller is deactivated, i.e., when the users got reconnected
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT ConnectionLostController");
    }

    /**
     * @return the name of the file as a String
     */
    public String getFileName() {
        return CONNECTION_LOST_FXML;
    }

    /**
     * method for the back button
     */
    @FXML
    private void lobby() {
        LOGGER.log(Level.INFO, "GO TO LobbyController");
        engine.activateLobbyController();
    }
}
