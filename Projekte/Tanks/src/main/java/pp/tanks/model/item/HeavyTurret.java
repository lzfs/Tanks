package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a HeavyTurret
 */
public class HeavyTurret extends Turret {
    public HeavyTurret() {
        super(0, 5, 1, 5, ItemEnum.HEAVY_PROJECTILE);
    }

    @Override
    public Projectile mkProjectile(Model model, ProjectileData data, DoubleVec target) {
        return new HeavyProjectile(model, data, target);
    }

    @Override
    public int getBounces() {
        return 0;
    }
}
