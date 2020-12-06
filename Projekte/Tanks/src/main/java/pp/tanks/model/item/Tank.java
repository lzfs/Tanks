package pp.tanks.model.item;

import pp.tanks.message.client.ShootMessage;
import pp.tanks.message.client.TurretUpdateMessage;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.data.ProjectileData;
import pp.tanks.model.Model;
import pp.tanks.message.data.TankData;
import pp.tanks.notification.TanksNotification;
import pp.util.DoubleVec;

import java.util.ArrayList;
import java.util.List;

import static pp.tanks.model.item.MoveDirection.*;

/**
 * abstract base class of all tanks in a {@linkplain pp.tanks.model.TanksMap}
 */
public abstract class Tank extends Item<TankData> {
    protected List<Track> tracksPosList = new ArrayList<Track>();
    protected Turret turret;
    protected Armor armor;
    protected double speed;
    protected double rotationSpeed = 150;
    private int lives = 1;
    public final PlayerEnum playerEnum;
    private int projectileId;
    private DataTimeItem<TankData> latestOp;
    private double trackRotation = 0.0;
    protected double buffer;
    private int counter = 0;

    protected Tank(Model model, Armor armor, Turret turret, TankData data) {
        super(model, armor.getEffectiveRadius(), data);
        this.armor = armor;
        this.turret = turret;
        this.speed = calculateSpeed();
        this.playerEnum = PlayerEnum.getPlayer(data.getId());
        this.projectileId = playerEnum.projectileID;
        this.buffer = 0.25;
        latestOp = new DataTimeItem<>(data.mkCopy(), System.nanoTime());
        tracksPosList.add(new Track(data.getPos(), data.getRotation(), TrackIntensity.NORMAL));
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
     * @return the list with the positions we want to draw
     */
    public List<Track> getTracksPosList() {
        return tracksPosList;
    }

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
            double tmp2 = Math.abs(currentRot - moveDirRotation) % 180;
            if (tmp2 < 4) {
                data.setRotation(moveDirRotation);
                if (!collide(newPos)) {
                    setPos(newPos);
                    addTrack();
                }
            } else if (tmp > tmp1) {
                data.setRotation(currentRot + delta * rotationSpeed);
                addTrackRotation();
            } else {
                data.setRotation(currentRot - delta * rotationSpeed);
                addTrackRotation();
            }
        }
        oilCollision();
    }

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
            if (model.getEngine() != null && !model.getEngine().isClientGame()) {
                ShootMessage msg = new ShootMessage(new DataTimeItem<ProjectileData>(projectile.data, System.nanoTime() + model.getEngine().getOffset()));
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
    public boolean collide(DoubleVec pos) {
        for (Tank tank : model.getTanksMap().getAllTanks()) {
            if (this != tank && collisionWith(tank, pos, buffer)) {
                //setPos(getPos().sub(getMoveDir().getVec().mult(0.01)));
                stopMovement();
                setMove(false);
                return true;
            }
        }
        for (Block block : model.getTanksMap().getBlocks()) {
            if (collisionWith(block, pos, buffer)) {
                stopMovement();
                setMove(false);
                return true;
            }
        }
        return false;
    }

    /**
     * Process the collision with an oil item
     */
    public void oilCollision() {
        for (Oil oil : model.getTanksMap().getOilList()) {
            if (getPos().distance(oil.getPos()) <= effectiveRadius + oil.effectiveRadius) {
                counter = 20;
                return;
            }
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
            if (model.getEngine() != null) model.getEngine().notify(TanksNotification.ARMOR_HIT);
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
     */
    @Override
    public void interpolateTime(long time) {
        if (latestOp == null || latestOp.data.getMoveDir().equals(STAY)) return;
        double latestSec = FACTOR_SEC * latestOp.serverTime;
        double deltaT = FACTOR_SEC * (time - latestOp.serverTime);

        double latestRot = latestOp.data.getRotation();
        double moveDirRotation = latestOp.data.getMoveDir().getRotation();

        double turnRightAngle = (latestRot - moveDirRotation + 180) % 180;
        double turnLeftAngle = (moveDirRotation - latestRot + 180) % 180;
        boolean turnLeft = turnRightAngle > turnLeftAngle;
        double turnBy = Math.min(turnRightAngle, turnLeftAngle);
        double tTime = ((turnBy + latestSec * rotationSpeed) / rotationSpeed) - latestSec;

        if (tTime > deltaT) {
            if (turnLeft) data.setRotation(latestRot + deltaT * rotationSpeed);
            else data.setRotation(latestRot - deltaT * rotationSpeed);
            addTrackRotation();
        } else {
            double rest = deltaT - tTime;
            data.setRotation(moveDirRotation);
            data.setPos(latestOp.getPos().add(latestOp.data.getMoveDir().getVec().mult(rest * speed)));
            addTrack();
            oilCollision();
        }
    }

    /**
     * Add a new Track to a List of Track named posList
     */
    public void addTrack() {
        DoubleVec refPos = getPos().sub(getMoveDir().getVec().mult(0.2));
        if (tracksPosList.size() == 0) {
            tracksPosList.add(new Track(getPos(), getRotation(), TrackIntensity.NORMAL));
        }
        if (Math.abs(refPos.distance(tracksPosList.get(tracksPosList.size() - 1).getVec())) > 0.3) {
            if (counter > 0) {
                tracksPosList.add(new Track(refPos, data.getRotation(), TrackIntensity.OIL));
                counter--;
            } else {
                tracksPosList.add(new Track(refPos, data.getRotation(), TrackIntensity.NORMAL));
            }
            trackRotation = getRotation();
            if (tracksPosList.size() > 50) tracksPosList.remove(0);
        }
    }

    /**
     * Added the rotation of a track
     */
    public void addTrackRotation() {
        if (tracksPosList.size() == 0) {
            tracksPosList.add(new Track(getPos(), getRotation(), TrackIntensity.NORMAL));
        }
        if (Math.abs(getRotation() - trackRotation) > 22.4) {
            if (counter > 0) {
                tracksPosList.add(new Track(getPos(), data.getRotation(), TrackIntensity.OIL));
                counter--;
            } else {
                tracksPosList.add(new Track(getPos(), data.getRotation(), TrackIntensity.NORMAL));
            }
            trackRotation = getRotation();
        }
        if (tracksPosList.size() > 50) tracksPosList.remove(0);
    }

    /**
     * Indicates that this item has been destroyed.
     */
    @Override
    public void destroy() {
        data.destroy();
        counter = 0;
    }

    public void sendTurretUpdate() {

    }
}
