package pp.tanks.model.item;

import pp.tanks.message.client.ShootMessage;
import pp.tanks.message.client.TurretUpdateMessage;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.tanks.message.data.TankData;
import pp.tanks.notification.TanksNotification;
import pp.util.DoubleVec;

import java.awt.geom.Ellipse2D;

import static pp.tanks.model.item.MoveDirection.*;

/**
 * abstract base class of all tanks in a {@linkplain pp.tanks.model.TanksMap}
 */
public abstract class Tank extends Item<TankData> {
    protected Turret turret;
    protected Armor armor;
    protected double speed;
    protected double rotationSpeed = 150;
    protected double turretRotationSpeed = 1000;
    private int lives = 1;
    public final PlayerEnum playerEnum;
    private int projectileId;
    private DataTimeItem<TankData> latestOp;
    private long latestInterpolate;
    private DoubleVec lastTurretDir = new DoubleVec(0, 0);
    protected DoubleVec newTurretDir = new DoubleVec(1, 0);

    protected Tank(Model model, double effectiveRadius, Armor armor, Turret turret, TankData data) {
        super(model, 1, data);
        this.armor = armor;
        this.turret = turret;
        this.speed = calculateSpeed();
        this.playerEnum = PlayerEnum.getPlayer(data.getId()); //TODO Com enemys beim schießen
        this.projectileId = playerEnum.projectileID;
        latestOp = new DataTimeItem<>(data.mkCopy(), System.nanoTime());
    }

    /**
     * reduces remaining lives by one
     */
    public void decreaseLives() {
        this.lives -= 1;
    }

    /**
     * @return remaining lives
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * updates lives
     *
     * @param lives new number of lives
     */
    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * @return latest operation as DataTimeItem
     */
    public DataTimeItem<TankData> getLatestOp() {
        return latestOp;
    }

    /**
     * updates latest operation
     *
     * @param latestOp new latest operation as DataTimeItem
     */
    public void setLatestOp(DataTimeItem<TankData> latestOp) {
        this.latestOp = latestOp;
    }

    /**
     * checks if the tank is moving
     *
     * @return move-boolean
     */
    public boolean isMoving() {
        return data.isMoving();
    }

    /**
     * @return used turret
     */
    public Turret getTurret() {
        return this.turret;
    }

    /**
     * @return used armor
     */
    public Armor getArmor() {
        return armor;
    }

    /**
     * updates the move of a tank
     *
     * @param move move-flag
     */
    public void setMove(boolean move) {
        data.setMove(move);
    }

    /**
     * updates the rotation of the turret
     *
     * @param rotation new roation
     */
    public void setRotation(double rotation) {
        data.setRotation(rotation % 180);
    }

    /**
     * @return current move-direction
     */
    public MoveDirection getMoveDir() {
        return data.getMoveDir();
    }

    /**
     * updates the MoveDirection
     *
     * @param dir new direction
     */
    public void setMoveDirection(MoveDirection dir) {
        data.setMoveDir(dir);
    }

    /**
     * @return current speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @return current rotation
     */
    public double getRotation() {
        return data.getRotation();
    }

    /**
     * updates postion
     *
     * @param pos the new position of the item
     */
    @Override
    public void setPos(DoubleVec pos) {
        super.setPos(pos);
        data.setPos(pos);
    }

    /**
     * Called once per frame. Used for updating this item's position etc.
     * TODO: what the heck is with this comment?
     * //@param delta time in seconds since the last update call
     */

    /**
     * updates destruction-status
     *
     * @param bool boolean of new status
     */
    public void setDestroyed(boolean bool) {
        this.data.setDestroyed(bool);
    }

    /**
     * updates the movement of a tank
     *
     * @param delta time
     */
    public void updateMove(double delta) {
        DoubleVec newPos = getPos().add(getMoveDir().getVec().mult(delta * speed));
        DoubleVec newPos2 = getPos().add(getMoveDir().getVec().mult(delta * speed * 2));

        if (isMoving() && !data.isDestroyed() && data.getMoveDir() != STAY) {
            double currentRot = data.getRotation() % 180;
            double moveDirRotation = data.getMoveDir().getRotation();
            double tmp = (currentRot - moveDirRotation + 180) % 180;
            double tmp1 = (moveDirRotation - currentRot + 180) % 180;
            double tmp2 = Math.abs(currentRot - moveDirRotation) % 180; //TODO
            if (tmp2 < 4) {
                data.setRotation(moveDirRotation);
                if (!collide(newPos)) {
                    setPos(newPos);
                }
            } else if (tmp > tmp1) {
                data.setRotation(currentRot + delta * rotationSpeed);
            } else {
                data.setRotation(currentRot - delta * rotationSpeed);
            }
        }
    }

    //haben latest OP
    //wenn nachricht an server
    //dataTime item an sich selbst geben
    //berechenen angefangen und wann fertig
    //delta größer als deltaT also zeit die ich bräuchte
    //iwas iwo abziehen

    //es kommt bewegunsgänderung
    //hat Data
    //und latestOP  (bräuchte er eigentlich nicht)
    //wenn stopmovement oder setmovedirection
    //das datatime item als latestOP abspeichern
    //delta winkel berechnen  (der zu drehende winkel)
    //wie lange ich bräuchte kann ich mir deltaT für dauer der drehung berechnen
    //deltaT abspeichern
    //iwo oben ne deltaZeit
    //deltaZeit+=delta
    //ist deltazeit kleiner als deltaT?
    //=> current rotation dreh dings
    //berechnen speichern
    //deltazeit=deltaT
    // deltazeit- deltaT=minizeit
    //setRotation auf moveDirdirection
    //setPosition(alte + direction * speed*minizeit

