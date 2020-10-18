package pp.battleship.message.client;

import pp.battleship.message.server.ServerMessage;
import pp.battleship.model.Projectile;
import pp.network.IConnection;

public class ChangedProjectileType implements ClientMessage {
    public final Projectile typeUsed;

    public ChangedProjectileType(Projectile typeUsed) {
        this.typeUsed = typeUsed;
    }

    @Override
    public void accept(ClientInterpreter interpreter, IConnection<ServerMessage> from) {
        interpreter.visit(this, from);
    }
}