package pp.tanks.model.item;

import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a NormalProjectile
 */
public class NormalProjectile extends Projectile {

    public NormalProjectile(Model model, double effectiveRadius, int damage, double speed, DoubleVec pos) {
        super(model, effectiveRadius, damage, speed, pos);
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
}
