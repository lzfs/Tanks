package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a LightProjectile
 */
public class LightProjectile extends Projectile {

    public LightProjectile(Model model, double effectiveRadius, int damage, double speed, ProjectileData data) {
        super(model, effectiveRadius, damage, speed,data);
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

    @Override
    public String toString() { return "Light";}
}
