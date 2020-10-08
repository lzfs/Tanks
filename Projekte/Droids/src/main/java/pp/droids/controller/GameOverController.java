package pp.droids.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * The controller realizing the game state when the game is over, either by winning or loosing the game.
 */
class GameOverController extends Controller {
    private final String text;
    private Scene scene;

    /**
     * Creates the controller.
     *
     * @param engine the game engine this controller belongs to
     * @param text   the text, which is set in the Scene
     */
    public GameOverController(GameEngine engine, String text) {
        super(engine);
        this.text = text;
    }

    /**
     * Creates the scene shown when the game is over.
     */
    private Scene makeScene() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10, 10, 10, 10));
        box.setAlignment(Pos.CENTER);
        final Label label = new Label(text);
        final Button button = new Button("Zurück zum Menü");
        button.setFocusTraversable(false);
        button.setOnAction(e -> engine.activateMenuController());
        box.getChildren().addAll(label, button);
        return new Scene(box);
    }

    /**
     * This method is called whenever this controller is activated, i.e., when the game is over.
     */
    @Override
    void entry() {
        if (scene == null)
            scene = makeScene();
        engine.setScene(scene);
    }
}
