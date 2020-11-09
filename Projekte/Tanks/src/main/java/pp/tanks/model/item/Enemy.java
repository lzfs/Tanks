package pp.tanks.model.item;

import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a Enemy; could be a COMEnemy or a Enemy played by another person
 */
public class Enemy extends Tank {
    private double animationTime;
    private DoubleVec targetPos;

    protected Enemy(Model model, double effectiveRadius, Armor armor, Turret turret) {
        super(model, effectiveRadius, armor, turret);
    }

    /**
     * method for test cases
     */
    @Override
    public void isVisible() {

    }

    /**
     * Accept method of the visitor pattern.
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
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
        super.destroy();
    }
}
