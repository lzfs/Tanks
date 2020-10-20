package pp.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A connection that allows to send and receive messages.
 *
 * @param <S> the type of sent messages.
 * @param <R> the type of received messages.
 */
public class Connection<S, R> implements IConnection<S>, Runnable {
    private static final Logger LOGGER = Logger.getLogger(IConnection.class.getName());

    private static int ctr = 0;

    private final String clientId = "CONNECTION-" + (ctr++);
    private final MessageReceiver<R, IConnection<S>> receiver;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private Socket socket;

    /**
     * Creates a new connection
     *
     * @param socket   socket that represents the connection
     * @param receiver where messages received over this connection are dispatched to
     * @throws java.io.IOException if the data streams for this connection cannot be created
     */
    public Connection(Socket socket, MessageReceiver<R, IConnection<S>> receiver) throws IOException {
        this.receiver = receiver;
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Returns whether the connection is currently running
     */
    @Override
    public boolean isConnected() {
        return socket != null;
    }

    /**
     * The run method of the thread that listens to all incoming messages from the
     * other side of the connection and dispatches these messages.
     */
    @Override
    public void run() {
        if (socket == null) {
            LOGGER.severe("Trying to run thread without a connection being established");
            return;
        }
        try {
            LOGGER.info(clientId + ": connection established.");

            // ready to dispatch messages
            while (socket != null) {
                try {
                    dispatchMessage(in.readObject());
                }
                catch (SocketTimeoutException e) {
                    // everything is fine, just nothing received
                }
            }
        }
        catch (EOFException e) {
            // client no longer exists
            LOGGER.info("when trying to receive a message: connection closed by host");
        }
        catch (SocketException e) {
            LOGGER.info("when trying to receive a message: " + e.getMessage());
        }
        catch (IOException | ClassNotFoundException | ClassCastException e) {
            LOGGER.log(Level.SEVERE,
                       "while listening to socket: " + e.getMessage(),
                       e);
        }
        receiver.onConnectionClosed(this);
        shutdown();

        LOGGER.info(clientId + ": connection closed.");
    }

    /**
     * Dispatches the incoming message.
     *
     * @param msg message to be dispatched
     */
    @SuppressWarnings("unchecked")
    private void dispatchMessage(Object msg) {
        receiver.receiveMessage((R) msg, this);
    }

    /**
     * Sends a message over this connection
     */
    @Override
    public void send(S message) {
        try {
            out.writeObject(message);
            out.reset();
        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "when sending message: " + e.getMessage(), e);
        }
    }

    /**
     * Shuts down this connection
     */
    @Override
    public void shutdown() {
        if (socket == null) return;
        try {
            socket.close();
        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "when closing the socket: " + e.getMessage(), e);
        }
        socket = null;
    }

    @Override
    public String toString() {
        return clientId;
    }
}
