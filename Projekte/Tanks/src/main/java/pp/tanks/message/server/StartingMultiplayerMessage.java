package pp.tanks.message.server;

import pp.tanks.message.data.TankData;
import pp.tanks.model.item.ItemEnum;

/**
 * sends the client the enums of their own, and the enemy tank
 */
public class StartingMultiplayerMessage implements IServerMessage {
    public final ItemEnum enemyTurret;
    public final ItemEnum enemyArmor;
    public final TankData playerTank;
    public final TankData enemyTank;

    public StartingMultiplayerMessage(ItemEnum turret, ItemEnum armor, TankData playerTank, TankData enemyTank) {
        this.enemyTank = enemyTank;
        this.playerTank = playerTank;
        this.enemyArmor = armor;
        this.enemyTurret = turret;
    }

    /**
     * Method to accept a visitor
     *
     * @param interpreter visitor to be used
     */
    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}
