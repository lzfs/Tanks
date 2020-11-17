package pp.tanks.message.server;

import pp.tanks.message.data.DataTimeItem;
import pp.tanks.model.Model;

import java.util.List;

public class ModelMessage implements IServerMessage {
    public final List<DataTimeItem> tanks;
    public final List<DataTimeItem> projectile;

    public ModelMessage(List<DataTimeItem> tanks, List<DataTimeItem> projectile) {
        this.tanks = tanks;
        this.projectile = projectile;
    }

    @Override
    public void accept(IServerInterpreter interpreter) {

    }
}
