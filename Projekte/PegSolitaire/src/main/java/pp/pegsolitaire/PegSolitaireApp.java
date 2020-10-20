package pp.pegsolitaire;

import pp.pegsolitaire.model.Cross;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.logging.Logger;

/**
 * Main class of the Peg Solitaire game <br>
 * The game is derived from: https://github.com/joerno/PegSolitaire
 */
public class PegSolitaireApp extends Application {

    private final static Logger LOGGER = Logger.getLogger(PegSolitaireApp.class.getName());

    /**
     * Called when the game is started
     */
    @Override
    public void start(Stage primaryStage) {
        Cross model = new Cross();
        GameStage view = new GameStage(model);
        GameStageController controller = new GameStageController(view);

        for (Node control : view.getControls())
            control.setOnMousePressed(controller);

        view.setSceneAndShow();

        LOGGER.info("Peg Solitaire started");
    }

    /**
     * Main method of the game
     *
     * @param args input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}