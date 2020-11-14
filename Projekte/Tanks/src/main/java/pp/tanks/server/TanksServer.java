package pp.tanks.server;

import pp.network.IConnection;
import pp.network.IServer;
import pp.network.MessageReceiver;
import pp.network.Server;
import pp.tanks.message.client.*;
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

    /*
     * Sends map to the player's client
     *
     * @param p the player to receive the map
     */ /* add this method after creating the makeModel methode in "Player"
    public void sendMap(Player p) {
        p.getConnection().send(p.makeModel());
    }
    */

    @Override
    public void receiveMessage(IClientMessage message, IConnection<IServerMessage> conn) {
        message.accept(this, conn);
    }

    @Override
    public void onConnectionClosed(IConnection<IServerMessage> conn) {
        System.exit(0);
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
    public void visit(MoveMessage msg,  IConnection<IServerMessage> from) {
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

    }

    @Override
    public void visit(TankSelectedMessage msg, IConnection<IServerMessage> from) {

    }

    @Override
    public void visit(UpdateTankConfigMessage msg, IConnection<IServerMessage> from) {

    }
}
