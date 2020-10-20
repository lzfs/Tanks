package pp.pegsolitaire;

import pp.pegsolitaire.model.Cross;
import pp.pegsolitaire.view.CrossView;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

/**
 * Represents the stage of the game
 */
class GameStage extends Stage {
    private final CrossView crossView;

    /**
     * Creates a game stage
     *
     * @param cross the game model
     */
    public GameStage(Cross cross) {
        setTitle("Peg Solitaire");
        setResizable(false);

        // create droids map view
        crossView = new CrossView(cross);
    }

    /**
     * Returns the game model
     *
     * @return cross
     */
    public Cross getCross() {
        return crossView.getCross();
    }

    /**
     * Returns the list of all controls shown in the view. Each of these controls represent the
     * visualization of a single square of the game map, i.e., the cross of all squares.
     */
    public List<? extends Node> getControls() {
        return crossView.getControls();
    }

    public void update() {
        crossView.update();
    }

    /**
     * Sets the scene and shows it
     */
    public void setSceneAndShow() {
        setScene(new Scene(crossView));
        sizeToScene();
        show();
    }
}