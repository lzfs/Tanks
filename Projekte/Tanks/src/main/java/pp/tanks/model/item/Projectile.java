package pp.tanks.model.item;

import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * abstract base class for all types of projectiles
 */
public abstract class Projectile extends Item<ProjectileData> {
    private static final DoubleVec STAY = new DoubleVec(0, 0);
    protected final int damage;
    protected final Double speed;
    protected long flag;
    private DataTimeItem<ProjectileData> latestOp;
    private long latestInterpolate;

    public Projectile(Model model, double effectiveRadius, int damage, Double speed, ProjectileData data) {
        super(model, effectiveRadius, data);
        this.data = data;
        this.damage = damage;
        this.speed = speed;
        this.flag = System.nanoTime();
        if (model.getEngine() != null)
            latestOp = new DataTimeItem<>(data.mkCopy(), System.nanoTime() + model.getEngine().getOffset());
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
        setPos(getPos().add(getDir().mult(0.1)));
    }

    /**
     * updates direction
     *
     * @param dir new direction
     */
    public void setDir(DoubleVec dir) {
        data.setDir(dir);
    }

    /**
     * @return current direction
     */
    public DoubleVec getDir() {
        return data.getDir();
    }

    /**
     * @return damage
     */
    public int getDamage() {
        return this.damage;
    }

    /**
     * @return ProjectileData
     */
    public ProjectileData getProjectileData() {
        return this.data;
    }

    /**
     * @return DataTimeItem of last operation
     */
    public DataTimeItem<ProjectileData> getLatestOp() {
        return latestOp;
    }

    /**
     * updates latestOperation
     *
     * @param latestOp new latest operation as DataTimeItem
     */
    public void setLatestOp(DataTimeItem<ProjectileData> latestOp) {
        this.latestOp = latestOp;
    }

    /**
     * Updates the projectile
     *
     * @param serverTime time
     */
    @Override
    public void update(long serverTime) {
        interpolateTime(serverTime);//TODO was ist mit der Flag
        //data.setPos(data.getPos().add(data.getDir().mult(delta * speed)));
        if (System.nanoTime() - flag > 1000000) {
            flag = 0;
        }
    }

    /**
     * Checks if the projectile hits an obstacle or an enemy. Projectiles are destroyed that way.
     */
    public void processHits() {
        for (Tank tank : model.getTanksMap().getAllTanks()) {
            if (collisionWith(tank, getPos()) && flag == 0) {
                model.getTanksMap().notifyObs(this.data.id, tank.data.id, 0, damage, true, tank.processDestruction(damage), System.nanoTime());
                return;
            }
        }
        for (BreakableBlock bBlock : model.getTanksMap().getBreakableBlocks()) {
            if (collisionWith(bBlock, getPos())) {
                model.getTanksMap().notifyObs(this.data.id, bBlock.data.id, 0, damage, true, bBlock.processDestruction(damage), System.nanoTime());
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
                return;
            }
        }

        for (Projectile p : model.getTanksMap().getProjectiles()) {
            if (p != this && collisionWith(p, getPos()) && !(p instanceof HeavyProjectile)) {
                model.getTanksMap().notifyObs(this.data.id, p.data.id, 0, 0, true, true, System.nanoTime());
                return;
            }
        }
    }

    /**
     * Method to accept a visitor
     *
     * @param v visitor to be used
     */
    public abstract void accept(Visitor v);

    /**
     * methode for creating new projectiles with different types
     *
     * @param model model
     * @param data  data
     * @return returns the correct projectile
     */
    public static Projectile mkProjectile(Model model, ProjectileData data) {
        if (data.type == ItemEnum.LIGHT_PROJECTILE) return new LightProjectile(model, data);
        else if (data.type == ItemEnum.NORMAL_PROJECTILE) return new NormalProjectile(model, data);
        else return new HeavyProjectile(model, data);
    }

    /**
     * @return Player-Enumeration from id
     */
    public PlayerEnum getEnemy() {
        if (data.getId() <= 2000 && data.getId() >= 1000) return PlayerEnum.PLAYER2;
        else return PlayerEnum.PLAYER1;
    }

    @Override
    public void interpolateData(DataTimeItem<ProjectileData> item) {
        this.data = item.data.mkCopy();
        this.latestOp = item;
    }

    @Override
    public boolean interpolateTime(long time) {
        if (latestOp == null || latestOp.data.getDir().equals(STAY)) return false;
        long tmp = (time - latestOp.serverTime);
        double deltaT = ((double) tmp) / FACTOR_SEC;
        data.setPos(latestOp.getPos().add(latestOp.data.getDir().mult(deltaT * speed)));
        latestInterpolate = time;
        return true;
    }
}
