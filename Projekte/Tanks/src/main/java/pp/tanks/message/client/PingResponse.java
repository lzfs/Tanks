package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;

public class PingResponse implements IClientMessage {
    public final long nanoTime;

    public PingResponse(long nT) {
        this.nanoTime = nT;
    }

    @Override
    public void accept(IClientInterpreter interpreter, IConnection<IServerMessage> from) {
        interpreter.visit(this, from);
    }
}
