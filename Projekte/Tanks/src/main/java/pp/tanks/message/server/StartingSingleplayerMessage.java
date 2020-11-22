package pp.tanks.message.server;

import pp.tanks.message.data.TankData;
import pp.tanks.model.item.ItemEnum;

import java.util.ArrayList;
import java.util.List;

public class StartingSingleplayerMessage implements IServerMessage {
    public final List<ItemEnum> comType;
    public final List<TankData> dataList;

    public StartingSingleplayerMessage(List<ItemEnum> comType, List<TankData> dataList) {
        this.comType = comType;
        this.dataList = dataList;
    }

    /**
     * Method to accept a visitor
     *
     * @param interpreter       visitor to be used
     */
    @Override
    public void accept(IServerInterpreter interpreter) {
        interpreter.visit(this);
    }
}
