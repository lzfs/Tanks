package pp.network;

/**
 * Interface for servers that shall be notified when a message over a connection is received
 * or the connection gets closed.
 *
 * @param <M> the type of messages received over a connection
 * @param <C> the type of the connection that receives messages of type M
 */
public interface IServer<M, C> extends MessageReceiver<M, C> {
    /**
     * Shuts down this transmitter.
     */
    void shutdown();
}
