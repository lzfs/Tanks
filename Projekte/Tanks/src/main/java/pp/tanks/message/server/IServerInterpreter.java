package pp.tanks.message.server;


public interface IServerInterpreter {

    void visit(SynchronizeMessage msg);

    void visit(PingMessage msg);

    void visit(SetPlayerMessage msg);

    void visit(ServerTankUpdateMessage msg);
}
