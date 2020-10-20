package pp.network;

/**
 * Interface of objects that shall be notified when a message over a connection
 * is received or the connection gets closed.
 *
 * @param <M> the message type
 * @param <C> the type of the connection that receives messages of type M
 */
public interface MessageReceiver<M, C> {
    /**
     * This method is executed when a message has been received.
     *
     * @param message the message object
     * @param conn    the connection that received the message
     */
    void receiveMessage(M message, C conn);

    /**
     * Executed when a connection gets closed
     *
     * @param conn the connection that has been closed
     */
    void onConnectionClosed(C conn);
}
