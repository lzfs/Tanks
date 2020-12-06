package pp.tanks.server.auto;

import pp.automaton.StateSupport;
import pp.network.IConnection;
import pp.tanks.message.client.ClientReadyMessage;
import pp.tanks.message.client.MoveMessage;
import pp.tanks.message.client.PingResponse;
import pp.tanks.message.client.ShootMessage;
import pp.tanks.message.client.StartGameMessage;
import pp.tanks.message.client.TurretUpdateMessage;
import pp.tanks.message.client.UpdateTankConfigMessage;
import pp.tanks.message.server.IServerMessage;

public abstract class TankState extends StateSupport<TankState> {

    /**
     * is called when a player sends a ClientReadyMsg and is received by the server
     *
     * @param msg  the ClientReadyMsg
     * @param conn the Connection the message was sent from
     */
    public void playerConnected(ClientReadyMessage msg, IConnection<IServerMessage> conn) {
        handle(s -> s.playerConnected(msg, conn));
    }

    /**
     * is called when a player sends a PingResponse and is received by the server
     *
     * @param msg  the PingResponse
     * @param conn the connection the message was sent from
     */
    public void pingResponse(PingResponse msg, IConnection<IServerMessage> conn) {
        handle(s -> s.pingResponse(msg, conn));
    }

    /**
     * is called when a player sends a MoveMessage and is received by the server
     *
     * @param msg  the MoveMessage
     * @param conn the connection the message was sent from
     */
    public void tankMove(MoveMessage msg, IConnection<IServerMessage> conn) {
        handle(s -> s.tankMove(msg, conn));
    }

    /**
     * is called when a player sends a ShootMessage and is received by the server
     *
     * @param msg the ShootMessage
     */
    public void shoot(ShootMessage msg) {
        handle(s -> s.shoot(msg));
    }

    /**
     * is called when a player sends a BackMessage and is received by the server
     */
    public void back(IConnection<IServerMessage> conn) {
        handle(s -> s.back(conn));
    }

    /**
     * is called when a player sends a StartGameMessage and is received by the server
     *
     * @param msg the StartGameMessage
     */
    public void startGame(StartGameMessage msg) {
        handle(s -> s.startGame(msg));
    }

    /**
     * is called when a player sends a UpdateTankConfigMessage and is received by the server
     *
     * @param msg the UpdateTankConfigMessage
     */
    public void updateTankConfig(UpdateTankConfigMessage msg) {
        handle(s -> s.updateTankConfig(msg));
    }

    /**
     * is called when a player is disconnected
     *
     * @param conn correct message
     */
    public void playerDisconnected(IConnection<IServerMessage> conn) {
        handle(s -> s.playerDisconnected(conn));
    }

    /**
     * called when a turret-update is received
     *
     * @param msg
     */
    public void turretUpdate(TurretUpdateMessage msg) {
        handle(s -> s.turretUpdate(msg));
    }
}
