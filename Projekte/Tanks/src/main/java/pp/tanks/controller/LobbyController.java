package pp.tanks.controller;

import pp.tanks.server.GameMode;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The controller displaying the lobby settings.
 */
public class LobbyController extends Controller {
    private static final Logger LOGGER = Logger.getLogger(LobbyController.class.getName());
    private static final String LOBBY_FXML = "Lobby.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new LobbyController
     *
     * @param engine the engine this controller belongs to
     */
    public LobbyController(Engine engine) {
        super(engine);
    }

    /**
     * the button to search for a game
     */
    @FXML
    private Button searchForGame;

    /**
     * the button to create a new game
     */
    @FXML
    private Button createGame;

    /**
     * the button to get back
     */
    @FXML
    private Button back;

    /**
     * Create the scene displaying the lobby settings.
     */
    public Scene makeScene() {
        return new Scene(engine.getViewForController(LOBBY_FXML, this));
    }

    /**
     * This method is called whenever this controller is activated,
     * i.e., when the user clicked "Multiplayer" in the main menu.
     */
    @Override
    public void entry() {
        LOGGER.log(Level.INFO, "ENTRY LobbyController");
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    /**
     * This method is called whenever this controller is deactivated, i.e., when the users chose a lobby.
     */
    @Override
    public void exit() {
        LOGGER.log(Level.INFO, "EXIT LobbyController");
    }

    /**
     * @return the name of the file as a String
     */
    public String getFileName() {
        return LOBBY_FXML;
    }

    /**
     * method for the back button
     */
    @FXML
    private void back() {
        LOGGER.log(Level.INFO, "clicked BACK");
        engine.activateMainMenuController();
    }

    /**
     * method for the "search for game" button
     */
    @FXML
    private void searchForGame() {
        LOGGER.log(Level.INFO, "clicked SEARCH_FOR_GAME");
        engine.getTankApp().joinGame(GameMode.MULTIPLAYER);
        System.out.println("Client connected to MP");
        engine.setScene(new Scene(engine.miniController));
    }

    /**
     * method for the "create game" button
     */
    @FXML
    private void createGame() {
        LOGGER.log(Level.INFO, "clicked CREATE_GAME");
        engine.getTankApp().joinGame(GameMode.MULTIPLAYER);
        System.out.println("Client connected to MP");
        engine.setScene(new Scene(engine.miniController));
    }
}
