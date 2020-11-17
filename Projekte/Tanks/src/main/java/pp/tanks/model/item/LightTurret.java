package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a LightTurret
 */
public class LightTurret extends Turret {
    public LightTurret() {
        super(5, 5, 5,1, ItemEnum.LIGHT_PROJECTILE);
    }

    @Override
    public Projectile mkProjectile(Model model, ProjectileData data, DoubleVec target) {
        return new LightProjectile(model, data);
    }

    @Override
    public int getBounces() {
        return 2;
    }
}
