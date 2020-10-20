package pp.battleship.message.server;

import java.io.Serializable;

/**
 * Message interface for network transfer
 */
public interface ServerMessage extends Serializable {

    /**
     * Method to accept a visitor
     *
     * @param interpreter       visitor to be used
     */
    void accept(ServerInterpreter interpreter);
}
