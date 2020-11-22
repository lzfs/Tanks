package pp.tanks.message.server;

import pp.tanks.server.GameMode;

public class GameEndingMessage implements IServerMessage {
    public final GameMode mode;
    public final boolean won;

    public GameEndingMessage(GameMode mode, boolean won) {
        this.mode = mode;
        this.won = won;
    }

    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}
