package pp.tanks.message.server;

import pp.tanks.server.GameMode;

/**
 * Message send if a game is ending
 */
public class GameEndingMessage implements IServerMessage {
    public final GameMode mode;
    public final boolean won;

    public GameEndingMessage(GameMode mode, boolean won) {
        this.mode = mode;
        this.won = won;
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
