package pp.pegsolitaire.view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pp.pegsolitaire.model.Cross;

import java.util.ArrayList;
import java.util.List;

/**
 * View of the cross model element
 */
public class CrossView extends Group {

    private final static Image FINISHED_IMAGE = new Image(CrossView.class.getResource("/images/finished.png").toString());
    private final static Image BACKGROUND_IMAGE = new Image(CrossView.class.getResource("/images/desertcross.png").toString());
    private final static ImageView BACKGROUND_VIEW = new ImageView(BACKGROUND_IMAGE);

    private final static int SQUARE_PX_WIDTH = 32;
    private final static int SQUARE_PX_HEIGHT = 32;

    private final List<SquareView> squareViews = new ArrayList<>(Cross.SQUARE_XY_COUNT * Cross.SQUARE_XY_COUNT);
    private final Cross cross;

    /**
     * Constructor of CrossGameMap
     *
     * @param cross cross model element
     */
    public CrossView(Cross cross) {
        this.cross = cross;
        getChildren().add(BACKGROUND_VIEW);
        /* Initialize all fields with knights and empty images */
        for (int x = 0; x < Cross.SQUARE_XY_COUNT; x++)
            for (int y = 0; y < Cross.SQUARE_XY_COUNT; y++) {
                SquareView squareView = new SquareView(cross.getSquare(x, y));
                squareViews.add(squareView);
                squareView.setTranslateX((x + 1) * SQUARE_PX_WIDTH);
                squareView.setTranslateY((y + 1) * SQUARE_PX_HEIGHT);
                getChildren().add(squareView);
            }
        prefHeight((Cross.SQUARE_XY_COUNT + 2) * SQUARE_PX_HEIGHT);
        prefWidth((Cross.SQUARE_XY_COUNT + 2) * SQUARE_PX_WIDTH);
    }

    /**
     * Returns the game model shown by this view
     *
     * @return the cross model
     */
    public Cross getCross() {
        return cross;
    }

    /**
     * Updates the view
     */
    public void update() {
        for (SquareView sqv : squareViews)
            sqv.update();

        // check if game is finished
        if (cross.numOccupiedFields() == 1)
            getChildren().add(new ImageView(FINISHED_IMAGE));
    }

    /**
     * Returns a list of all controls added to this view.
     *
     * @return list of controls
     */
    public List<? extends Node> getControls() {
        return squareViews;
    }
}
