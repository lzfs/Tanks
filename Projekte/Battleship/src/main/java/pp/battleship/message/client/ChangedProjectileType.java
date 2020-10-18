package pp.battleship.message.client;

import pp.battleship.message.server.ServerMessage;
import pp.battleship.model.Projectile;
import pp.network.IConnection;

/**
 * Message sent when another Projectile Type is chosen
 */
public class ChangedProjectileType implements ClientMessage {
    public final Projectile typeUsed;

    /**
     * creates a new ChangedProjectileType message
     * @param typeUsed the projectile which is changed to
     */
    public ChangedProjectileType(Projectile typeUsed) {
        this.typeUsed = typeUsed;
    }

    /**
     * Method to accept a visitor
     *
     * @param interpreter       visitor to be used
     * @param from              the connectionID
     */
    @Override
    public void accept(ClientInterpreter interpreter, IConnection<ServerMessage> from) {
        interpreter.visit(this, from);
    }
}