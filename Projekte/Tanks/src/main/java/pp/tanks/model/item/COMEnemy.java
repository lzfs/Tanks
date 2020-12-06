package pp.tanks.model.item;

import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;
import pp.tanks.model.item.navigation.Navigator;
import pp.tanks.notification.TanksNotification;
import pp.util.DoubleVec;
import pp.util.IntVec;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a enemy played by the computer
 */
public class COMEnemy extends Enemy {
    public final PlayerEnum player1 = PlayerEnum.PLAYER1;
    private long latestViewUpdate;
    private boolean shootable = true;

    protected List<DoubleVec> path;
    private List<MoveDirection> dirs;

    protected COMEnemy(Model model, Armor armor, Turret turret, TankData data) {
        super(model, armor, turret, data);
        if (model.getEngine() != null) latestViewUpdate = System.nanoTime() + model.getEngine().getOffset();
        else latestViewUpdate = System.nanoTime();
        this.path = new LinkedList<>();
        this.dirs = new ArrayList<>();
    }

    /**
     * shoots a projectile
     *
     * @param pos target-position
     */
    @Override
    public void shoot(DoubleVec pos) {
        if (canShoot() && !this.isDestroyed() && shootable) {
            turret.shoot();
            Projectile projectile = super.makeProjectile(pos);
            model.getTanksMap().addProjectile(projectile);
        }
    }

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param serverTime the synced nanotime of the server
     */
    @Override
    public void update(long serverTime) {
        if (!model.getEngine().isTutorial()) {
            long tmp = serverTime - latestViewUpdate;
            double delta = FACTOR_SEC * tmp;
            turret.update(delta);
            data.setTurretDir(model.getTanksMap().get(0).getData().getPos().sub(data.getPos()));
            if (model.getEngine() != null) {
                if (isMoving()) {
                    if (!collide(getPos().add(getMoveDir().getVec().normalize().mult(speed * delta)))) {
                        updateMove(delta);
                    } else {
                        path.clear();
                    }
                } else {
                    if (!this.isDestroyed()) {
                        behaviour(delta);
                    }
                }
            }
        }
        latestViewUpdate = serverTime;
    }

    /**
     * @param vec only if this double Vector is a Vector associated in the MoveDirection Enum
     * @return the MoveDirection to a given DoubleVector
     */
    public static MoveDirection getMoveDirToVec(DoubleVec vec) {
        if (vec.equals(MoveDirection.UP.getVec())) {
            return MoveDirection.UP;
        } else if (vec.equals(MoveDirection.DOWN.getVec())) {
            return MoveDirection.DOWN;
        } else if (vec.equals(MoveDirection.LEFT.getVec())) {
            return MoveDirection.LEFT;
        } else if (vec.equals(MoveDirection.RIGHT.getVec())) {
            return MoveDirection.RIGHT;
        } else if (vec.equals(MoveDirection.RIGHT_DOWN.getVec())) {
            return MoveDirection.RIGHT_DOWN;
        } else if (vec.equals(MoveDirection.RIGHT_UP.getVec())) {
            return MoveDirection.RIGHT_UP;
        } else if (vec.equals(MoveDirection.LEFT_DOWN.getVec())) {
            return MoveDirection.LEFT_DOWN;
        } else if (vec.equals(MoveDirection.LEFT_UP.getVec())) {
            return MoveDirection.LEFT_UP;
        } else {
            return null;
        }
    }

    /**
     * updates the movement of a tank
     *
     * @param delta
     */
    public void updateMove(double delta) {
        double currentRot = data.getRotation() % 180;
        double moveDirRotation = data.getMoveDir().getRotation();
        double tmp = (currentRot - moveDirRotation + 180) % 180;
        double tmp1 = (moveDirRotation - currentRot + 180) % 180;
        double tmp2 = Math.abs(currentRot - moveDirRotation) % 180;
        if (tmp2 < 5) {
            setRotation(moveDirRotation);
            if (path != null && !path.isEmpty()) {
                final DoubleVec target = path.get(0);
                if (getPos().distance(target) < 0.05) {
                    setPos(target);
                    path.remove(0);
                    if (path.size() >= 1) {
                        setMoveDirection(getMoveDirToVec(path.get(0).sub(target)));
                    }
                } else {
                    setPos(getPos().add(getMoveDir().getVec().normalize().mult(speed * delta)));
                    addTrack();
                }
            } else {
                setMove(false);
            }
        } else if (tmp > tmp1) {
            data.setRotation(currentRot + delta * rotationSpeed);
            addTrackRotation();
        } else {
            data.setRotation(currentRot - delta * rotationSpeed);
            addTrackRotation();
        }
        oilCollision();
    }

