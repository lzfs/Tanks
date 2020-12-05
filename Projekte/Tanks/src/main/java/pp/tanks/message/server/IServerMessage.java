package pp.tanks.message.server;

import java.io.Serializable;

/**
 * interface for all Server Messages
 */
public interface IServerMessage extends Serializable {

    /**
     * Method to accept a visitor
     *
     * @param interpreter visitor to be used
     */
    void accept(IServerInterpreter interpreter);
}
