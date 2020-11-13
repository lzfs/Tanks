package pp.tanks.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class GameOverMPController extends Controller {
    private static final String GAME_OVER_MP_FXML = "GameOverMP.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new GameOverMPController
     *
     * @param engine the engine of the game that switches between controllers
     */
    public GameOverMPController(Engine engine) {
        super(engine);
    }

    /**
     * the button to go back to the lobby
     */
    @FXML
    private Button lobbyButton;

    public Scene makeScene() {
        return new Scene(engine.getViewForController(GAME_OVER_MP_FXML, this));
    }
    /**
     * This method is called whenever this controller is activated, i.e., when the game is over.
     */
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
        return GAME_OVER_MP_FXML;
    }

    /**
     * method for the back button
     */
    @FXML
    private void lobby() {
        System.out.println("LOBBY");
        engine.activateLobbyController();
    }
}
