package pp.tanks.message.server;

import pp.tanks.model.item.PlayerEnum;

public class SynchronizeMessage implements IServerMessage {
    public final long nanoOffset;
    public final PlayerEnum player;

    public SynchronizeMessage(long offset, int id) {
        this.nanoOffset = offset;
        this.player = PlayerEnum.getPlayer(id);
    }

    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}
