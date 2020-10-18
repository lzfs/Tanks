package pp.droids.view;

import pp.droids.model.item.Droid;
import pp.droids.model.item.Enemy;
import pp.droids.model.item.Item;
import pp.droids.model.item.Moon;
import pp.droids.model.item.Obstacle;
import pp.droids.model.item.Projectile;
import pp.droids.model.item.Rocket;
import pp.droids.model.item.Visitor;
import pp.util.DoubleVec;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Item visitor drawing debug decorations to the Droids map view
 */
class DebugVisitor implements Visitor {
    private final static double POINT_RADIUS = 5.;
    private final DroidsMapView view;

    /**
     * Constructor of DebugView
     *
     * @param view graphics context
     */
    public DebugVisitor(DroidsMapView view) {
        this.view = view;
    }

    @Override
    public void visit(Droid droid) {
        drawBoundingCircle(droid);
        DoubleVec droidDir = DoubleVec.polar(10., droid.getRotation());
        drawLine(droid.getPos(), droid.getPos().add(droidDir), Color.YELLOW);
        for (DoubleVec p : droid.getPath())
            drawPoint(p, Color.RED);
    }

    @Override
    public void visit(Obstacle o) {
        drawBoundingCircle(o);
    }

    @Override
    public void visit(Enemy e) {
        drawBoundingCircle(e);
    }

    @Override
    public void visit(Projectile p) {
        drawBoundingCircle(p);
        drawLine(p.getPos(), p.getPos().add(p.getDirection()), Color.YELLOW);
    }

    @Override
    public void visit(Rocket rocket) {}

    @Override
    public void visit(Moon moon) {

    }

    /**
     * Draws a Circle
     * @param item the Item, on which a circle is drawn
     */
    private void drawBoundingCircle(Item item) {
        drawCircle(item.getPos(), item.effectiveRadius * DroidsMapView.FIELD_SIZE, Color.YELLOW);
    }

    /**
     * Draws a point
     *
     * @param pos   position
     * @param color color
     */
    private void drawPoint(DoubleVec pos, Paint color) {
        final DoubleVec vPos = view.modelToView(pos);
        view.getGraphicsContext2D().setFill(color);
        view.getGraphicsContext2D().fillOval(vPos.x - POINT_RADIUS, vPos.y - POINT_RADIUS, POINT_RADIUS * 2., POINT_RADIUS * 2.);
    }

    /**
     * Draws a circle
     *
     * @param pos    position
     * @param radius radius
     * @param color  color
     */
    private void drawCircle(DoubleVec pos, double radius, Paint color) {
        final DoubleVec vPos = view.modelToView(pos);
        view.getGraphicsContext2D().setStroke(color);
        view.getGraphicsContext2D().strokeOval(vPos.x - radius, vPos.y - radius, radius * 2, radius * 2);
    }

    /**
     * Draws a line
     *
     * @param from  from
     * @param to    to
     * @param color color
     */
    private void drawLine(DoubleVec from, DoubleVec to, Paint color) {
        final DoubleVec vFrom = view.modelToView(from);
        final DoubleVec vTo = view.modelToView(to);
        view.getGraphicsContext2D().setStroke(color);
        view.getGraphicsContext2D().setLineWidth(2.);
        view.getGraphicsContext2D().strokeLine(vFrom.x, vFrom.y, vTo.x, vTo.y);
    }
}
