package pp.tanks.model.item;

import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

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
                if (!this.isDestroyed()){
                    behaviour(delta);
                }
            //}
        }

        else interpolateTime(serverTime);
        latestViewUpdate = serverTime;
    }

    /**
     * specifies the behaviour of a COMEnemy
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

    public static COMEnemy mkComEnemy(ItemEnum type, Model model, TankData data) {
        if (type == ItemEnum.ACP) return new ArmoredPersonnelCarrier(model, data);
        else if (type == ItemEnum.TANK_DESTROYER) return new TankDestroyer(model, data);
        else return new Howitzer(model, data);
    }
}
