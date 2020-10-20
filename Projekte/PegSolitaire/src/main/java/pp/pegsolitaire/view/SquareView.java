package pp.pegsolitaire.view;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import pp.pegsolitaire.model.Square;

/**
 * A class representing a view element of a single square.
 */
public class SquareView extends ImageView {

    private static final Image KNIGHT_IMAGE = new Image(SquareView.class.getResource("/images/knight.png").toString());
    private static final Image EMPTY_HIGHLIGHT_IMAGE = new Image(SquareView.class.getResource("/images/empty_highlight.png").toString());

    private static final Effect DROP_SHADOW = new DropShadow(BlurType.GAUSSIAN, Color.LIGHTGREEN, 8, 0.5, 0, 0);

    private final Square square;

    /**
     * Creates a new view element for the specified square.
     */
    public SquareView(Square square) {
        this.square = square;
        update();
    }

    /**
     * Updates this view element.
     */
    public void update() {
        setImage(null);
        setEffect(null);

        if (square.isEmptyReachable()) {
            setImage(EMPTY_HIGHLIGHT_IMAGE);
            setEffect(DROP_SHADOW);
        }
        else if (square.isOccupied()) {
            setImage(KNIGHT_IMAGE);
            if (square.isSelected())
                setEffect(DROP_SHADOW);
        }
    }

    /**
     * Returns the square represented by this view element.
     *
     * @return square model element
     */
    public Square getSquare() {
        return square;
    }

    /**
     * Returns a string representation of this view element. This method in fact returns just the
     * string representation of the square represented by this view element.
     */
    @Override
    public String toString() {
        return square.toString();
    }
}
