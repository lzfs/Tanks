package pp.tanks.model.item;

import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a enemy played by the computer
 */
public class COMEnemy extends Enemy{

    protected COMEnemy(Model model, double effectiveRadius, Armor armor, Turret turret) {
        super(model, effectiveRadius, armor, turret);
    }

    /**
     * method for test cases to check if the COMEnemy can shoot
     */
    public void cheatShoot() {}

    /**
     * method for test cases to check if the COMEnemy can move
     */
    public void cheatMove(DoubleVec pos) {}

    public boolean blockShooting() {
        return false;
    }

    public void blockMovement() {}

    /**
     * Accept method of the visitor pattern.
     */
    public void accept(Visitor v) {
        v.visit(this);
    }
}
