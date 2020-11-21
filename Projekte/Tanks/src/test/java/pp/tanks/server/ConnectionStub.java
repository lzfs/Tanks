package pp.tanks.server;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;

import java.util.ArrayList;
import java.util.List;

public class ConnectionStub implements IConnection<IServerMessage> {
    //private final TanksApp game;
    private final String name;
    private final List<IServerMessage> serverMessages = new ArrayList<>();

    public ConnectionStub(String name) {
        this.name = name;
        //game = new TanksApp();
    }

    public String getName() {
        return name;
    }

    public List<IServerMessage> getMessages() {
        return serverMessages;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void send(IServerMessage message) {
        serverMessages.add(message);
    }

    @Override
    public void shutdown() {
        // do nothing
    }
}
