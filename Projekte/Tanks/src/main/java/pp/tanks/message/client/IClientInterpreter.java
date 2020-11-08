package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;

public interface IClientInterpreter {

    void visit(ClientReadyMsg msg, IConnection<IServerMessage> from);

    void visit(PingResponse msg, IConnection<IServerMessage> from);

    void visit(CollisionMessage msg, IConnection<IServerMessage> from);

    void visit(CreateNewLobbyMessage msg, IConnection<IServerMessage> from);

    void visit(JoinLobbyXMessage msg, IConnection<IServerMessage> from);

    void visit(LevelSelectedMessage msg, IConnection<IServerMessage> from);

    void visit(MoveMessage msg, IConnection<IServerMessage> from);

    void visit(ReadyMessage msg, IConnection<IServerMessage> from);

    void visit(ShootMessage msg, IConnection<IServerMessage> from);

    void visit(StartGameMessage msg, IConnection<IServerMessage> from);

    void visit(TankSelectedMessage msg, IConnection<IServerMessage> from);

    void visit(UpdateTankConfigMessage msg, IConnection<IServerMessage> from);

}
