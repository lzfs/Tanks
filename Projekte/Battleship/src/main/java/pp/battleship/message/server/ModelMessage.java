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
    public final int amountType1;
    public final int amountType2;


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
                        ClientState state, int amountType1, int amountType2) {
        this.ownMap = ownMap;
        this.harbor = harbor;
        this.opponentMap = opponentMap;
        this.infoText = infoText;
        this.state = state;
        this.amountType1 = amountType1;
        this.amountType2 = amountType2;
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