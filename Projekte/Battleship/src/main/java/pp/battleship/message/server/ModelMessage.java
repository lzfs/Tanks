package pp.battleship.message.server;

import pp.battleship.model.ClientState;
import pp.battleship.model.ShipMap;

/**
 * Message which contains the model of a client's game state
 */
public class ModelMessage implements ServerMessage {
    public final ShipMap ownMap;
    public final ShipMap harbor;
    public final ShipMap opponentMap;
    public final String infoText;
    public final ClientState state;

    /**
     * Creates a new ModelMessage
     *
     * @param ownMap        the client's map
     * @param harbor        the client's harbor
     * @param opponentMap   the opponent's map
     * @param infoText      info text to be displayed
     * @param state         client state to determine controls
     */
    public ModelMessage(ShipMap ownMap, ShipMap harbor, ShipMap opponentMap,
                        String infoText,
                        ClientState state) {
        this.ownMap = ownMap;
        this.harbor = harbor;
        this.opponentMap = opponentMap;
        this.infoText = infoText;
        this.state = state;
    }


    /**
     * Method to accept a visitor
     *
     * @param interpreter       visitor to be used
     */
    @Override
    public void accept(ServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}