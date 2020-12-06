package pp.tanks.message.server;

/**
 * Message send if a Player disconnect in a multiplayer game
 */
public class PlayerDisconnectedMessage implements IServerMessage {

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
