package pp.network;

/**
 * The interface representing connections for sending messages.
 *
 * @param <S> the type of sent messages.
 */
public interface IConnection<S> {
    /**
     * Returns whether the connection is currently running
     */
    boolean isConnected();

    /**
     * Sends a message over this connection
     */
    void send(S message);

    /**
     * Shuts down this connection
     */
    void shutdown();
}
