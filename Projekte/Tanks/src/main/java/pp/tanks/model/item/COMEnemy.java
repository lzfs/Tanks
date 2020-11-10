package pp.tanks.model.item;

import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a enemy played by the computer
 */
public class COMEnemy extends Enemy{

    protected COMEnemy(Model model, double effectiveRadius, Armor armor, Turret turret, TankData data) {
        super(model, effectiveRadius, armor, turret, data);
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

    @Override
    public void update(double delta) {
        turret.update(delta);
        if(isMoving()) {
            super.update(delta);
        }
        else {
            behaviour();
        }
    }

    public void behaviour() {}

    /**
     * Accept method of the visitor pattern.
     */
    public void accept(Visitor v) {
        v.visit(this);
    }
}
