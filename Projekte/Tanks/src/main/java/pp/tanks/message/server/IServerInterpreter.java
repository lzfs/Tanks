package pp.tanks.message.server;


public interface IServerInterpreter {

    void visit(SynchronizeMessage msg);

    void visit(PingMessage msg);

    void visit(SetPlayerMessage msg);

    void visit(ServerTankUpdateMessage msg);

    void visit(StartingMultiplayerMessage msg);

    void visit(ModelMessage msg);

    void visit(StartingSingleplayerMessage msg);
}
