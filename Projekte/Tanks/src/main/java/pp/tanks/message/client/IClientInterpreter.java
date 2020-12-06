package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;

/**
 * Visitor interface for all client messages
 */
public interface IClientInterpreter {

    void visit(ClientReadyMessage msg, IConnection<IServerMessage> from);

    void visit(PingResponse msg, IConnection<IServerMessage> from);

    void visit(MoveMessage msg, IConnection<IServerMessage> from);

    void visit(ShootMessage msg, IConnection<IServerMessage> from);

    void visit(StartGameMessage msg, IConnection<IServerMessage> from);

    void visit(UpdateTankConfigMessage msg, IConnection<IServerMessage> from);

    void visit(BackMessage msg, IConnection<IServerMessage> from);

    void visit(TurretUpdateMessage msg, IConnection<IServerMessage> from);

}
