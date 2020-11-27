package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;

/**
 * message sent when a tank is selected a tank
 */
public class TankSelectedMessage implements IClientMessage {

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
}
