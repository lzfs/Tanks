package pp.droids.model.item;

import pp.droids.model.DroidsGameModel;
import pp.util.DoubleVec;

/**
 * Abstract base class of all items in a {@linkplain pp.droids.model.DroidsMap}
 */
public abstract class Item {
    public final DroidsGameModel model;
    public final double effectiveRadius;
    private boolean destroyed = false;

    /**
     * Creates a new item for the specified game model.
     *
     * @param model           the game model whose game map will contain this item.
     * @param effectiveRadius the radius of the bounding circle of this item.
     *                        It is used to compute collisions between two items with circular bounds.
     */
    protected Item(DroidsGameModel model, double effectiveRadius) {
        this.model = model;
        this.effectiveRadius = effectiveRadius;
    }

    /**
     * Returns the position of the item
     */
    public abstract DoubleVec getPos();

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
     * Checks, whether this item has been destroyed
     *
     * @return true or false
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Indicates that this item has been destroyed.
     */
    public void destroy() {
        destroyed = true;
    }
}
