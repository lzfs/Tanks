package pp.tanks.model.item;

import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a enemy played by the computer
 */
public class COMEnemy extends Enemy {

    protected COMEnemy(Model model, double effectiveRadius, Armor armor, Turret turret, TankData data) {
        super(model, effectiveRadius, armor, turret, data);
    }

    /**
     * method for test cases to check if the COMEnemy can shoot
     */
    public void cheatShoot() {
        //TODO
    }

    /**
     * method for test cases to check if the COMEnemy can move
     */
    public void cheatMove(DoubleVec pos) {
        //TODO
    }

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param delta time in seconds since the last update call
     */
    @Override
    public void update(double delta) {
        turret.update(delta);
        if (isMoving()) {
            super.update(delta);
        }
        else {
            if (!this.isDestroyed()){
                behaviour(delta);
            }
        }
    }

    /**
     * specifies the behaviour of a COMEnemy
     * @param delta
     */
    public void behaviour(double delta) {}

    /**
     * Accept method of the visitor pattern.
     */
    public void accept(Visitor v) {
        v.visit(this);
    }
}
