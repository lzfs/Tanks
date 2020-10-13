package pp.droids.model.item;

import pp.droids.model.DroidsGameModel;
import pp.util.DoubleVec;

public class Moon extends Mover {
    private DoubleVec pos;
    private final double distance;
    private double dg;

    /**
     * Creates a new moon with the specified starting position and its speed set to 1
     *
     */
    public Moon(DroidsGameModel model, DoubleVec target) {
        super(model, .5);
        setTarget(target);
        final double tmp = Math.random() * (7 - 3) + 3;
        this.pos = new DoubleVec(target.x + tmp, target.y + tmp);
        setPos(pos);
        this.distance = 0.2 * getPos().distance(getTarget());
        setMoving(true);
        speed = Math.random();
        this.dg = pos.angle();
    }

    /**
     * sets new position
     *
     * @param pos new position
     */
    @Override
    public void setPos(DoubleVec pos) {
        this.pos = pos;
    }

    /**
     * returns position
     *
     * @return position
     */
    @Override
    public DoubleVec getPos() {
        return pos;
    }

    /**
     * Accept method of the visitor pattern
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    /*@Override
    public void setMoving(boolean moving) {
       TODO
    }*/

    /**
     * The actual method to move a moving item. Rotating around the target position
     *
     * @param delta delta time for update
     */
    @Override
    protected void move(double delta) {
        dg = dg + getSpeed();
        setPos(getTarget().add(DoubleVec.polar(distance, dg)));

    }



    //CollistionWith überschreiben? Destroyed?
}
