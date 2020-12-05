package pp.tanks.model.item;

import pp.tanks.message.data.Data;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.model.Model;
import pp.tanks.notification.TanksNotification;

/**
 * base class for blocks
 */
public abstract class Block<T extends Data> extends Item<T> {
    private double width;
    private double height;

    public Block(Model model, T data) {
        super(model, 0.9, data);
        this.width = 0.9;
        this.height = 0.9;
    }

    /**
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @return width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Indicates that this block has been destroyed.
     */
    @Override
    public void destroy() {
        data.destroy();
        model.notifyReceivers(TanksNotification.BLOCK_DESTROYED);
    }

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param serverTime the synced nanotime of the server
     */
    @Override
    public void update(long serverTime) {}

    @Override
    public void interpolateData(DataTimeItem<T> item) {

    }

    @Override
    public void interpolateTime(long serverTime) {
    }
}

