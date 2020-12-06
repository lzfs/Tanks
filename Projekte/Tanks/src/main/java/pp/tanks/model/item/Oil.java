package pp.tanks.model.item;

import pp.tanks.message.data.Data;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.model.Model;

public class Oil extends Item<Data> {
    /**
     * Creates a new item for the specified game model.
     *
     * @param model           the game model whose game map will contain this item.
     * @param effectiveRadius the radius of the bounding circle of this item.
     * @param data
     */
    public Oil(Model model, double effectiveRadius, Data data) {
        super(model, effectiveRadius, data);
    }

    /**
     * Method to accept a visitor
     *
     * @param v visitor to be used
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param serverTime the synced nanotime of the server
     */
    @Override
    public void update(long serverTime) {

    }

    /**
     * Does nothing because it is unnecessary in oil
     *
     * @param item
     */
    @Override
    public void interpolateData(DataTimeItem item) {

    }

    /**
     * return false because it is unnecessary in oil
     *
     * @param serverTime
     */
    @Override
    public void interpolateTime(long serverTime) {
    }
}
