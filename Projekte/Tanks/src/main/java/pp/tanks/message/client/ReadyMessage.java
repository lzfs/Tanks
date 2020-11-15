package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;

/**
 * message sent when a client clicking "ready"
 */
public class ReadyMessage implements IClientMessage {

    /**
     * Method to accept a visitor
     *
     * @param interpreter       visitor to be used
     * @param from              the connectionID
     */
    @Override
    public void accept(IClientInterpreter interpreter, IConnection<IServerMessage> from) {

    }
}
