package pp.battleship.server;

import pp.battleship.message.server.ServerMessage;
import pp.network.IConnection;

import java.util.ArrayList;
import java.util.List;

public class ConnectionStub implements IConnection<ServerMessage> {
    private final String name;
    private final List<ServerMessage> serverMessages = new ArrayList<>();

    public ConnectionStub(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<ServerMessage> getMessages() {
        return serverMessages;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void send(ServerMessage message) {
        serverMessages.add(message);
    }

    @Override
    public void shutdown() {
        // do nothing
    }
}
