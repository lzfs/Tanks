package pp.battleship.message.server;

/**
 * Visitor interface for all server messages
 */
public interface ServerInterpreter {
    void visit(ModelMessage msg);
}
