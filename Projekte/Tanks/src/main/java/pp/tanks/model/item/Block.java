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
        super(model, 1, data);
        this.width = 1;
        this.height = 1;
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
     * method for test cases
     */
    @Override
    public void isVisible() {
        //TODO
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
    public boolean interpolateTime(long serverTime) {
        return false;
    }
}

