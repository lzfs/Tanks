package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a HeavyProjectile that flies across the map an makes damage in an specified radius on the map
 */
public class HeavyProjectile extends Projectile {
    private DoubleVec targetPos;

    public HeavyProjectile(Model model, double effectiveRadius, int damage, double speed, DoubleVec pos, DoubleVec targetPos, ProjectileData data ){
        super(model, effectiveRadius, damage, speed,data);
    }

    /**
     * method for test cases
     */
    @Override
    public void isVisible() {}

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
     * @param delta time in seconds since the last update call
     */
    @Override
    public void update(double delta) {
        data.setPos(data.getPos().add(data.getDir().mult(delta * speed)));
        //TODO schiesst übers ziel hinaus
        if(data.getPos().equals(targetPos)) {
            processHits();
            destroy();
        }
    }

    /**
     * makes damage at a specified radius on the map
     */
    @Override
    public void processHits() {
        for (Tank tank : model.getTanksMap().getTanks()) {
            if (collisionWith(tank)) {
                tank.processDamage(damage);
                return;
            }
        }
        for (BreakableBlock bblock : model.getTanksMap().getBreakableBlocks()) {
            if (collisionWith(bblock)) {
                bblock.reduce(damage);
                return;
            }
        }
    }
}
