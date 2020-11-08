package pp.tanks.message.server;

import java.io.Serializable;

public interface IServerMessage extends Serializable {

    void accept(IServerInterpreter interpreter);
}
