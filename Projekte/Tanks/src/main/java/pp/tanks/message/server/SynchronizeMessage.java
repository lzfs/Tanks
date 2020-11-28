package pp.tanks.message.server;

public class SynchronizeMessage implements IServerMessage {
    public final long nanoOffset;
    public final long latency;

    public SynchronizeMessage(long offset, long latency) {
        this.nanoOffset = offset;
        this.latency = latency;
    }

    /**
     * Method to accept a visitor
     *
     * @param interpreter visitor to be used
     */
    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}
