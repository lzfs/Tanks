package pp.battleship.message.client;

import pp.battleship.message.server.ServerMessage;
import pp.network.IConnection;

/**
 * Message which signalizes a removal of the selected ship
 */
public class RemoveMessage implements ClientMessage {
    /**
     * Method to accept a visitor
     *
     * @param interpreter       visitor to be used
     * @param from              the connectionID
     */
    @Override
    public void accept(ClientInterpreter interpreter, IConnection<ServerMessage> from) {
        interpreter.visit(this, from);
    }
}