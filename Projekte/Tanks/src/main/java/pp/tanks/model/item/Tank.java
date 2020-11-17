package pp.tanks.model.item;

import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.tanks.message.data.TankData;
import pp.tanks.notification.TanksNotification;
import pp.util.DoubleVec;

/**
 * abstract base class of all tanks in a {@linkplain pp.tanks.model.TanksMap}
 */
public abstract class Tank extends Item<TankData> {
    protected Turret turret;
    protected Armor armor;
    protected double speed;
    protected double rotationSpeed = 150;
    private int lives = 1;
    public final PlayerEnum playerEnum;
    private int projectileId;

    protected Tank(Model model, double effectiveRadius, Armor armor, Turret turret, TankData data) {
        super(model, 0.8, data);
        this.armor = armor;
        this.turret = turret;
        this.speed = calculateSpeed();
        this.playerEnum = PlayerEnum.getPlayer(data.getId());
        this.projectileId = playerEnum.projectileID;
    }

    public void decreaseLives() {
        this.lives -= 1;
    }

    public int getLives() {
        return this.lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * checks if the tank is moving
     *
     * @return move-boolean
     */
    public boolean isMoving() {
        return data.isMoving();
    }

    public Turret getTurret() {
        return this.turret;
    }

    public Armor getArmor() {
        return armor;
    }

    /**
     * updates the move of a tank
     *
     * @param move
     */
    public void setMove(boolean move) {
        data.setMove(move);
    }

    /**
     * updates the rotation of the turret
     *
     * @param rotation
     */
    public void setRotation(double rotation) {
        data.setRotation(rotation % 180);
    }

    public MoveDirection getMoveDir() {
        return data.getMoveDir();
    }

    /**
     * updates the MoveDirection
     *
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
        data.setMove(false);
    }

    public void setDestroyed(boolean bool) {
        this.data.setDestroyed(bool);
    }

    /**
     * updates the movement of a tank
     *
     * @param delta
     */
    public void updateMove(double delta) {
        DoubleVec newPos = getPos().add(getMoveDir().getVec().mult(delta * speed));
        collide(newPos);
        if (isMoving() && !data.isDestroyed()) {
            double currentRot = data.getRotation() % 180;
            double moveDirRotation = data.getMoveDir().getRotation();
            System.out.println("currentRot " + currentRot);
            System.out.println("moveDirRot " + moveDirRotation);
            double tmp = (currentRot - moveDirRotation + 180) % 180;
            double tmp1 = (moveDirRotation - currentRot + 180) % 180;
            System.out.println("tmp " + tmp);
            System.out.println("tmp1 " + tmp1);
            double tmp2 = Math.abs(currentRot - moveDirRotation) % 180; //TODO
            if (tmp2 < 4) {
                data.setRotation(moveDirRotation);
                setPos(newPos);
            }
            else if (tmp > tmp1) {
                data.setRotation(currentRot + delta * rotationSpeed);
            }
            else {
                data.setRotation(currentRot - delta * rotationSpeed);
            }
        }
    }

    /**
     * Accept method of the visitor pattern.
     */
    public abstract void accept(Visitor v);

    /**
     * shooting a projectile in the direction of the cursor
     *
     * @param pos
     */
    public void shoot(DoubleVec pos) {
        if (canShoot() && !this.isDestroyed()) {
            turret.shoot();
            Projectile projectile = makeProjectile(pos);
            model.getTanksMap().addProjectile(projectile);
        }
    }

    /**
     * checks if the tank is able to shoot
     *
     * @return flag for shoot-possibility
     */
    protected boolean canShoot() {
        return turret.canShoot();
    }

    /**
     * decides which type of projectile the tank is shooting by type of turret it's using
     *
     * @param targetPos Position of the target-cursor
     * @return projectile
     */
    private Projectile makeProjectile(DoubleVec targetPos) {
        if (projectileId == playerEnum.projectileID + 999) projectileId = playerEnum.projectileID;
        DoubleVec dir = targetPos.sub(this.getPos()).normalize();
        DoubleVec position = this.getPos().add(dir.mult(1.01));
        ProjectileData data = new ProjectileData(position, projectileId + 1, turret.getBounces(), dir, turret.projectileType);
        model.notifyReceivers(TanksNotification.TANK_FIRED);
        return turret.mkProjectile(this.model, data, targetPos);
        /*
        if (turret instanceof LightTurret) {
            return new LightProjectile(model, 0.3, turret.getDamage(), 4, data);  //TODO
        }
        else if (turret instanceof NormalTurret) {
            return new NormalProjectile(model, 0.3, turret.getDamage(), 4, data); //TODO
        }
        else if (turret instanceof HeavyTurret) {
            return new HeavyProjectile(model, 0.3, turret.getDamage(), 4, targetPos, data); //TODO
        }
        return new LightProjectile(model, 0.3, turret.getDamage(), 4, data);*/
    }

    private double calculateSpeed() {
        return (25.0 / (armor.getWeight() + turret.getWeight()));
    }

    /**
     * handles the movement of the tank if it collides with other tanks or blocks in the map
     */
    private void collide(DoubleVec pos) {
        for (Tank tank : model.getTanksMap().getAllTanks()) {
            if (this != tank && collisionWith(tank, pos)) {
                setPos(getPos().sub(getMoveDir().getVec().mult(0.01)));
                setMove(false);
                return;
            }
        }
        for (Block block : model.getTanksMap().getBlocks()) {
            if (collisionWith(block, pos)) {
                setMove(false);
                return;
            }
        }
    }


    /**
     * reduces armor points if the tank was hit by a projectile
     *
     * @param damage incoming damage-data
     */
    public void processDamage(int damage) {
        if (armor.getArmorPoints() - damage <= 0) {
            armor.setArmorPoints(0);
            destroy();
        }
        else {
            armor.takeDamage(damage);
        }
        data.setLifePoints(armor.getArmorPoints());
    }
}
