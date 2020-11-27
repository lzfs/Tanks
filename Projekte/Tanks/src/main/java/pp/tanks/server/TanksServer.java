package pp.tanks.server;

import pp.network.IConnection;
import pp.network.IServer;
import pp.network.MessageReceiver;
import pp.network.Server;
import pp.tanks.message.client.BackMessage;
import pp.tanks.message.client.ClientReadyMessage;
import pp.tanks.message.client.CollisionMessage;
import pp.tanks.message.client.CreateNewLobbyMessage;
import pp.tanks.message.client.IClientInterpreter;
import pp.tanks.message.client.IClientMessage;
import pp.tanks.message.client.JoinLobbyXMessage;
import pp.tanks.message.client.LevelSelectedMessage;
import pp.tanks.message.client.MoveMessage;
import pp.tanks.message.client.PingResponse;
import pp.tanks.message.client.ReadyMessage;
import pp.tanks.message.client.ShootMessage;
import pp.tanks.message.client.StartGameMessage;
import pp.tanks.message.client.TankSelectedMessage;
import pp.tanks.message.client.UpdateTankConfigMessage;
import pp.tanks.message.server.IServerMessage;
import pp.tanks.server.auto.TankAutomaton;

import java.io.IOException;
import java.net.ServerSocket;

public class TanksServer implements MessageReceiver<IClientMessage, IConnection<IServerMessage>>, IClientInterpreter {
    private static final int DEFAULT_PORT = 1234;
    private IServer<IClientMessage, ? extends IConnection<IServerMessage>> server;
    final TankAutomaton auto = new TankAutomaton(this);

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
            serverSocket.setSoTimeout(1000);
            Server<IServerMessage, IClientMessage> server = new Server<>(serverSocket, 2);
            TanksServer ts = new TanksServer(server);
            server.setReceiver(ts);
            server.run();
        }
        catch (IllegalArgumentException | IOException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }

    public TanksServer(IServer<IClientMessage, ? extends IConnection<IServerMessage>> server) {
        this.server = server;
    }

    /**
     * shuts the server down
     */
    public void shutdown() {
        if (server != null) {
            server.shutdown();
        }
        server = null;
    }

    @Override
    public void receiveMessage(IClientMessage message, IConnection<IServerMessage> conn) {
        //System.out.println("msg: " + message.toString() + " conn: " + conn);
        message.accept(this, conn);
    }

    @Override
    public void onConnectionClosed(IConnection<IServerMessage> conn) {
        auto.playerDisconnected(conn);
        TankAutomaton.LOGGER.info("Player disconnected");
    }

    @Override
    public void visit(ClientReadyMessage msg, IConnection<IServerMessage> from) {
        auto.playerConnected(msg, from);
    }

    @Override
    public void visit(PingResponse msg, IConnection<IServerMessage> from) {
        auto.pingResponse(msg, from);
    }

    @Override
    public void visit(CollisionMessage msg, IConnection<IServerMessage> from) {

    }

    @Override
    public void visit(CreateNewLobbyMessage msg, IConnection<IServerMessage> from) {

    }

    @Override
    public void visit(JoinLobbyXMessage msg, IConnection<IServerMessage> from) {

    }

    @Override
    public void visit(LevelSelectedMessage msg, IConnection<IServerMessage> from) {

    }

    @Override
    public void visit(MoveMessage msg, IConnection<IServerMessage> from) {
        auto.tankMove(msg, from);
    }

    @Override
    public void visit(ReadyMessage msg, IConnection<IServerMessage> from) {

    }

    @Override
    public void visit(ShootMessage msg, IConnection<IServerMessage> from) {
        auto.shoot(msg);
    }

    @Override
    public void visit(StartGameMessage msg, IConnection<IServerMessage> from) {
        auto.startGame(msg);
    }

    @Override
    public void visit(TankSelectedMessage msg, IConnection<IServerMessage> from) {

    }

    @Override
    public void visit(UpdateTankConfigMessage msg, IConnection<IServerMessage> from) {
        auto.updateTankConfig(msg);
    }

    @Override
    public void visit(BackMessage msg, IConnection<IServerMessage> from) {
        auto.back(from);
    }
}
