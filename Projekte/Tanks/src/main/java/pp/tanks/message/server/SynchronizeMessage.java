package pp.tanks.message.server;

public class SynchronizeMessage implements IServerMessage {
    public final long nanoOffset;

    public SynchronizeMessage(long offset) {
        this.nanoOffset = offset;
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
