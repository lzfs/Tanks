package pp.pegsolitaire;

import pp.pegsolitaire.model.Square;
import pp.pegsolitaire.view.SquareView;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.logging.Logger;

/**
 * The controller that handles any mouse clicks in the game view.
 */
class GameStageController implements EventHandler<MouseEvent> {
    private final static Logger LOGGER = Logger.getLogger(GameStageController.class.getName());

    private final GameStage gameStage;

    /**
     * Creates a new controller for the specified game.
     */
    public GameStageController(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    /**
     * Method that is called after each click
     */
    @Override
    public void handle(MouseEvent me) {

        // get the current clicked square view
        final SquareView clickedSquareView = (SquareView) me.getSource();
        final Square clickedSquare = clickedSquareView.getSquare();

        // let the state object handle the click event accordingly
        clickedSquare.getState().handleClickEvent(gameStage.getCross(), clickedSquare);

        // update view
        gameStage.update();

        LOGGER.info(gameStage.getCross().numOccupiedFields() + " remaining knights");

        me.consume();
    }
}
