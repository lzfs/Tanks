package pp.tanks.message.server;

public class PingMsg implements IServerMessage{


    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}
