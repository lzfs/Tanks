package pp.tanks.message.server;

import pp.tanks.model.item.PlayerEnum;

/**
 * tells the client which player enum he got
 */
public class SetPlayerMessage implements IServerMessage {
    public final PlayerEnum player;

    public SetPlayerMessage(PlayerEnum player) {
        this.player = player;
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
