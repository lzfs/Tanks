package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;

import java.io.Serializable;

/**
 * Message interface for network transfer
 */
public interface IClientMessage extends Serializable {
    /**
     * Method to accept a visitor
     *
     * @param interpreter       visitor to be used
     * @param from              the connectionID
     */
    void accept(IClientInterpreter interpreter, IConnection<IServerMessage> from);
}
