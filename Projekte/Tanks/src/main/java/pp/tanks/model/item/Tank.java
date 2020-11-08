package pp.tanks.model.item;


import pp.tanks.model.Model;
import pp.tanks.message.data.TankData;
import pp.util.DoubleVec;

/**
 * abstract base class of all tanks in a {@linkplain pp.tanks.model.TanksMap}
 */
public abstract class Tank extends Item {
    protected Turret turret;
    protected Armor armor;
    protected TankData data;
    protected double speed;

    protected Tank(Model model, double effectiveRadius, Armor armor, Turret turret) {
        super(model, effectiveRadius);
        this.armor = armor;
        this.turret = turret;
        data = new TankData(new DoubleVec(1,1), 1, armor.getArmorPoints());
    }

    /**
     * checks if the tank is moving
     * @return
     */
    public boolean isMoving() {
        return data.isMoving();
    }

    /**
     * updates the move of a tank
     * @param move
     */
    public void setMove(boolean move) {
        data.setMove(move);
    }

    /**
     * updates the rotation of the turret
     * @param rotation
     */
    public void setRotation(double rotation) {
        data.setRotation(rotation);
    }

    public MoveDirection getMoveDir() {
        return data.getMoveDir();
    }

    /**
     * updates the MoveDirection
     * @param dir
     */
    public void setMoveDirection(MoveDirection dir) {
        data.setMoveDir(dir);
    }

    public double getSpeed() {
        return speed;
    }

    public double getRotation() {
        return data.getRotation();
    }

    @Override
    public void setPos(DoubleVec pos) {
        super.setPos(pos);
        data.setPos(pos);
    }

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param delta time in seconds since the last update call
     */
    public void update(double delta) {
        turret.update(delta);
        updateMove(delta);
    }

    /**
     * updates the movement of a tank
     * @param delta
     */
    public void updateMove(double delta) {
        if (isMoving()) {
            setPos(getPos().add(getMoveDir().getVec().mult(delta)));
            collide();
        }
    }

    /**
     * Accept method of the visitor pattern.
     */
    public abstract void accept(Visitor v);

    /**
     * shooting a projectile in the direction of the cursor
     * @param pos
     */
    public void shoot(DoubleVec pos) {
        if(canShoot()) {
            turret.shoot();
            makeProjectile(pos);
        }
    }

    /**
     * checks if the tank is able to shoot
     * @return
     */
    protected boolean canShoot() {
        return turret.canShoot();
    }

    /**
     * decides which type of projectile the tank is shooting by type of turret it's using
     * @param targetPos
     * @return
     */
    private Projectile makeProjectile(DoubleVec targetPos) {
        //model.notifyReceivers(DroidsNotification.DROID_FIRED);
        final DoubleVec dir = DoubleVec.polar(1., getRotation()); //?
        if (turret instanceof LightTurret) {
            return new LightProjectile(model, 1, turret.getDamage(), 4, this.getPos());
        }
        else if (turret instanceof NormalTurret) {
            return new NormalProjectile(model, 1,turret.getDamage(), 2, this.getPos());
        }
        else if (turret instanceof HeavyTurret) {
            return new HeavyProjectile(model, 5, turret.getDamage(), 1, this.getPos(), targetPos);
        }
        return null;
    }

    /**
     * handles the movement of the tank if it collides with other tanks or blocks in the map
     */
    private void collide() {
        for (Tank tank : model.getTanksMap().getTanks()) {
            if (collisionWith(tank)) {
                setMove(false);
                return;
            }
        }
        for (Block block : model.getTanksMap().getBreakableBlocks()) {
            if (collisionWith(block)) {
                setMove(false);
                return;
            }
        }
    }

    /**
     * reduces armor points if the tank was hitted by a projectile
     * @param damage
     */
    public void processDamage(int damage) {
        if (armor.getArmorPoints() - damage <= 0) {
            armor.setArmorPoints(0);
            destroy();
        }
        else {
            armor.takeDamage(damage);
        }
        data.setLifepoints(armor.getArmorPoints());
    }
}
