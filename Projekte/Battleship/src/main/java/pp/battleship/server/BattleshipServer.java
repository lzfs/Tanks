package pp.battleship.server;

import pp.battleship.Resources;
import pp.battleship.message.client.ClickHarborMessage;
import pp.battleship.message.client.ClickOpponentMapMessage;
import pp.battleship.message.client.ClickOwnMapMessage;
import pp.battleship.message.client.ClientInterpreter;
import pp.battleship.message.client.ClientMessage;
import pp.battleship.message.client.ClientReadyMessage;
import pp.battleship.message.client.ConfirmMessage;
import pp.battleship.message.client.ReadyMessage;
import pp.battleship.message.client.RemoveMessage;
import pp.battleship.message.client.RotateMessage;
import pp.battleship.message.server.ServerMessage;
import pp.battleship.server.auto.BattleshipAutomaton;
import pp.network.IConnection;
import pp.network.IServer;
import pp.network.MessageReceiver;
import pp.network.Server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Server implementing the visitor pattern as MessageReceiver for ClientMessages
 */
public class BattleshipServer implements MessageReceiver<ClientMessage, IConnection<ServerMessage>>, ClientInterpreter {
    private static final int DEFAULT_PORT = 1234;
    private final Model model;
    private final BattleshipAutomaton auto = new BattleshipAutomaton(this);
    private IServer<ClientMessage, ? extends IConnection<ServerMessage>> server;

    /**
     * Starts the battleships server.
     */
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(getPort(args));
            serverSocket.setSoTimeout(1000);
            Server<ServerMessage, ClientMessage> server = new Server<>(serverSocket, 2);
            Config config = new Config();
            BattleshipServer bs = new BattleshipServer(config, server);
            server.setReceiver(bs);
            server.run();
        }
        catch (IllegalArgumentException | IOException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }

    /**
     * Returns the port
     */
    private static int getPort(String[] args) {
        if (args.length == 0)
            return DEFAULT_PORT;
        else if (args.length == 1)
            try {
                return Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e) {
                System.err.println(Resources.getString("invalid.port.number") + " " + args[0]);
                System.exit(1);
            }
        System.err.println(Resources.getString("usage.call.without.parameter.or.with.port.number"));
        System.exit(1);
        return -1;
    }

    /**
     * Creates the server.
     *
     * @param server the transmitter used to send and receive messages
     */
    BattleshipServer(Config config, IServer<ClientMessage, ? extends IConnection<ServerMessage>> server) {
        this.server = server;
        model = new Model(config);
    }

    /**
     * Interprets the messages
     *
     * @param msg  the message
     * @param conn the connection that received the message
     */
    @Override
    public void receiveMessage(ClientMessage msg, IConnection<ServerMessage> conn) {
        msg.accept(this, conn);
    }

    /**
     * Method called on loss of connection
     *
     * @param conn the connection that has been closed
     */
    @Override
    public void onConnectionClosed(IConnection<ServerMessage> conn) {
        System.exit(0);
    }

    /**
     * Shuts down the server.
     */
    public void shutdown() {
        if (server != null)
            server.shutdown();
        server = null;
    }

    /**
     * Sends full map to the player's client
     *
     * @param p the player to receive the map
     */
    public void sendGameOverMap(Player p) {
        p.getConnection().send(p.makeGameOverMapModel());
    }

    /**
     * Sends map to the player's client
     *
     * @param p the player to receive the map
     */
    public void sendMap(Player p) {
        p.getConnection().send(p.makeModel());
    }

    @Override
    public void visit(ClickOwnMapMessage msg, IConnection<ServerMessage> from) {
        auto.clickOwnMap(model.getPlayer(from), msg.pos);
    }

    @Override
    public void visit(ClickHarborMessage msg, IConnection<ServerMessage> from) {
        auto.clickHarbor(model.getPlayer(from), msg.pos);
    }

    @Override
    public void visit(RemoveMessage msg, IConnection<ServerMessage> from) {
        auto.remove(model.getPlayer(from));
    }

    @Override
    public void visit(RotateMessage msg, IConnection<ServerMessage> from) {
        auto.rotate(model.getPlayer(from));
    }

    @Override
    public void visit(ClickOpponentMapMessage msg, IConnection<ServerMessage> from) {
        auto.clickOpponentMap(model.getPlayer(from), msg.pos);
    }

    @Override
    public void visit(ReadyMessage msg, IConnection<ServerMessage> from) {
        auto.ready(model.getPlayer(from));
    }

    @Override
    public void visit(ConfirmMessage msg, IConnection<ServerMessage> from) {
        auto.confirm(model.getPlayer(from));
    }

    @Override
    public void visit(ClientReadyMessage msg, IConnection<ServerMessage> from) {
        auto.playerConnected(from);
    }

    /**
     * Returns the model
     *
     * @return the model
     */
    public Model getModel() {
        return model;
    }
}

