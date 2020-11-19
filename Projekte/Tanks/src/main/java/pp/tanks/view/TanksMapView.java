package pp.tanks.view;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import pp.tanks.ImageSupport;
import pp.tanks.TanksImageProperty;
import pp.tanks.model.Model;
import pp.tanks.model.item.HeavyProjectile;
import pp.tanks.model.item.Item;
import pp.tanks.model.item.Projectile;
import pp.tanks.notification.TanksNotification;
import pp.tanks.notification.TanksNotificationReceiver;
import pp.util.DoubleVec;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static pp.tanks.TanksImageProperty.backgroundImage;
import static pp.tanks.TanksImageProperty.bigExplosion;
import static pp.tanks.TanksImageProperty.explosion;

/**
 * Represents the view of the game map
 */
public class TanksMapView extends Canvas implements TanksNotificationReceiver {
    private static final Logger LOGGER = Logger.getLogger(TanksMapView.class.getName());

    private static final Font TEXT_FONT = new Font(13);
    private static final Font HINT_FONT = new Font(16);
    static final int FIELD_SIZE = 50;
    static final int HALF_FIELD_SIZE = FIELD_SIZE / 2;

    private final Model model;
    private final ImageSupport<TanksImageProperty> images;
    private final VisualizerVisitor visualizer;

    private final List<Projectile> projectiles = new ArrayList<>();

    private ProgressBar progressBar;

    /**
     * Creates a view for the specified game model
     *
     * @param model game model
     */
    public TanksMapView(Model model, ImageSupport<TanksImageProperty> images) {
        this.model = model;
        this.images = images;
        this.visualizer = new VisualizerVisitor(this);
        this.progressBar = new ProgressBar(1.0);
        setCanvasSize();
        model.addReceiver(this);
    }

    /**
     * Sets the size of the drawing area.
     */
    private void setCanvasSize() {
        setWidth(model.getTanksMap().getWidth() * FIELD_SIZE);
        setHeight(model.getTanksMap().getHeight() * FIELD_SIZE);
    }

    /**
     * Returns the manager object for all images.
     *
     * @return the images
     */
    ImageSupport<TanksImageProperty> getImages() {
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

        // render items
        for (Item p : model.getTanksMap()) {
            p.accept(visualizer);
        }

        for (Projectile p : projectiles) {
            for (int i = 0; i < 5; i++) {
                if (p instanceof HeavyProjectile) {
                    drawImage(bigExplosion, p.getPos().x, p.getPos().y);
                }
                else {
                    drawImage(explosion, p.getPos().x, p.getPos().y);
                }
            }
        }
        projectiles.removeAll(projectiles);

        context.setFont(TEXT_FONT);
        context.setFill(Color.WHITE);
        context.setFont(TEXT_FONT);
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
    public void notify(TanksNotification notification) {
        LOGGER.finer("received " + notification);
        if (notification == TanksNotification.MAP_UPDATE) {
            setCanvasSize();
            if (getScene() != null && getScene().getWindow() != null)
                getScene().getWindow().sizeToScene();
        }
    }

    public void addExplosion(Projectile projectile) {
        projectiles.add(projectile);
    }

    /**
     * Draws an image if such an image has been configured.
     *
     * @param prop the string property indicating an image
     */
    private void drawImage(TanksImageProperty prop, double x, double y) {
        final GraphicsContext context = getGraphicsContext2D();
        final Image img = getImages().getImage(prop);
        DoubleVec pos = modelToView(x, y);
        if (img != null) {
            context.drawImage(img, pos.x - (img.getWidth() * 0.5), pos.y - (img.getHeight() * 0.5));
        }
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void updateProgressBar(double percentage) {
        this.progressBar.setProgress(percentage);
    }
}
