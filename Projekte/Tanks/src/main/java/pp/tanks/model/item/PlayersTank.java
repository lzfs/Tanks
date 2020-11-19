package pp.tanks.model.item;

import pp.tanks.message.client.MoveMessage;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;

/**
 * Represents the tank of the current player
 */
public class PlayersTank extends Tank{
    private long latestViewUpdate;


    public PlayersTank(Model model, double effectiveRadius, Armor armor, Turret turret, TankData data) {
        super(model, effectiveRadius, armor, turret, data);
        setLives(3);
        if (getLatestOp() != null) latestViewUpdate = getLatestOp().serverTime;
        else System.nanoTime();
    }

    /**
     * method for test cases
     */
    @Override
    public void isVisible() {
        //TODO
    }

    /**
     * Method to accept a visitor
     *
     * @param v visitor to be used
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public void update(long serverTime) {
        long tmp = (serverTime - latestViewUpdate);
        double delta = ((double) tmp) / FACTOR_SEC;
        latestViewUpdate = serverTime;
        turret.update(delta);
        updateMove(delta);
        data.setMove(false);
    }

    /**
     *
     * @param turret
     * @param armor
     */
    public static PlayersTank mkPlayersTank(Model model, ItemEnum turret, ItemEnum armor, TankData data) {
        Turret ergTurret = Turret.mkTurret(turret);
        Armor ergArmor = Armor.mkArmor(armor);
        return new PlayersTank(model,1, ergArmor, ergTurret, data);
    }

    @Override
    public void stopMovement() {
        data.setMoveDir(MoveDirection.STAY);
        DataTimeItem<TankData> item = new DataTimeItem<TankData>(data, System.nanoTime() + model.getEngine().getOffset());
        model.getEngine().getConnection().send(new MoveMessage(item));
    }

    @Override
    public void setMoveDirection(MoveDirection dir) {
        if (dir != data.getMoveDir()) {
            super.setMoveDirection(dir);
            DataTimeItem<TankData> item = new DataTimeItem<TankData>(data, System.nanoTime() + model.getEngine().getOffset());
            model.getEngine().getConnection().send(new MoveMessage(item));
        }
        else super.setMoveDirection(dir);
    }
}
