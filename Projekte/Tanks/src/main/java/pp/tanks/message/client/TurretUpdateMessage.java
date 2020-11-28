package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;
import pp.util.DoubleVec;

public class TurretUpdateMessage implements IClientMessage {
    public final int id;
    public final DoubleVec turDir;

    public TurretUpdateMessage(int id, DoubleVec dir) {
        this.id = id;
        this.turDir = dir;
    }

    @Override
    public void accept(IClientInterpreter interpreter, IConnection<IServerMessage> from) {
        interpreter.visit(this, from);
    }
}
