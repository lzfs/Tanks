package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.tanks.message.data.Data;
import pp.tanks.message.data.ProjectileData;
import pp.util.DoubleVec;

/**
 * abstract base class for all types of projectiles
 */
public abstract class Projectile extends Item {
    protected final int damage;
    protected final Double speed;
    protected final ProjectileData data;

    public Projectile(Model model, double effectiveRadius, int damage, Double speed, DoubleVec pos) {
        super(model, effectiveRadius);
        this.data = new ProjectileData(pos, 0, 0);
        this.damage = damage;
        this.speed = speed;
    }

    /**
     * reflect the projectile and gives it a new direction
     */
    public void reflect() {
        //TODO
        data.setDir(data.getDir().mult(-1));
    }

    public int getDamage() {
        return this.damage;
    }

    /**
     * Updates the projectile
     *
     * @param delta delta
     */
    @Override
    public void update(double delta) {
        data.setPos(data.getPos().add(data.getDir().mult(delta * speed)));
    }

    /**
     * Checks if the projectile hits an obstacle or an enemy. Projectiles are destroyed that way.
     */
    public void processHits() {
        for (Tank tank : model.getTanksMap().getTanks()) {
            if (collisionWith(tank)) {
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
            }
        }
        for (UnbreakableBlock uBlock : model.getTanksMap().getUnbreakableBlocks()) {
            if (collisionWith(uBlock)) {
                destroy();
            }
        }
    }

    /**
     * Accept method of the visitor pattern.
     */
    public abstract void accept(Visitor v);
}
