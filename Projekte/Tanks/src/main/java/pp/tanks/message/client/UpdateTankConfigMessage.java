package pp.tanks.message.client;

import pp.network.IConnection;
import pp.tanks.message.server.IServerMessage;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.PlayerEnum;

/**
 * message sent when a client changes his tank configuration
 */
public class UpdateTankConfigMessage implements IClientMessage {
    public final ItemEnum turret;
    public final ItemEnum armor;
    public final PlayerEnum player;

    public UpdateTankConfigMessage(ItemEnum turret, ItemEnum armor, PlayerEnum player) {
        this.turret = turret;
        this.armor = armor;
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
}
