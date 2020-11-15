package pp.tanks.message.server;

import pp.tanks.model.item.ItemEnum;

public class ServerTankUpdateMessage implements IServerMessage {
    public final ItemEnum turret;
    public final ItemEnum armor;

    public ServerTankUpdateMessage(ItemEnum turret, ItemEnum armor) {
        this.armor = armor;
        this.turret = turret;
    }

    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}
