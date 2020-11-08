package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.server.IServerMessage;

public class ShootMessage implements IClientMessage {
    public final DataTimeItem dataTime;

    public ShootMessage(DataTimeItem dataTime) {
        this.dataTime = dataTime;
    }

    @Override
    public void accept(IClientInterpreter interpreter, IConnection<IServerMessage> from) {
        interpreter.visit(this, from);
    }
}
