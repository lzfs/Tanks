package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a NormalProjectile
 */
public class NormalProjectile extends Projectile {

    public NormalProjectile(Model model, ProjectileData data) {
        super(model, 0.3, 20, 4.0, data);
    }

    /**
     * method for test cases
     */
    @Override
    public void isVisible() {
        //TODO
    }

    /**
     * Accept method of the visitor pattern.
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
