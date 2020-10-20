package pp.network;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server for establishing and managing connections to clients
 *
 * @param <S> the type of messages sent by the server
 * @param <R> the type of messages received by the server
 */
public class Server<S, R> implements Runnable, IServer<R, IConnection<S>> {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private final int maxClients;
    private final Set<IConnection<S>> connections = new HashSet<>();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private MessageReceiver<R, IConnection<S>> receiver;
    private ServerSocket serverSocket;

    /**
     * Creates a new server
     *
     * @param socket     the server socket used by this server
     * @param receiver   the object that is notified when this server receives a message from a client
     * @param maxClients the maximum number of accepted clients
     */
    public Server(ServerSocket socket, MessageReceiver<R, IConnection<S>> receiver, int maxClients) {
        this(socket, maxClients);
        this.receiver = receiver;
    }

    /**
     * Creates a new server, but does not set a receiver for incoming messages.
     *
     * @param socket     the server socket used by this server
     * @param maxClients the maximum number of accepted clients
     * @see #setReceiver(MessageReceiver)
     */
    public Server(ServerSocket socket, int maxClients) {
        this.maxClients = maxClients;
        this.serverSocket = socket;
    }

    public MessageReceiver<R, IConnection<S>> getReceiver() {
        return receiver;
    }

    public void setReceiver(MessageReceiver<R, IConnection<S>> receiver) {
        this.receiver = receiver;
    }

    /**
     * Returns whether or not the server is online, i.e., clients can connect to it.
     */
    public boolean isOnline() {
        return serverSocket != null;
    }

    @Override
    public void receiveMessage(R message, IConnection<S> connection) {
        receiver.receiveMessage(message, connection);
    }

    @Override
    public synchronized void onConnectionClosed(IConnection<S> connection) {
        connections.remove(connection);
        receiver.onConnectionClosed(connection);
    }

    /**
     * Broadcasts a message to all connected clients
     *
     * @param msg Message to be sent
     */
    public synchronized void broadcastMessage(S msg) {
        LOGGER.info("Broadcasting message: " + msg);

        //Send message to all clients
        for (IConnection<S> connection : connections)
            connection.send(msg);
    }

    /**
     * Accepts connections from clients up to the maximum number.
     * This method does not return before {@linkplain #shutdown()} has been called.
     */
    @Override
    public void run() {
        if (serverSocket == null)
            LOGGER.severe("Trying to run server thread without a server socket");
        else {
            LOGGER.info("SERVER: service established, connections can be accepted.");
            while (serverSocket != null)
                try {
                    final Socket clientSocket = serverSocket.accept();
                    clientSocket.setSoTimeout(1000);
                    acceptConnection(clientSocket);
                }
                catch (InterruptedIOException e) {
                    // everything is just fine... try again!
                }
                catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "when server listens to server socket: " + e.getMessage(), e);
                }
            LOGGER.info("SERVER: out of service, connections cannot be accepted any longer.");
            shutdown();
        }
    }

    /**
     * Accepts a new connection
     *
     * @param socket the socket for communicating with the client
     */
    private synchronized void acceptConnection(Socket socket) throws IOException {
        if (connections.size() < maxClients) {
            final Connection<S, R> connection = new Connection<>(socket, this);
            connections.add(connection);
            threadPool.execute(connection);
        }
    }

    /**
     * Shuts down this server and all its managed clients. Calling this method terminates a current
     * method call to {@linkplain #run()}.
     */
    public synchronized void shutdown() {
        for (IConnection<?> connection : connections)
            connection.shutdown();
        connections.clear();
        threadPool.shutdown();
        if (serverSocket != null)
            try {
                serverSocket.close();
            }
            catch (IOException e) {
                LOGGER.log(Level.SEVERE, "when closing the socket: " + e.getMessage(), e);
            }
        serverSocket = null;
    }

    /**
     * Returns the IP address of the machine, which hosts the server
     *
     * @return the IP address
     */
    public String getIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            LOGGER.log(Level.SEVERE, "failed to get IP address from server", e);
            return "<unknown host>";
        }
    }

    /**
     * Returns the port, which is used by the server
     *
     * @return the current port
     */
    public int getPort() {
        return serverSocket.getLocalPort();
    }
}
