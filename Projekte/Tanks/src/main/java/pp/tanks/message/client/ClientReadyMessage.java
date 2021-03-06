package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;
import pp.tanks.server.GameMode;

/**
 * Message used to establish the connection to the server
 */
public class ClientReadyMessage implements IClientMessage {

    public ClientReadyMessage() {}

    /**
     * Method to accept a visitor
     *
     * @param interpreter       visitor to be used
     * @param from              the connectionID
     */
    @Override
    public void accept(IClientInterpreter interpreter, IConnection<IServerMessage> from) {
        interpreter.visit(this, from);
    }

    @Override
    public String toString() {
        return "ClientReadyMessage:";
    }
}
