package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;

public class CreateNewLobbyMessage implements IClientMessage {
    @Override
    public void accept(IClientInterpreter interpreter, IConnection<IServerMessage> from) {

    }
}
