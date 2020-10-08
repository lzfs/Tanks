package pp.battleship.server;

import pp.battleship.message.client.ClientMessage;
import pp.network.IServer;

public class ServerStub implements IServer<ClientMessage, ConnectionStub> {
    private BattleshipServer server;

    public BattleshipServer getServer() {
        return server;
    }

    public void setServer(BattleshipServer server) {
        this.server = server;
    }

    @Override
    public void shutdown() {
        // do nothing
    }

    @Override
    public void receiveMessage(ClientMessage message, ConnectionStub conn) {
        server.receiveMessage(message, conn);
    }

    @Override
    public void onConnectionClosed(ConnectionStub conn) {
        server.onConnectionClosed(conn);
    }
}
