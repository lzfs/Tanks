package pp.tanks.server;

import pp.tanks.message.client.IClientMessage;
import pp.network.IServer;

public class ServerStub implements IServer<IClientMessage, ConnectionStub> {
    private TanksServer server;

    public TanksServer getServer() {
        return server;
    }

    public void setServer(TanksServer server) {
        this.server = server;
    }

    @Override
    public void shutdown() {
        // do nothing
    }

    @Override
    public void receiveMessage(IClientMessage message, ConnectionStub conn) {
        server.receiveMessage(message, conn);
    }

    @Override
    public void onConnectionClosed(ConnectionStub conn) {
        server.onConnectionClosed(conn);
    }
}
