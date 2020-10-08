package pp.battleship.message.client;

import pp.battleship.message.server.ServerMessage;
import pp.network.IConnection;
import pp.util.IntVec;

/**
 * Message sent when clicking in the opponent's map
 */
public class ClickOpponentMapMessage implements ClientMessage {
    public final IntVec pos;

    /**
     * Creates a new ClickOpponentMessage
     *
     * @param pos    position off the click
     */
    public ClickOpponentMapMessage(IntVec pos) {
        this.pos = pos;
    }

    /**
     * Method to accept a visitor
     *
     * @param interpreter       visitor to be used
     * @param from              the connectionID
     */
    public void accept(ClientInterpreter interpreter, IConnection<ServerMessage> from) {
        interpreter.visit(this, from);
    }
}