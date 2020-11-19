package pp.tanks.model.item;

import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a enemy played by the computer
 */
public class COMEnemy extends Enemy {
    public final PlayerEnum player;

    protected COMEnemy(Model model, double effectiveRadius, Armor armor, Turret turret, TankData data) {
        super(model, effectiveRadius, armor, turret, data);
        if (model.getEngine() == null) this.player = PlayerEnum.PLAYER1;
        else this.player = model.getEngine().getPlayerEnum();
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
     * @param delta time in seconds since the last update call
     */
    @Override
    public void update(long serverTime) {
        /*turret.update(delta);
        if (isMoving()) {
            //hier
            super.update(delta);
        }
        else {
            if (!this.isDestroyed()){
                behaviour(delta);
            }
        }*/
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
}
