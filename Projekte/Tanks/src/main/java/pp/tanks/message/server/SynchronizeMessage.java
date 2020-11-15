package pp.tanks.message.server;

import pp.tanks.model.item.PlayerEnum;

public class SynchronizeMessage implements IServerMessage {
    public final long nanoOffset;

    public SynchronizeMessage(long offset) {
        this.nanoOffset = offset;
    }

    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}
