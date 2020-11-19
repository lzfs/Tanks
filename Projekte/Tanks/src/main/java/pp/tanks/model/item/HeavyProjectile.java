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
        super(model, 0.25, 30, 5.0, data);
        this.targetPos = data.getTargetPos();
        this.flag = 1;
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

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param delta time in seconds since the last update call
     */
    @Override
    public void update(long serverTime) {
        interpolateTime(serverTime);
       /* if (flag > 0) {
            flag -= delta;
        }
        if (flag < 0) {
            flag = 0;
        }

        data.setPos(data.getPos().add(data.getDir().mult(delta * speed)));
        */
        if (getPos().distance(targetPos) <= 0.3) {
            setPos(targetPos);
        }
        if (getPos().x == targetPos.x && getPos().y == targetPos.y) {
            this.effectiveRadius = 2;
            collision();
            destroy();
        }
    }


    /**
     * Checks if the projectile hits an obstacle or an enemy. Projectiles are destroyed that way.
     */
    public void processHits() {}

    /**
     * TODO add fitting JavaDoc
     */
    public void collision() {
        for (Tank tank : model.getTanksMap().getAllTanks()) {
            if (collisionWith(tank, getPos()) && flag == 0) {
                tank.processDamage(damage);
                destroy();
                return;
            }
        }
        for (BreakableBlock bblock : model.getTanksMap().getBreakableBlocks()) {
            if (collisionWith(bblock, getPos())) {
                bblock.reduce(damage);
                destroy();
                return;
            }
        }
        destroy();
    }
    /*
    public void processHits() {

        for (Tank tank : model.getTanksMap().getTanks()) {
            if (collisionWith(tank) && flag == 0) {
                tank.processDamage(damage);
                destroy();
                return;
            }
        }
        for (BreakableBlock bblock : model.getTanksMap().getBreakableBlocks()) {
            if (collisionWith(bblock)) {
                bblock.reduce(damage);
                destroy();
                return;
            }
        }
        for (ReflectableBlock rBlock : model.getTanksMap().getReflectable()) {
            if (collisionWith(rBlock)) {
                reflect();
                return;
            }
        }
        for (UnbreakableBlock uBlock : model.getTanksMap().getUnbreakableBlocks()) {
            if (collisionWith(uBlock)) {
                destroy();
            }
        }
    }

         */
}
