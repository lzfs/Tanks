package pp.tanks.message.server;

import pp.tanks.model.item.PlayerEnum;

public class SetPlayerMessage implements IServerMessage{
    public final PlayerEnum player;

    public SetPlayerMessage(PlayerEnum player) {
        this.player = player;
    }

    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}