package pp.droids.view;

import pp.droids.DroidsImageProperty;
import pp.droids.model.item.Droid;
import pp.droids.model.item.Enemy;
import pp.droids.model.item.Item;
import pp.droids.model.item.Moon;
import pp.droids.model.item.Obstacle;
import pp.droids.model.item.Projectile;
import pp.droids.model.item.Rocket;
import pp.droids.model.item.Visitor;
import pp.util.DoubleVec;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

/**
 * Item visitor that adds visual representations of items to the Droids map view
 */
class VisualizerVisitor implements Visitor {
    private enum Shape {RECTANGLE, OVAL, DIRECTED_OVAL}

    private final DroidsMapView view;

    public VisualizerVisitor(DroidsMapView view) {
        this.view = view;
    }

    @Override
    public void visit(Droid droid) {
        if (droid.isVisible()) {
            final GraphicsContext context = view.getGraphicsContext2D();
            final Affine ori = context.getTransform();
            final DoubleVec pos = view.modelToView(droid.getPos());
            context.translate(pos.x, pos.y);
            context.rotate(droid.getRotation());
            drawImage(DroidsImageProperty.greenShipImage, Shape.DIRECTED_OVAL, Color.GREEN);
            context.setTransform(ori);
        }
    }

    @Override
    public void visit(Enemy enemy) {
        if (enemy.isVisible())
            drawItem(enemy, DroidsImageProperty.redShipImage, Shape.OVAL, Color.RED);
    }

    @Override
    public void visit(Obstacle obs) {
        drawItem(obs, DroidsImageProperty.asteroidImage, Shape.RECTANGLE, Color.BLUE);
    }

    @Override
    public void visit(Projectile p) {
        drawItem(p, DroidsImageProperty.droidProjectileImage, Shape.RECTANGLE, Color.RED);
    }

    @Override
    public void visit(Rocket rocket) {
        if (rocket.isMoving())
            drawItem(rocket, DroidsImageProperty.rocketImage, Shape.RECTANGLE, Color.BLUE);
    }

    @Override
    public void visit(Moon moon) {
        drawItem(moon, DroidsImageProperty.moonImage, Shape.OVAL, Color.GRAY);
    }

    /**
     * Draws an item as an image or, if the image is missing, as the specified shape.
     *
     * @param item  the item to be drawn
     * @param prop  the property specifying the image
     * @param shape the shape, which is drawn if the image is missing
     * @param color the color, which is used if the image is missing
     */
    private void drawItem(Item item, DroidsImageProperty prop, Shape shape, Color color) {
        final GraphicsContext context = view.getGraphicsContext2D();
        final Affine ori = context.getTransform();
        final DoubleVec pos = view.modelToView(item.getPos());
        context.translate(pos.x, pos.y);
        drawImage(prop, shape, color);
        context.setTransform(ori);
    }

    /**
     * Draws an image if such an image has been configured.
     * Otherwise, a specified shape of the specified color is drawn
     *
     * @param prop  the string property indicating an image
     * @param shape the shape to be used when the image is missing
     * @param color the color that is used as a circle color if the image is missing
     */
    private void drawImage(DroidsImageProperty prop, Shape shape, Color color) {
        final GraphicsContext context = view.getGraphicsContext2D();
        final Image img = view.getImages().getImage(prop);
        if (img != null)
            context.drawImage(img, -img.getWidth() * 0.5, -img.getHeight() * 0.5);
        else
            switch (shape) {
                case RECTANGLE:
                    context.setFill(color);
                    context.fillRect(-DroidsMapView.HALF_FIELD_SIZE + 2,
                                     -DroidsMapView.HALF_FIELD_SIZE + 2,
                                     DroidsMapView.FIELD_SIZE - 4,
                                     DroidsMapView.FIELD_SIZE - 4);
                    break;
                case DIRECTED_OVAL:
                    context.setFill(Color.BROWN);
                    context.fillRect(10, -10, 20, 20);
                case OVAL:
                    context.setFill(color);
                    context.fillOval(-DroidsMapView.HALF_FIELD_SIZE + 2,
                                     -DroidsMapView.HALF_FIELD_SIZE + 2,
                                     DroidsMapView.FIELD_SIZE - 4,
                                     DroidsMapView.FIELD_SIZE - 4);
                    break;
            }
    }
}
