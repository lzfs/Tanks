package pp.tanks.model.item;

import pp.tanks.message.data.Data;
import pp.tanks.model.Model;
import pp.tanks.message.data.Data;
import pp.util.DoubleVec;

/**
 * Abstract base class of all items in a {@linkplain pp.tanks.model.TanksMap}
 */
public abstract class Item {
    protected final Model model;
    protected final double effectiveRadius;
    protected boolean destroyed = false;
    protected Data data;

    /**
     * Creates a new item for the specified game model.
     *
     * @param model           the game model whose game map will contain this item.
     * @param effectiveRadius the radius of the bounding circle of this item.
     *                        It is used to compute collisions between two items with circular bounds.
     */
    protected Item(Model model, double effectiveRadius) {
        this.model = model;
        this.effectiveRadius = effectiveRadius;
    }

    /**
     *
     * @return The effective Radius of the Item
     */
    public double getEffectiveRadius() {
        return effectiveRadius;
    }

    /**
     * Sets the position of the item
     * @param pos the new position of the item
     */
    public void setPos(DoubleVec pos) {
        data.setPos(pos);
    }

    /**
     *
     * @return the actual position of the item
     */
    public DoubleVec getPos() {
        return data.getPos();
    }

    /**
     *
     * @return returns true if the item is destroyed and false if not
     */
    public boolean isDestroyed() {
        return data.isDestroyed();
    };

    /**
     * Checks whether there is a collision with another item
     *
     * @param other the item which is checked for a collision
     */
    public boolean collisionWith(Item other) {
        if (getPos() == null) return false;
        return !other.isDestroyed() &&
                getPos().distance(other.getPos()) <= effectiveRadius + other.effectiveRadius;
    }

    /**
     * Indicates that this item has been destroyed.
     */
    public void destroy() {
        data.destroy();
    }

    public abstract void isVisible();

    /**
     * Accept method of the visitor pattern.
     */
    public abstract void accept(Visitor v);

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param delta time in seconds since the last update call
     */
    public abstract void update(double delta);
}