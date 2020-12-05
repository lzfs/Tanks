package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a HeavyProjectile that flies across the map an makes damage in an specified radius on the map
 */
public class HeavyProjectile extends Projectile {
    private DoubleVec targetPos;

    public HeavyProjectile(Model model, ProjectileData data) {
        super(model, 0.25, 30, 4.0, data);
        this.targetPos = data.getTargetPos();
    }

    /**
     * Accept method of the visitor pattern.
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param serverTime time in seconds since the last update call
     */
    @Override
    public void update(long serverTime) {
        interpolateTime(serverTime);
        if (getPos().distance(targetPos) <= 0.3) {
            setPos(targetPos);
            this.effectiveRadius = 1;
            collision();
            destroy();
        }
    }

    /**
     * Checks if the projectile hits an obstacle or an enemy. Projectiles are destroyed that way.
     */
    public void processHits() {}

    /**
     * Checks if HeavyProjectile collides with tank or block and process the damage then
     */
    public void collision() {
        for (Tank tank : model.getTanksMap().getAllTanks()) {
            if (collisionWith(tank, getPos())) {
                tank.processDamage(damage);
                destroy();
                return;
            }
        }
        for (BreakableBlock bBlock : model.getTanksMap().getBreakableBlocks()) {
            if (collisionWith(bBlock, getPos())) {
                bBlock.processDamage(damage);
                destroy();
                return;
            }
        }
        for (Projectile projectile : model.getTanksMap().getProjectiles()) {
            if (collisionWith(projectile, getPos())) {
                projectile.destroy();
                destroy();
                return;
            }
        }
        destroy();
    }
}
