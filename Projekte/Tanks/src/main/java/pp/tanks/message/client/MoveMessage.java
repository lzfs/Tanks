package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.data.TankData;
import pp.tanks.message.server.IServerMessage;

/**
 * message sent when a tank is moving
 */
public class MoveMessage implements IClientMessage {
    public final DataTimeItem<TankData> dataTime;

    public MoveMessage(DataTimeItem<TankData> dataTime) {
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
        return "MoveMessage: " + dataTime;
    }
}
