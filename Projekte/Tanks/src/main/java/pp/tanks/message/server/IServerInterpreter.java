package pp.tanks.message.server;


public interface IServerInterpreter {

    void visit(SynchronizeMsg msg);

    void visit(PingMsg msg);
}
