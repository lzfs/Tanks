package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.PlayerEnum;
import pp.tanks.server.GameMode;

/**
 * message sent when a game is starting
 */
public class StartGameMessage implements IClientMessage {
    public final ItemEnum turret;
    public final ItemEnum armor;
    public final GameMode gameMode;
    public final PlayerEnum player;

    public StartGameMessage(ItemEnum turret, ItemEnum armor, GameMode gameMode, PlayerEnum player) {
        this.turret = turret;
        this.armor = armor;
        this.gameMode = gameMode;
        this.player = player;
    }


    /**
     * Method to accept a visitor
     *
     * @param interpreter       visitor to be used
     * @param from              the connectionID
     */
    @Override
    public void accept(IClientInterpreter interpreter, IConnection<IServerMessage> from) {
        interpreter.visit(this, from);
    }

    @Override
    public String toString() {
        return "StartGameMessage: " + "turret=" + turret + ", armor=" + armor + ", gameMode=" + gameMode + ", player=" + player;
    }
}
