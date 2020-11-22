package pp.tanks.message.server;

import pp.tanks.message.data.ProjectileCollision;

import java.util.List;

public class ProjectileCollisionMessage implements IServerMessage {
    public final List<ProjectileCollision> collision;

    public ProjectileCollisionMessage(List<ProjectileCollision> collision) {
        this.collision = collision;
    }

    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}
