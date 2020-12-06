package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a NormalProjectile
 */
public class NormalProjectile extends Projectile {

    public NormalProjectile(Model model, ProjectileData data) {
        super(model,20, 6.0, data);
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
}
