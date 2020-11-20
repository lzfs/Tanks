package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a NormalTurret
 */
public class NormalTurret extends Turret {
    public NormalTurret() {
        super(10, 2, 3, 3, ItemEnum.NORMAL_PROJECTILE);
    }

    @Override
    public Projectile mkProjectile(Model model, ProjectileData data, DoubleVec target) {
        return new NormalProjectile(model, data);
    }

    @Override
    public int getBounces() {
        return 3;
    }
}
