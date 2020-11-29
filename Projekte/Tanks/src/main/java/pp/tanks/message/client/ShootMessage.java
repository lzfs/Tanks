package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.data.ProjectileData;
import pp.tanks.message.server.IServerMessage;

/**
 * message sent when a tank is shooting
 */
public class ShootMessage implements IClientMessage {
    public final DataTimeItem<ProjectileData> dataTime;

    public ShootMessage(DataTimeItem<ProjectileData> dataTime) {
        this.dataTime = dataTime;
    }

    /**
     * Method to accept a visitor
     *
     * @param interpreter visitor to be used
     * @param from        the connectionID
     */
    @Override
    public void accept(IClientInterpreter interpreter, IConnection<IServerMessage> from) {
        interpreter.visit(this, from);
    }

    @Override
    public String toString() {
        return "ShootMessage: " + dataTime;
    }
}
