package pp.tanks.message.server;

public class PlayerDisconnectedMessage implements IServerMessage {

    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}
