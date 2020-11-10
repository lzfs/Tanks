package pp.tanks.model.item;

import pp.tanks.message.data.Data;
import pp.tanks.model.Model;

/**
 * base class for blocks
 */
public abstract class Block<T extends Data> extends Item<T> {
    private int width;
    private int height;

    public Block(Model model, T data) {
        super(model, 1, data);
        this.width = 3;
        this.height = 3;
    }

    /**
     * Indicates that this block has been destroyed.
     */
    @Override
    public void destroy() {

    }

    /**
     * method for test cases
     */
    @Override
    public void isVisible() {

    }

    /**
     * Accept method of the visitor pattern.
     */
    @Override
    public void accept(Visitor v) {
        //??
    }

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param delta time in seconds since the last update call
     */
    @Override
    public void update(double delta) {

    }
}