    /**
     * Method to accept a visitor
     *
     * @param v visitor to be used
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
            ShootMessage msg = new ShootMessage(new DataTimeItem<ProjectileData>(projectile.data, System.nanoTime() + model.getEngine().getOffset()));
            if (!model.getEngine().isClientGame()) {
                model.getEngine().getConnection().send(msg);
                model.getEngine().getConnection().send(new TurretUpdateMessage(data.id, data.getTurretDir()));
            }
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
    public Projectile makeProjectile(DoubleVec targetPos) {
        if (projectileId == playerEnum.projectileID + 999) projectileId = playerEnum.projectileID;
        DoubleVec dir = targetPos.sub(this.getPos()).normalize();
        DoubleVec position = this.getPos().add(dir.mult(1.01));
        ProjectileData data = new ProjectileData(position, projectileId + 1, turret.getBounces(), dir, targetPos, turret.projectileType, false);
        model.notifyReceivers(TanksNotification.TANK_FIRED);
        projectileId++;
        return turret.mkProjectile(this.model, data, targetPos);

    }

    /**
     * @return caluclated speed depending on armor- and turret- weight
     */
    private double calculateSpeed() {
        return (25.0 / (armor.getWeight() + turret.getWeight()));
    }

    /**
     * handles the movement of the tank if it collides with other tanks or blocks in the map
     */
    protected boolean collide(DoubleVec pos) {
        for (Tank tank : model.getTanksMap().getAllTanks()) {
            if (this != tank && collisionWith(tank, pos)) {
                //setPos(getPos().sub(getMoveDir().getVec().mult(0.01)));
                setMoveDirection(STAY);
                setMove(false);
                return true;
            }
        }
        for (Block block : model.getTanksMap().getBlocks()) {
            if (collisionWith(block, pos)) {
                setMoveDirection(STAY);
                setMove(false);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether there is a collision with another item
     *
     * @param other the item which is checked for a collision
     */
    @Override
    public boolean collisionWith(Item other, DoubleVec newPos) {
        if (getPos() == null || other.isDestroyed()) return false;

        double buffer = 0.05;

        if (other instanceof Block) {
            Block block = (Block) other;
            Ellipse2D item1 = new Ellipse2D.Double(newPos.x - (effectiveRadius / 2.0), newPos.y - (effectiveRadius / 2.0), effectiveRadius, effectiveRadius);
            return item1.intersects(other.getPos().x - ((block.getWidth() + buffer) / 2.0), other.getPos().y - ((block.getHeight() + buffer) / 2.0), block.getWidth() + buffer, block.getHeight() + buffer);
        } else {
            return newPos.distance(other.getPos()) <= effectiveRadius + other.effectiveRadius;
        }
    }

    /**
     * reduces armor points if the tank was hit by a projectile
     *
     * @param damage incoming damage-data
     */
    @Override
    public void processDamage(int damage) {
        if (armor.getArmorPoints() - damage <= 0) {
            armor.setArmorPoints(0);
            destroy();
        } else {
            armor.takeDamage(damage);
        }
        data.setLifePoints(armor.getArmorPoints());
    }

    /**
     * @return boolean-value if the tank is destroyed by incoming damage
     */
    public boolean processDestruction(int damage) {
        return armor.getArmorPoints() - damage <= 0;
    }

    /**
     * over written in PlayersTank
     */
    public void stopMovement() {
    }

    /**
     * makes a copy of the interpolating Data-object and overwrites the current data
     *
     * @param item represents the given DataTimeItem-object
     */
    @Override
    public void interpolateData(DataTimeItem<TankData> item) {
        this.data = item.data.mkCopy();
        this.latestOp = item;
    }

    /**
     * the interpolateTime methode calculates the time for a interpolated movement
     *
     * @param time for calculation
     * @return returns a boolean while the movement is calculated valid
     */
    @Override
    public boolean interpolateTime(long time) {
        if (latestOp == null || latestOp.data.getMoveDir().equals(STAY)) return false;  //TODO == statt equals
        long tmp = (time - latestOp.serverTime);
        double latestSec = ((double) latestOp.serverTime) / FACTOR_SEC; //TODO ggf. 10e^-9
        double deltaT = ((double) tmp) / FACTOR_SEC;
        if (model.getEngine() != null) tmp = tmp + model.getEngine().getAnimationTime();
        double latestRot = (latestOp.data.getRotation() + 180) % 180;
        double moveDirRotation = latestOp.data.getMoveDir().getRotation();

        double tmp0 = (latestRot - moveDirRotation + 180) % 180;
        double tmp1 = (moveDirRotation - latestRot + 180) % 180;

        if (tmp0 > tmp1) {
            double tFin = (tmp1 + latestSec * rotationSpeed) / rotationSpeed;
            double tTime = (tFin - latestSec);
            if (tTime > deltaT) {
                data.setRotation(latestRot + deltaT * rotationSpeed);
            } else {
                double rest = deltaT - tTime;
                data.setRotation(moveDirRotation);
                data.setPos(latestOp.getPos().add(latestOp.data.getMoveDir().getVec().mult(rest * speed)));
            }
        } else {
            double tFin = (tmp0 + latestSec * rotationSpeed) / rotationSpeed;
            double tTime = (tFin - latestSec);
            if (tTime > deltaT) {
                data.setRotation(latestRot - deltaT * rotationSpeed);
            } else {
                double rest = deltaT - tTime;
                data.setRotation(moveDirRotation);
                data.setPos(latestOp.getPos().add(latestOp.data.getMoveDir().getVec().mult(rest * speed)));
            }
        }

        //data.setPos(latestOp.getPos().add(latestOp.data.getMoveDir().getVec().mult(deltaT * speed)));
        latestInterpolate = time;
        return true;
    }

    public void sendTurretUpdate() {
    }
}
