package pp.droids.view;

import pp.droids.DroidsImageProperty;
import pp.droids.DroidsStringProperty;
import pp.droids.model.DroidsGameModel;
import pp.droids.model.item.Item;
import pp.droids.notifications.DroidsNotification;
import pp.droids.notifications.DroidsNotificationReceiver;
import pp.media.ImageSupport;
import pp.util.DoubleVec;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.logging.Logger;

import static pp.droids.DroidsImageProperty.backgroundImage;

/**
 * Represents the view of the game map
 */
public class DroidsMapView extends Canvas implements DroidsNotificationReceiver {
    private static final Logger LOGGER = Logger.getLogger(DroidsMapView.class.getName());

    private static final Font TEXT_FONT = new Font(13);
    private static final Font HINT_FONT = new Font(16);
    static final int FIELD_SIZE = 50;
    static final int HALF_FIELD_SIZE = FIELD_SIZE / 2;

    private final DroidsGameModel model;
    private final ImageSupport<DroidsImageProperty> images;
    private final VisualizerVisitor visualizer;
    private final DebugVisitor debug;

    /**
     * Creates a view for the specified game model
     *
     * @param model game model
     */
    public DroidsMapView(DroidsGameModel model, ImageSupport<DroidsImageProperty> images) {
        this.model = model;
        this.images = images;
        this.visualizer = new VisualizerVisitor(this);
        this.debug = new DebugVisitor(this);
        setCanvasSize();
        model.addReceiver(this);
    }

    /**
     * Sets the size of the drawing area.
     */
    private void setCanvasSize() {
        setWidth(model.getDroidsMap().getWidth() * FIELD_SIZE);
        setHeight(model.getDroidsMap().getHeight() * FIELD_SIZE);
    }

    /**
     * Returns the manager object for all images.
     *
     * @return the images
     */
    ImageSupport<DroidsImageProperty> getImages() {
        return images;
    }

    /**
     * Renders the view
     */
    public void update() {
        final GraphicsContext context = getGraphicsContext2D();

        // delete background
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, getWidth(), getHeight());

        // draw background image
        final Image bgImage = images.getImage(backgroundImage);
        if (bgImage != null) context.drawImage(bgImage, 0, 0);

        if (model.isDebugMode()) {
            // draw grid
            context.setStroke(Color.WHITE);
            context.setLineWidth(1.);
            for (int x = FIELD_SIZE; x < getWidth(); x += FIELD_SIZE)
                context.strokeLine(x, 0, x, getHeight());
            for (int y = FIELD_SIZE; y < getHeight(); y += FIELD_SIZE)
                context.strokeLine(0, y, getWidth(), y);
        }

        // render items
        for (Item p : model.getDroidsMap()) {
            p.accept(visualizer);
            if (model.isDebugMode())
                p.accept(debug);
        }

        //
        context.setFont(TEXT_FONT);
        context.setFill(Color.WHITE);
        context.setFont(TEXT_FONT);
        final String prefix = DroidsStringProperty.remainingLivesText.value(model.getProperties());
        context.fillText(prefix + model.getDroidsMap().getDroid().getLives(), 10, 20);
        if (model.getShowHint()) {
            context.setFont(HINT_FONT);
            final String text = DroidsStringProperty.hintText.value(model.getProperties());
            context.fillText(text, 10, getHeight() - 10);
        }
    }

    /**
     * Translates model coordinates to view coordinates
     *
     * @param vec model coordinates
     * @return the corresponding view coordinates
     */
    public DoubleVec modelToView(DoubleVec vec) {
        return modelToView(vec.x, vec.y);
    }

    /**
     * Translates model coordinates to view coordinates
     *
     * @param xm x coordinate of the model coordinates
     * @param ym y coordinate of the model coordinates
     * @return the corresponding view coordinates
     */
    public DoubleVec modelToView(double xm, double ym) {
        double x = xm * FIELD_SIZE + HALF_FIELD_SIZE;
        double y = ym * FIELD_SIZE + HALF_FIELD_SIZE;
        return new DoubleVec(x, y);
    }

    /**
     * Translates view coordinates to model coordinates
     *
     * @param x x coordinate of the view coordinates
     * @param y y coordinate of the view coordinates
     * @return the corresponding model coordinates
     */
    public DoubleVec viewToModel(double x, double y) {
        double xm = x / FIELD_SIZE - 0.5;
        double ym = y / FIELD_SIZE - 0.5;
        return new DoubleVec(xm, ym);
    }

    /**
     * Subscriber method according to the subscriber pattern. The method updates the view whenever the game map has
     * changed.
     */
    @Override
    public void notify(DroidsNotification notification) {
        LOGGER.finer("received " + notification);
        if (notification == DroidsNotification.MAP_UPDATE) {
            setCanvasSize();
            if (getScene() != null && getScene().getWindow() != null)
                getScene().getWindow().sizeToScene();
        }
    }
}
