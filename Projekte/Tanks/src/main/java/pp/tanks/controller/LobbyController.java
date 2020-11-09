package pp.tanks.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class LobbyController extends Controller {
    private static final String LOBBY_FXML = "Lobby.fxml"; //NON-NLS
    private Scene scene;

    /**
     * create a new LobbyController
     * @param engine the engine of the game that switches between controllers
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

    public Scene makeScene()  {
        return new Scene(engine.getViewForController(LOBBY_FXML, this));
    }

    @Override
    public void entry(){
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }

    @Override
    public void exit(){
        System.out.println("EXIT");
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
        System.out.println("BACK");
        engine.activateMainMenuController();
    }

    /**
     * method for the "search for game" button
     */
    @FXML
    private void searchForGame() {
        System.out.println("SEARCH_FOR_GAME");
    }

    /**
     * method for the "create game" button
     */
    @FXML
    private void createGame() {
        engine.setScene(new Scene(engine.miniController));
        System.out.println("CREATE_GAME");
    }
}
