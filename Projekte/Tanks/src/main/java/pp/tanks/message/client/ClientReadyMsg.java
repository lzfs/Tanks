package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;

public class ClientReadyMsg implements IClientMessage {
    public final String nachricht;

    public ClientReadyMsg(String msg) {
        this.nachricht = msg;
    }
    @Override
    public void accept(IClientInterpreter interpreter, IConnection<IServerMessage> from) {
        interpreter.visit(this, from);
    }
}
