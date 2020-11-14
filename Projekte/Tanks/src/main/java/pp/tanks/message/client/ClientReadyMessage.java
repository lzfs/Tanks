package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;

/**
 * Message used to establish the connection to the server
 */
public class ClientReadyMessage implements IClientMessage {
    public final String message;

    public ClientReadyMessage(String msg) {
        this.message = msg;
    }

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
}
