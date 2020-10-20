package pp.droids.model.item;

import pp.droids.model.DroidsGameModel;
import pp.droids.notifications.DroidsNotification;
import pp.util.DoubleVec;

/**
 * Abstract base class of all moving items in a {@linkplain pp.droids.model.DroidsMap}
 */
public abstract class Mover extends Item {
    /**
     * the target to move to
     */
    protected DoubleVec target = DoubleVec.NULL;

    /**
     * the speed
     */
    protected double speed = 1.;

    /**
     * flag indicating this item is moving or not
     */
    private boolean moving = false;

    public Mover(DroidsGameModel model, double effectiveRadius) {
        super(model, effectiveRadius);
    }

    /**
     * Sets the position
     *
     * @param pos new position
     */
    public abstract void setPos(DoubleVec pos);

    /**
     * Returns the speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the speed
     *
     * @param f new speed value
     */
    public void setSpeed(double f) {
        this.speed = f;
    }

    /**
     * Returns true if this item is currently moving
     */
    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        if (moving && !this.isMoving())
            model.notifyReceivers(DroidsNotification.ROCKET_STARTS);
        this.moving = moving;
    }

    /**
     * The actual method to move a moving item. Only linear movement is considered, no rotation
     *
     * @param delta delta time for update
     */
    protected void move(double delta) {
        // check if arrived at target x position
        // Only x is considered due to only horizontal movement
        if (getPos().x == target.x) {
            setMoving(false);
            return;
        }
        // if near target jump to target
        if (getSpeed() * delta >= getPos().distance(target)) {
            setPos(target);
        }
        // else move to target according to own speed (no rotation)
        else {
            setPos(getPos().add(new DoubleVec(-getSpeed() * delta, 0.)));
        }
    }

    /**
     * Returns the target position
     *
     * @return FloatVec target position
     */
    public DoubleVec getTarget() {
        return target;
    }

    /**
     * Sets the target vector
     *
     * @param pos target position
     */
    public void setTarget(DoubleVec pos) {
        target = pos;
    }

    /**
     * Updates the item
     *
     * @param delta time in seconds since the last update call
     */
    @Override
    public void update(double delta) {
        if (moving)
            move(delta);
    }

    /**
     * Increases the speed
     * @param f the value which gets added to the current speed
     */
    public void increaseSpeed(double f) {
        if (moving)
            speed += f;
    }
}
