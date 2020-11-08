package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.server.IServerMessage;

public class MoveMessage implements IClientMessage {
    public final DataTimeItem dataTime;

    public MoveMessage(DataTimeItem dataTime) {
        this.dataTime = dataTime;
    }


    @Override
    public void accept(IClientInterpreter interpreter, IConnection<IServerMessage> from) {
        interpreter.visit(this, from);
    }
}
