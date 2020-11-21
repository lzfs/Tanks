package pp.tanks.model.item;

import pp.tanks.message.data.TankData;
import pp.tanks.model.AStar;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a enemy played by the computer
 */
public class COMEnemy extends Enemy {
    public final PlayerEnum player1 = PlayerEnum.PLAYER1;
    private long latestViewUpdate;

    private List<DoubleVec> path;
    private List<MoveDirection> dirs;

    protected COMEnemy(Model model, double effectiveRadius, Armor armor, Turret turret, TankData data) {
        super(model, effectiveRadius, armor, turret, data);
        if (model.getEngine() != null) latestViewUpdate = System.nanoTime() + model.getEngine().getOffset();
        else latestViewUpdate = System.nanoTime();
        this.path = new ArrayList<>();
        this.dirs = new ArrayList<>();
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
                // behaviour(delta);
            }
            //}
        }
        else interpolateTime(serverTime);

        latestViewUpdate = serverTime;

        setMove(true);
        if (!path.isEmpty() && !dirs.isEmpty()) {
            updateMove(delta);
            // setPos(getPos().add(getMoveDir().getVec().mult(delta * speed)));
            if (getPos().distance(path.get(0)) < 0.5) {
                setPos(path.get(0));
                //setMoveDir()
                setMoveDirection(dirs.get(0));
                path.remove(0);
                dirs.remove(0);
            }
        }
        else if (path == null || path.isEmpty()) {
            setMove(false);
            if (!shootIsBlocked()) {
                shoot(model.getTanksMap().getTank(PlayerEnum.PLAYER1).getPos());
            }
            double r = Math.random() * 4;
            r -= 2;
            r = Math.toIntExact(Math.round(r));
            int x = Math.toIntExact(Math.round(model.getTanksMap().getTank(PlayerEnum.PLAYER1).getPos().x + r));
            int y = Math.toIntExact(Math.round(model.getTanksMap().getTank(PlayerEnum.PLAYER1).getPos().y + r));
            if (x != getPos().x || y != getPos().y) {
                navigate(x, y);
                setMoveDirection(dirs.get(0));
            }
        }
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

    public void navigate(int x, int y) {
        //TODO
        path.clear();
        dirs.clear();
        int panzerX = Math.toIntExact(Math.round(getPos().x));
        int panzerY = Math.toIntExact(Math.round(getPos().y));
        int width = model.getTanksMap().getWidth();
        int height = model.getTanksMap().getHeight();

        int n = model.getTanksMap().getBlocks().size() + model.getTanksMap().getAllTanks().size() - 1;
        int[][] blocked = new int[n][2];

        int i = 0;
        while (i < model.getTanksMap().getBlocks().size()) {
            blocked[i][1] = Math.toIntExact(Math.round(model.getTanksMap().getBlocks().get(i).getPos().x));
            blocked[i][0] = Math.toIntExact(Math.round(model.getTanksMap().getBlocks().get(i).getPos().y));
            i++;
        }

        int j = 0;
        while (i < n) {
            if (model.getTanksMap().getAllTanks().get(j) != this) {
                blocked[i][1] = Math.toIntExact(Math.round(model.getTanksMap().getAllTanks().get(j).getPos().x));
                blocked[i][0] = Math.toIntExact(Math.round(model.getTanksMap().getAllTanks().get(j).getPos().y));
                i++;
            }
            j++;
        }


        path = AStar.execute(height, width, panzerY, panzerX, y, x, blocked);
        Collections.reverse(path);

        List<DoubleVec> tmpPath = new ArrayList<>();
        for (DoubleVec p : path) {
            tmpPath.add(new DoubleVec(p.y, p.x));
        }
        path = tmpPath;

        dirs = AStar.getDirsList(path);
        Collections.reverse(dirs);

        // path.remove(0);
    }

    /**
     * test if a ComEnemy can hit the players Tank if he would shoot
     *
     * @return if the way is blocked
     */
    public boolean shootIsBlocked() {
        DoubleVec pos = model.getTanksMap().getTank(PlayerEnum.PLAYER1).getPos();
        DoubleVec dir = getPos().sub(pos).normalize().mult(0.5);
        while (pos.distance(getPos()) < 0.1) {
            for (Block other : model.getTanksMap().getBlocks()) {
                Block block = (Block) other;
                Ellipse2D item1 = new Ellipse2D.Double(pos.x - (effectiveRadius / 2), pos.y - (effectiveRadius / 2), effectiveRadius, effectiveRadius);
                if (item1.intersects(other.getPos().x - (block.getWidth() / 2.0),
                                     other.getPos().y - (block.getHeight() / 2.0), block.getWidth(), block.getHeight())) {
                    return true;
                }
                pos = pos.add(dir);
            }
        }
        return false;
    }
}
