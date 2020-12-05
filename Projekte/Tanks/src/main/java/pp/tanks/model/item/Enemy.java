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

    protected Enemy(Model model, Armor armor, Turret turret, TankData data) {
        super(model, armor, turret, data);
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
     * @param serverTime the synced nanotime of the server
     */
    @Override
    public void update(long serverTime) {
        interpolateTime(serverTime);
        //addTrack();
    }

    /**
     * Indicates that this enemy has been destroyed.
     */
    @Override
    public void destroy() {
        super.destroy();
        model.notifyReceivers(TanksNotification.TANK_DESTROYED);
    }

    /**
     * Method for creating a EnemyTank Instance
     *
     * @param turret correct turret-type
     * @param armor correct armor-type
     */
    public static Enemy mkEnemyTank(Model model, ItemEnum turret, ItemEnum armor, TankData data) {
        Turret ergTurret = Turret.mkTurret(turret);
        Armor ergArmor = Armor.mkArmor(armor);
        return new Enemy(model, ergArmor, ergTurret, data);
    }
}
