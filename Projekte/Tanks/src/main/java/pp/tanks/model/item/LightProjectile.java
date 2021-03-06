package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;

/**
 * Represents a LightProjectile
 */
public class LightProjectile extends Projectile {

    public LightProjectile(Model model, ProjectileData data) {
        super(model, 10, 6.0, data);
    }

    /**
     * Method to accept a visitor
     *
     * @param v visitor to be used
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "LightProjectile";
    }
}
