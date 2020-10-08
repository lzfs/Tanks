package pp.battleship.message.client;

import pp.battleship.message.server.ServerMessage;
import pp.network.IConnection;

import java.io.Serializable;

/**
 * Message interface for network transfer
 */
public interface ClientMessage extends Serializable {
    /**
     * Method to accept a visitor
     *
     * @param interpreter       visitor to be used
     * @param from              the connectionID
     */
    void accept(ClientInterpreter interpreter, IConnection<ServerMessage> from);
}
