package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

import java.awt.geom.Ellipse2D;

/**
 * abstract base class for all types of projectiles
 */
public abstract class Projectile extends Item<ProjectileData> {
    protected final int damage;
    protected final Double speed;
    protected final ProjectileData data;
    protected double flag;

    public Projectile(Model model, double effectiveRadius, int damage, Double speed, ProjectileData data) {
        super(model, effectiveRadius, data);
        this.data = data;
        this.damage = damage;
        this.speed = speed;
        this.flag = 0.5;
        for (Block i : model.getTanksMap().getBlocks()) {
            if (collisionWith(i, getPos())) {
                destroy();
            }
        }
    }

    /**
     * reflect the projectile and gives it a new direction
     */
    public void reflect() {
        int i = 0;
        while (!collisionWith(model.getTanksMap().getReflectable().get(i), getPos())) {
            i++;
        }
        ReflectableBlock rBlock = model.getTanksMap().getReflectable().get(i);
        double width = ((double) rBlock.getWidth()) / 2;
        double height = ((double) rBlock.getHeight()) / 2;
        if (getPos().x >= rBlock.getPos().x + width || getPos().x <= rBlock.getPos().x - width) {
            //right and left
            setDir(new DoubleVec(getDir().x * (-1), getDir().y));
        }
        else if (getPos().y >= rBlock.getPos().y + height || getPos().y <= rBlock.getPos().y - height) {
            //above and below
            setDir(new DoubleVec(getDir().x, getDir().y * (-1)));
        }
        else {
            setDir(new DoubleVec(getDir().x * (-1), getDir().y * (-1)));
        }
    }

    public void setDir(DoubleVec dir) {
        data.setDir(dir);
    }

    public DoubleVec getDir() {
        return data.getDir();
    }

    public int getDamage() {
        return this.damage;
    }

    public ProjectileData getProjectileData() {
        return this.data;
    }

    /**
     * Updates the projectile
     *
     * @param delta delta
     */
    @Override
    public void update(double delta) {
        data.setPos(data.getPos().add(data.getDir().mult(delta * speed)));
        if (flag > 0) {
            flag -= delta;
        }
        if (flag < 0) {
            flag = 0;
        }
    }

    /**
     * Checks if the projectile hits an obstacle or an enemy. Projectiles are destroyed that way.
     */
    public void processHits() {
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
        for (ReflectableBlock rBlock : model.getTanksMap().getReflectable()) {
            if (collisionWith(rBlock, getPos())) {
                if (data.getBounce() > 0) {
                    reflect();
                    data.bounced();
                }
                else {
                    destroy();
                }
                return;
            }
        }
        for (UnbreakableBlock uBlock : model.getTanksMap().getUnbreakableBlocks()) {
            if (collisionWith(uBlock, getPos())) {
                destroy();
            }
        }

        for (Projectile p : model.getTanksMap().getProjectiles()) {
            if (p != this && collisionWith(p, getPos())) {
                destroy();
                p.destroy();
                return;
            }
        }
    }

    /**
     * Accept method of the visitor pattern.
     */
    public abstract void accept(Visitor v);
}
