package pp.tanks.message.server;

import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.data.ProjectileData;
import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;

import java.util.List;

public class ModelMessage implements IServerMessage {
    public final List<DataTimeItem<TankData>> tanks;
    public final List<DataTimeItem<ProjectileData>> projectile;

    public ModelMessage(List<DataTimeItem<TankData>> tanks, List<DataTimeItem<ProjectileData>> projectile) {
        this.tanks = tanks;
        this.projectile = projectile;
    }

    /**
     * Method to accept a visitor
     *
     * @param interpreter       visitor to be used
     */
    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}
