package pp.tanks.model.item;

import pp.tanks.message.data.TankData;
import pp.tanks.model.AStar;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

import java.util.ArrayList;

/**
 * Represents a enemy played by the computer
 */
public class COMEnemy extends Enemy {
    public final PlayerEnum player1 = PlayerEnum.PLAYER1;
    private long latestViewUpdate;

    protected COMEnemy(Model model, double effectiveRadius, Armor armor, Turret turret, TankData data) {
        super(model, effectiveRadius, armor, turret, data);
        if (model.getEngine() != null) latestViewUpdate = System.nanoTime() + model.getEngine().getOffset();
        else latestViewUpdate = System.nanoTime();
    }

    /**
     * method for test cases to check if the COMEnemy can shoot
     */
    public void cheatShoot() {
        //TODO
    }

    /**
     * method for test cases to check if the COMEnemy can move
     */
    public void cheatMove(DoubleVec pos) {
        //TODO
    }

    /**
     * shoots a projectile
     *
     * @param pos target-position
     */
    @Override
    public void shoot(DoubleVec pos) {
        if (canShoot() && !this.isDestroyed()) {
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
        long tmp = serverTime - latestViewUpdate;
        double delta = ((double) tmp) / FACTOR_SEC;
        turret.update(delta);
        if (model.getEngine() != null) {
            /*if (isMoving()) {
                //hier
                super.update(delta);
            }
            else {*/
            if (!this.isDestroyed()) {
                behaviour(delta);
            }
            //}
        }

        else interpolateTime(serverTime);
        latestViewUpdate = serverTime;
    }

    /**
     * specifies the behaviour of a COMEnemy
     *
     * @param delta
     */
    public void behaviour(double delta) {}

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

    /*
    public void navigate(double x, double y) throws Exception {
        //TODO
        double panzerX = getPos().x;
        double panzerY = getPos().y;
        double targetX = x;
        double targetY = y;
        int width = 5;
        int height = 5;
        //TODO: How to get Blocked list?? look in map
        int[][] blocked = new int[][]{{0,4},{2,2},{3,1},{3,3}};

        AStar aStar = new AStar();
        ArrayList<DoubleVec> path =  aStar.getPath(width, height, panzerX, panzerY, targetX, targetY, blocked);
        ArrayList<MoveDirection> dirs = aStar.getDirsList(path);

        //TODO: Create Message to Server
        setMove(true);
        if(!path.isEmpty() && !dirs.isEmpty() &&getPos().distance(path.get(0)) < 0.1) {
            setPos(path.get(0));
            //setMoveDir()
            setMoveDirection(dirs.get(0));
            path.remove(0);
            dirs.remove(0);
        } else if(path.isEmpty()) {
            setMove(false);
        }
    }
     */
}
