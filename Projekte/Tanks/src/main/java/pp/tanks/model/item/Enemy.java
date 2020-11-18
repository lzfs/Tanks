package pp.tanks.model.item;

import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;
import pp.tanks.notification.TanksNotification;
import pp.util.DoubleVec;

/**
 * Represents a Enemy; could be a COMEnemy or a Enemy played by another person
 */
public class Enemy extends Tank {
    private double animationTime;
    private DoubleVec targetPos;

    protected Enemy(Model model, double effectiveRadius, Armor armor, Turret turret, TankData data) {
        super(model, effectiveRadius, armor, turret, data);
    }

    /**
     * method for test cases
     */
    @Override
    public void isVisible() {
        //TODO
    }

    /**
     * Accept method of the visitor pattern.
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    /**
     * Called once per frame. Updates the enemy
     *
     * @param delta time in seconds since the last update call
     */
    @Override
    public void update(double delta) {
        super.update(delta);
    }

    /**
     * Indicates that this enemy has been destroyed.
     */
    @Override
    public void destroy() {
        super.destroy();
        model.notifyReceivers(TanksNotification.TANK_DESTROYED);
    }

    /** Method for creating a EnemyTank Instance
     *
     * @param turret
     * @param armor
     */
    public static Enemy mkEnemyTank(Model model, ItemEnum turret, ItemEnum armor, TankData data) {
        Turret ergTurret = Turret.mkTurret(turret);
        Armor ergArmor = Armor.mkArmor(armor);
        return new Enemy(model,1, ergArmor, ergTurret, data);
    }
}
