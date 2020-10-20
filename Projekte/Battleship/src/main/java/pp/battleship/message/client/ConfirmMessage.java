package pp.battleship.message.client;

import pp.battleship.message.server.ServerMessage;
import pp.network.IConnection;

/**
 * Message used to exit the INVALID_PLACEMENT state
 */
public class ConfirmMessage implements ClientMessage {
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
