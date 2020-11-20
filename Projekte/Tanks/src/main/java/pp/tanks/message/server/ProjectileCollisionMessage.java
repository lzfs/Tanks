package pp.tanks.message.server;

import pp.tanks.message.data.ProjectileCollision;

public class ProjectileCollisionMessage implements IServerMessage {
    public final ProjectileCollision collision;

    public ProjectileCollisionMessage(ProjectileCollision collision) {
        this.collision = collision;
    }

    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}
