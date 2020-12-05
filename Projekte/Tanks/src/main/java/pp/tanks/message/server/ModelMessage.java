package pp.tanks.message.server;

import pp.tanks.message.data.BBData;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.data.ProjectileData;
import pp.tanks.message.data.TankData;

import java.util.List;

/**
 * represents all of the changes and updates
 */
public class ModelMessage implements IServerMessage {
    public final List<DataTimeItem<TankData>> tanks;
    public final List<DataTimeItem<ProjectileData>> projectile;
    public final List<BBData> blocks;

    public ModelMessage(List<DataTimeItem<TankData>> tanks, List<DataTimeItem<ProjectileData>> projectile, List<BBData> blocks) {
        this.tanks = tanks;
        this.projectile = projectile;
        this.blocks = blocks;
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
