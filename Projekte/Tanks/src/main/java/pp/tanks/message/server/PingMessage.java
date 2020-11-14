package pp.tanks.message.server;

public class PingMessage implements IServerMessage{


    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}
