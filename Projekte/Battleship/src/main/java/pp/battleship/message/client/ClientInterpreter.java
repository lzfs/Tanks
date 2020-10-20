package pp.battleship.message.client;

import pp.battleship.message.server.ServerMessage;
import pp.network.IConnection;

/**
 * Visitor interface for all client messages
 */
public interface ClientInterpreter {
    void visit(ClickOwnMapMessage msg, IConnection<ServerMessage> from);

    void visit(ClickHarborMessage msg, IConnection<ServerMessage> from);

    void visit(RemoveMessage msg, IConnection<ServerMessage> from);

    void visit(RotateMessage msg, IConnection<ServerMessage> from);

    void visit(ClickOpponentMapMessage msg, IConnection<ServerMessage> from);

    void visit(ReadyMessage msg, IConnection<ServerMessage> from);

    void visit(ConfirmMessage msg, IConnection<ServerMessage> from);

    void visit(ClientReadyMessage msg, IConnection<ServerMessage> from);

    void visit(ChangedProjectileType msg, IConnection<ServerMessage> from);
}
