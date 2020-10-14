package pp.droids.model.item;

import pp.droids.model.DroidsGameModel;
import pp.droids.notifications.DroidsNotification;
import pp.navigation.Navigator;
import pp.util.DoubleVec;
import pp.util.IntVec;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents a droid
 */
public class Droid extends Shooter {
    private static final double EPS = 1e-4;
    private static final double TURN_SPEED = 200.;
    private static final double FORWARD_SPEED = 4.;

    private static final Logger LOGGER = Logger.getLogger(Droid.class.getName());

    private final List<DoubleVec> path = new LinkedList<>();
    private DoubleVec pos;
    private boolean moveForward;
    private boolean turnLeft;
    private boolean turnRight;
    private boolean moveBackward;
    private boolean accelerationF = false;
    private boolean accelerationB = false;
    private double currentSpeed = 0;

    /**
     * Creates a droid.
     *
     * @param model the game model that has this droid.
     */
    public Droid(DroidsGameModel model) {
        super(model, .5, 40, .2);
    }

    /**
     * Sets whether the droid shall move forward in the next frame or not depending on the argument.
     */
    public void setMoveForward(boolean moveForward) {
        this.moveForward = moveForward;
    }

    /**
     * Sets whether the droid shall move backward in the next frame or not depending on the argument.
     */
    public void setMoveBackward(boolean moveBackward) {
        this.moveBackward = moveBackward;
    }

    /**
     * Sets whether the droid shall turn left in the next frame or not depending on the argument.
     */
    public void setTurnLeft(boolean turnLeft) {
        this.turnLeft = turnLeft;
    }

    /**
     * Sets whether the droid shall turn right in the next frame or not depending on the argument.
     */
    public void setTurnRight(boolean turnRight) {
        this.turnRight = turnRight;
    }

    /**
     * Returns the position of the droid
     */
    @Override
    public DoubleVec getPos() {
        return pos;
    }

    /**
     * Sets the position of the droid
     */
    public void setPos(DoubleVec pos) {
        this.pos = pos;
    }

    /**
     * Called once per frame. The method updates the droid's position depending on the elapsed time passed
     * as the argument.
     *
     * @param delta time in seconds since the last update call
     */
    @Override
    public void update(double delta) {
        updateMovement(delta);
        moveForward = false;
        moveBackward = false;
        turnLeft = false;
        turnRight = false;
        super.update(delta);
    }

    /**
     * Indicates that the droid has been killed.
     */
    @Override
    public void destroy() {
        model.notifyReceivers(DroidsNotification.DROID_DESTROYED);
        super.destroy();
    }

    /**
     * Actually moves the droid
     *
     * @param delta time in seconds since the last update call
     */
    private void updateMovement(double delta) {

        processCollision();

        computeCurrentSpeed(delta);

        if (moveForward) {
            accelerationF = true;
            if (currentSpeed > 0) setPos(getPos().add(DoubleVec.polar(currentSpeed * delta, getRotation())));
            path.clear();
        }
        else {
            accelerationF = false;
            if (currentSpeed > 0) setPos(getPos().add(DoubleVec.polar(currentSpeed * delta, getRotation())));
        }

        if (moveBackward) {
            accelerationB = true;
            if (currentSpeed < 0) setPos(getPos().add(DoubleVec.polar(currentSpeed * delta, getRotation())));
            path.clear();
        }
        else {
            accelerationB = false;
            if (currentSpeed < 0) setPos(getPos().add(DoubleVec.polar(currentSpeed * delta, getRotation())));
        }

        if (turnLeft) {
            setRotation(getRotation() - TURN_SPEED * delta);
            path.clear();
        }
        if (turnRight) {
            setRotation(getRotation() + TURN_SPEED * delta);
            path.clear();
        }
        // as long as there is a path to follow and this time slot has still some time left
        while (path.size() > 0 && delta > 0.) {
            final DoubleVec target = path.get(0);
            if (getPos().distanceSq(target) < EPS) {
                // we arte so close... let's jump to the next path point
                setPos(target);
                path.remove(0);
            }
            else {
                final double bearing = target.sub(getPos()).angle();
                double needToTurnBy = normalizeAngle(bearing - getRotation());
                // we need to turn the droid such that its rotation coincides with the bearing of the next path point
                if (Math.abs(needToTurnBy) >= delta * TURN_SPEED) {
                    // we are turning during the rest of this time slot
                    setRotation(getRotation() + Math.signum(needToTurnBy) * delta * TURN_SPEED);
                    delta = 0.;
                }
                else {
                    // we first turn the droid
                    setRotation(bearing);
                    // and there is some time left in this time slot
                    delta -= Math.abs(needToTurnBy) / TURN_SPEED;
                    final double distanceToGo = getPos().distance(target);
                    if (distanceToGo >= delta * FORWARD_SPEED) {
                        // we do not reach the next path point in this time slot
                        setPos(getPos().add(DoubleVec.polar(FORWARD_SPEED * delta, getRotation())));
                        delta = 0.;
                    }
                    else {
                        // we have reached the next path pint in this time slot
                        setPos(target);
                        path.remove(0);
                        // and there is some time left in this time slot
                        delta -= distanceToGo / FORWARD_SPEED;
                    }
                }
            }
        }
    }

    /**
     * Returns the path the droid shall follow.
     */
    public List<DoubleVec> getPath() {
        return Collections.unmodifiableList(path);
    }

    @Override
    Projectile makeProjectile() {
        model.notifyReceivers(DroidsNotification.DROID_FIRED);
        final DoubleVec dir = DoubleVec.polar(1., getRotation());
        return Projectile.makeDroidProjectile(model, getPos(), dir);
    }

    /**
     * Searches for an optimal, collision-free path to the specified position and moves the droid there.
     *
     * @param target point to go
     */
    public void navigateTo(DoubleVec target) {
        LOGGER.info(() -> "Navigating from " + getPos() + " to " + target);
        path.clear();
        Navigator<IntVec> navigator = new DroidsNavigator(model.getDroidsMap(), getPos().toIntVec(), target.toIntVec());
        List<IntVec> pPath = navigator.findPath();
        if (pPath != null) {
            for (IntVec v : pPath)
                path.add(v.toFloatVec());
            // remove first path point as this represents the field where the droid is currently stying
            if (path.size() > 1) path.remove(0);
        }
        else
            LOGGER.info(() -> "no path to " + target.toIntVec());
    }

    /**
     * Accept method of the visitor pattern.
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    private void computeCurrentSpeed(double delta) {
        if (accelerationF && !moveBackward) {
            if (currentSpeed < FORWARD_SPEED) {
                currentSpeed = Math.min(currentSpeed += delta * 4, FORWARD_SPEED);
            }
        }
        else {
            if (currentSpeed > 0) {
                currentSpeed = Math.max(currentSpeed -= delta * 4, 0);
            }
        }

        if (accelerationB && !moveForward) {
            if (currentSpeed > -FORWARD_SPEED) {
                currentSpeed = Math.max(currentSpeed -= delta * 4, -FORWARD_SPEED);
            }
        }
        else {
            if (currentSpeed < 0) {
                currentSpeed = Math.min(currentSpeed += delta * 4, 0);
            }
        }
    }

    private void processCollision() {
        for (Obstacle obs : model.getDroidsMap().getObstacles())
            if (collisionWith(obs)) {
               double bearing = obs.getPos().sub(getPos()).angle();
               setRotation(-bearing);
            }
    }
}
