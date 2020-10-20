package pp.droids.model.item;

import pp.droids.model.DroidsGameModel;
import pp.droids.notifications.DroidsNotification;
import pp.util.DoubleVec;

import java.util.logging.Logger;

/**
 * Represents an enemy
 */
public class Enemy extends LivingItem {
    private final DoubleVec pos;
    private static final Logger LOGGER = Logger.getLogger(Enemy.class.getName());

    /**
     * Creates an enemy
     *
     * @param pos position
     */
    public Enemy(DroidsGameModel model, DoubleVec pos) {
        super(model, .5, 4);
        this.pos = pos;
    }

    /**
     * Returns the position of the enemy
     */
    @Override
    public DoubleVec getPos() {
        return pos;
    }

    /**
     * Called once per frame. Updates the enemy
     *
     * @param delta time in seconds since the last update call
     */
    @Override
    public void update(double delta) {
        super.update(delta);
    }

    /**
     * Indicates that this enemy has been destroyed.
     */
    @Override
    public void destroy() {
        model.notifyReceivers(DroidsNotification.ENEMY_DESTROYED);
        if (model.getEngine() != null) model.getEngine().pauseLoop(2000);
        super.destroy();
    }

    /**
     * Accept method of the visitor pattern.
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}