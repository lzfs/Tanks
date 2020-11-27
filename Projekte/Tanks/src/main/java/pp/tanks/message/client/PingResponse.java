package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;

/**
 * class for testing the ping response time
 */
public class PingResponse implements IClientMessage {
    public final long nanoTime;

    public PingResponse(long nT) {
        this.nanoTime = nT;
    }

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

    @Override
    public String toString() {
        return "PingResponse: " + nanoTime;
    }
}
