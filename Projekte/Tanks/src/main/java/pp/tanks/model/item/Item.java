package pp.tanks.model.item;

import pp.tanks.message.data.Data;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

import java.awt.geom.Ellipse2D;

/**
 * Abstract base class of all items in a {@linkplain pp.tanks.model.TanksMap}
 */
public abstract class Item<T extends Data> {
    public static final double FACTOR_SEC = 1e-9;
    protected final Model model;
    protected double effectiveRadius;
    protected boolean destroyed = false;
    protected T data;

    /**
     * Creates a new item for the specified game model.
     *
     * @param model           the game model whose game map will contain this item.
     * @param effectiveRadius the radius of the bounding circle of this item.
     *                        It is used to compute collisions between two items with circular bounds.
     */
    protected Item(Model model, double effectiveRadius, T data) {
        this.model = model;
        this.effectiveRadius = effectiveRadius;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    /**
     * @return the effective Radius of the Item
     */
    public double getEffectiveRadius() {
        return effectiveRadius;
    }

    /**
     * Sets the position of the item
     *
     * @param pos the new position of the item
     */
    public void setPos(DoubleVec pos) {
        data.setPos(pos);
    }

    /**
     * @return the actual position of the item
     */
    public DoubleVec getPos() {
        return data.getPos();
    }

    /**
     * @return returns true if the item is destroyed and false if not
     */
    public boolean isDestroyed() {
        return data.isDestroyed();
    }

    /**
     * Checks whether there is a collision with another item
     *
     * @param other the item which is checked for a collision
     */
    public boolean collisionWith(Item other, DoubleVec newPos, double buffer) {
        if (getPos() == null || other.isDestroyed() || this.isDestroyed()) return false;
        if (other instanceof Block) {
            Block block = (Block) other;
            Ellipse2D item1 = new Ellipse2D.Double(newPos.x - effectiveRadius * (1-buffer), newPos.y - effectiveRadius * (1-buffer), effectiveRadius * (1+buffer*2), effectiveRadius * (1+buffer*2));
            return item1.intersects(other.getPos().x - block.getWidth() * 0.5, other.getPos().y - block.getHeight() * 0.5, block.getWidth(), block.getHeight());
        } else {
            return newPos.distance(other.getPos()) <= effectiveRadius + other.effectiveRadius;
        }
    }

    /**
     * Indicates that this item has been destroyed.
     */
    public void destroy() {
        data.destroy();
    }

    /**
     * Method to accept a visitor
     *
     * @param v visitor to be used
     */
    public abstract void accept(Visitor v);

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param serverTime the synced nanotime of the server
     */
    public abstract void update(long serverTime);

    /**
     * Interpolates the Data
     *
     * @param item
     */
    public abstract void interpolateData(DataTimeItem<T> item);

    /**
     * Interpolates the time
     *
     * @param serverTime
     */
    public abstract void interpolateTime(long serverTime);

    /**
     * for thomases (god's) will
     */
    public void processDamage(int damage) {
    }
}
