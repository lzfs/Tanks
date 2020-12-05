package pp.tanks.message.server;

/**
 * used to measure the ping
 */
public class PingMessage implements IServerMessage {

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
