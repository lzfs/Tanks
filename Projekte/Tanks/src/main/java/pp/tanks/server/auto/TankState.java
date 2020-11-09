package pp.tanks.server.auto;

import pp.automaton.StateSupport;
import pp.network.IConnection;
import pp.tanks.message.client.ClientReadyMsg;
import pp.tanks.message.client.MoveMessage;
import pp.tanks.message.client.PingResponse;
import pp.tanks.message.client.ShootMessage;
import pp.tanks.message.server.IServerMessage;

public abstract class TankState extends StateSupport<TankState> {

    /**
     *
     * @return the automaton this state belongs to
     */
    TankAutomaton getAuto() {
        return containingState().getAuto();
    }

    /**
     * is called when a player sends a ClientReadyMsg and is received by the server
     * @param msg the ClientReadyMsg
     * @param conn the Connection the message was sent from
     */
    public void playerConnected(ClientReadyMsg msg, IConnection<IServerMessage> conn) {
        handle(s -> s.playerConnected(msg, conn));
    }

    /**
     * is called when a player sends a PingResponse and is received by the server
     * @param msg the PingResponse
     * @param conn the connection the message was sent from
     */
    public void pingResponse(PingResponse msg, IConnection<IServerMessage> conn) {
        handle(s -> s.pingResponse(msg, conn));
    }

    /**
     * is called when a player sends a MoveMessage and is received by the server
     * @param msg the MoveMessage
     * @param conn the connection the message was sent from
     */
    public void tankMove(MoveMessage msg, IConnection<IServerMessage> conn) {
        handle(s -> s.tankMove(msg, conn));
    }

    /**
     * is called when a player sends a ShootMessage and is received by the server
     * @param msg the ShootMessage
     */
    public void shoot(ShootMessage msg) {
        handle(s -> s.shoot(msg));
    }
}