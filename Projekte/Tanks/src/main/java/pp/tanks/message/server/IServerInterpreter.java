package pp.tanks.message.server;

/**
 * Interface for a Server Interpreter following the <em>visitor design pattern</em>.
 */
public interface IServerInterpreter {

    void visit(SynchronizeMessage msg);

    void visit(PingMessage msg);

    void visit(SetPlayerMessage msg);

    void visit(ServerTankUpdateMessage msg);

    void visit(StartingMultiplayerMessage msg);

    void visit(ModelMessage msg);

    void visit(GameEndingMessage msg);

    void visit(PlayerDisconnectedMessage msg);
}
