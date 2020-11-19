package pp.tanks.view;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * This is a bar representing the player's own score. It's implemented as a rectangular beam at the upper left corner of the street.
 */
public class ClientScore extends AnchorPane{
    private final int WIDTH = 5;
    private final int HEIGHT = 1;
    private Rectangle score = new Rectangle();
    private Rectangle border = new Rectangle();
    private Text percentageText = new Text("XX%");

    /**
     * Standard constructor setting the width and height, using a rectangle as background and another, colored rectangle as representation of your terrain in %.
     */
    public ClientScore(){
        setWidth(WIDTH);
        setHeight(HEIGHT);

        border.setWidth(WIDTH);
        border.setHeight(HEIGHT);
        border.setFill(Color.LIGHTGRAY);
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(3);

        score.setHeight(HEIGHT);
        score.setStroke(Color.BLACK);
        score.setStrokeWidth(3);

        percentageText.setFill(Color.WHITE);
        percentageText.setStroke(Color.BLACK);
        percentageText.setStrokeWidth(1);
        percentageText.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 20.));
        percentageText.setY(.75 * HEIGHT);
        percentageText.setX(0.5 * WIDTH - percentageText.prefWidth(percentageText.prefHeight(10.)) / 2);

        setTopAnchor(border, 0.);
        setLeftAnchor(score, 0.);

        getChildren().addAll(border, score, percentageText);
    }

    /**
     * Updates the ClientScore with a new Color and the new percentage
     *
     * @param c          The color used to fill the rectangle in the front, this is the player's color.
     * @param percentage double for the percentage of the map the player currently has captured.
     */
    public void update(Color c, double percentage){
        percentageText.setText((Math.round(percentage * 10) / 10.0) + "%");
        score.setFill(c);
        score.setWidth(percentage / 100 * WIDTH);
    }
}