    /**
     * specifies the behaviour of a COMEnemy
     *
     * @param delta
     */
    public void behaviour(double delta) {
    }

    /**
     * Method to accept a visitor
     *
     * @param v visitor to be used
     */
    public void accept(Visitor v) {
        v.visit(this);
    }

    /**
     * creates a new COM-Enemy
     *
     * @param type  type of Enemy
     * @param model correct model
     * @param data  additional data
     * @return the enemy
     */
    public static COMEnemy mkComEnemy(ItemEnum type, Model model, TankData data) {
        if (type == ItemEnum.ACP) return new ArmoredPersonnelCarrier(model, data);
        else if (type == ItemEnum.TANK_DESTROYER) return new TankDestroyer(model, data);
        else return new Howitzer(model, data);
    }

    /**
     * test if a ComEnemy can hit the players Tank if he would shoot
     *
     * @return if the way is blocked
     */
    public boolean shootIsBlocked() {
        DoubleVec pos = model.getTanksMap().getTank(PlayerEnum.PLAYER1).getPos();
        DoubleVec dir = getPos().sub(pos).normalize().mult(0.1);
        while (pos.distance(getPos()) > 0.1) {
            for (Block other : model.getTanksMap().getBlocks()) {
                Block block = other;
                Ellipse2D item1 = new Ellipse2D.Double(pos.x - (effectiveRadius / 2), pos.y - (effectiveRadius / 2), effectiveRadius, effectiveRadius);
                if (item1.intersects(other.getPos().x - (block.getWidth() / 2.0), other.getPos().y - (block.getHeight() / 2.0), block.getWidth(), block.getHeight())) {
                    return false;
                }
            }
            pos = pos.add(dir);
        }
        return true;
    }

    /**
     * Searches for an optimal, collision-free path to the specified position and moves the droid there.
     *
     * @param target point to go
     */
    public void navigateTo(DoubleVec target) {
        path.clear();
        Navigator<IntVec> navigator = new TanksNavigator(model.getTanksMap(), getPos().toIntVec(), target.toIntVec());
        List<IntVec> pPath = navigator.findPath();
        if (pPath != null) {
            for (IntVec v : pPath)
                path.add(v.toFloatVec());
            if (path.size() > 1) path.remove(0);
        }
        setMoveDirection(MoveDirection.LEFT);

        setPos(new DoubleVec(Math.round(getPos().x), Math.round(getPos().y)));
        if (path != null && !path.isEmpty()) {
            setMoveDirection(getMoveDirToVec(path.get(0).sub(getPos())));
            setMove(true);
        }
    }

    /**
     * Indicates that this enemy has been destroyed.
     */
    @Override
    public void destroy() {
        data.destroy();
        path.clear();
        model.notifyReceivers(TanksNotification.TANK_DESTROYED);
        setMoveDirection(MoveDirection.STAY);
    }

    /**
     * @param pos the position to check
     * @return if pos is within the map
     */
    public boolean isWithinMap(DoubleVec pos) {
        return !(model.getTanksMap().getHeight() > pos.y) && model.getTanksMap().getHeight() >= 0 && !(model.getTanksMap().getWidth() > pos.x) && model.getTanksMap().getWidth() >= 0;
    }

    public void setShootable(boolean shootable) {
        this.shootable = shootable;
    }

    /**
     * Reset the Interpolation
     */
    public void resetInterpolateTime() {
        latestViewUpdate = System.nanoTime();
    }
}
