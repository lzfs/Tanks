package pp.tanks.message.server;

public class SynchronizeMsg implements IServerMessage {
    public final long nanoOffset;

    public SynchronizeMsg(long offset) {
        this.nanoOffset = offset;
    }

    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}
