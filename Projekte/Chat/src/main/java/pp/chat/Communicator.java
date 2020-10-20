package pp.chat;

import pp.network.Connection;
import pp.network.IConnection;
import pp.network.MessageReceiver;
import pp.network.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A class that allows to manage the client and, if necessary, the server
 */
class Communicator implements MessageReceiver<Message, IConnection<Message>> {
    private static final Logger LOGGER = Logger.getLogger(Communicator.class.getName());
    private Server<Message, Message> server;
    private Connection<Message, Message> connection;

    private final Map<IConnection<Message>, String> conn2Name = new HashMap<>();
    private final ChatApp app;
    private final boolean isServer;
    private final int serverPort;
    private final String name;
    private final String serverAddr;

    /**
     * Creates a new communicator for the specified chat application.
     *
     * @throws ChatException whenever some values set in the preferences are invalid
     */
    public Communicator(ChatApp app) throws ChatException {
        this.app = app;
        this.isServer = app.isServer();
        try {
            serverPort = Integer.parseInt(app.getServerPort());
        }
        catch (NumberFormatException e) {
            throw new ChatException("Bitte geben Sie einen korrekten Port an.");
        }

        name = app.getUserName().trim();
        if (name.isEmpty())
            throw new ChatException("Bitte geben Sie einen nicht-leeren Benutzernamen an.");

        serverAddr = isServer ? "localhost" : app.getServerAddress().trim();
        if (serverAddr.isEmpty())
            throw new ChatException("Bitte geben Sie eine nicht-leere Host-Adresse an.");
    }

    /**
     * Returns whether there is a client and whether it is connected to its server
     */
    public boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    /**
     * Returns the client
     */
    public IConnection<Message> getConnection() {
        return connection;
    }

    /**
     * Starts up the server, if necessary, and the client. If anything goes wrong, it is guaranteed
     * that neither client nor server are running afterwards. A Chat Exception is thrown then.
     *
     * @throws ChatException if anything goes wrong
     */
    public void start() throws ChatException {
        if (isServer)
            startHost();
        startClient();
    }

    /**
     * Shuts down the client and, if running, the server.
     */
    public void shutdown() {
        if (connection != null)
            connection.shutdown();
        if (server != null)
            server.shutdown();
        server = null;
        connection = null;
    }

    /**
     * Starts a new Client
     *
     * @throws ChatException if the client is not connected
     */
    private void startClient() throws ChatException {
        try {
            final Socket socket = new Socket(serverAddr, serverPort);
            socket.setSoTimeout(1000);
            connection = new Connection<>(socket, app);
            connection.send(new Message(name, name + " ist dem Chat beigetreten.", MessageType.CONNECTED));
            new Thread(connection).start();
        }
        catch (IllegalArgumentException | IOException e) {
            shutdown();
            throw new ChatException("Die Verbindung zum Server konnte nicht hergestellt werden.");
        }
    }

    /**
     * Starts the host
     *
     * @throws ChatException if the Server is not online
     */
    private void startHost() throws ChatException {
        try {
            final ServerSocket serverSocket = new ServerSocket(serverPort);
            serverSocket.setSoTimeout(1000);
            server = new Server<>(serverSocket, this, Integer.MAX_VALUE);
            new Thread(server, "SERVER").start();
        }
        catch (IllegalArgumentException | IOException e) {
            shutdown();
            throw new ChatException("Der Server konnte nicht gestartet werden.");
        }
    }

    @Override
    public void receiveMessage(Message message, IConnection<Message> conn) {
        switch (message.getType()) {
            case TEXT:
                server.broadcastMessage(message);
                break;
            case CONNECTED:
                server.broadcastMessage(message);
                setName(conn, message.getFrom());
                break;
            default:
                break;
        }
    }

    public void sendToServer(String msg) {
        connection.send(new Message(name, name + ": " + msg, MessageType.TEXT));
    }

    private void setName(IConnection<Message> conn, String from) {
        LOGGER.info(() -> "setting name of " + conn + " to " + name);
        conn2Name.put(conn, from);
    }

    @Override
    public void onConnectionClosed(IConnection<Message> conn) {
        LOGGER.info("connection closed: " + conn);
        if (server != null) {
            final String clientName = conn2Name.get(conn);
            server.broadcastMessage(new Message(clientName, clientName + " hat den Chat verlassen.",
                                                MessageType.DISCONNECTED));
        }
    }
}
