package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;
import pp.util.DoubleVec;

/**
 * Message used to update the turret direction of the tank
 */
public class TurretUpdateMessage implements IClientMessage {
    public final int id;
    public final DoubleVec turDir;

    public TurretUpdateMessage(int id, DoubleVec dir) {
        this.id = id;
        this.turDir = dir;
    }

    /**
     * Method to accept a visitor
     *
     * @param interpreter visitor to be used
     * @param from        the connectionID
     */
    @Override
    public void accept(IClientInterpreter interpreter, IConnection<IServerMessage> from) {
        interpreter.visit(this, from);
    }
}
