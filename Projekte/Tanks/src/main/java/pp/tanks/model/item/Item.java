package pp.tanks.model.item;

import pp.tanks.message.data.Data;
import pp.tanks.model.Model;
import pp.tanks.message.data.Data;
import pp.util.DoubleVec;

import javafx.geometry.Rectangle2D;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D.Double;

/**
 * Abstract base class of all items in a {@linkplain pp.tanks.model.TanksMap}
 */
public abstract class Item<T extends Data> {
    public static final long FACTOR_SEC = 1_000_000_000;
    protected final Model model;
    protected double effectiveRadius;
    protected boolean destroyed = false;
    protected T data; //TODO ggf. final hinzufügen

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

    /*
     * Checks whether there is a collision with another item
     *
     * @param other the item which is checked for a collision
     *//*
    public boolean collisionWith(Item other) {
        if (getPos() == null || other.isDestroyed()) return false;

        double height = 0.5;
        double width = 0.5;

        if (other instanceof Projectile && this instanceof Projectile) {
            return getPos().distance(other.getPos()) <= effectiveRadius + other.effectiveRadius;
        }
        else if (other instanceof Projectile) {
            return (Math.abs(getPos().x - other.getPos().x) <= width + other.getEffectiveRadius())
                   && (Math.abs(getPos().y - other.getPos().y) <= height + other.getEffectiveRadius());
        }
        else if (this instanceof Projectile) {
            return (Math.abs(getPos().x - other.getPos().x) <= width + this.getEffectiveRadius())
                   && (Math.abs(getPos().y - other.getPos().y) <= height + this.getEffectiveRadius());
        }
        else {
            Rectangle2D item1 = new Rectangle2D(this.getPos().x - width, this.getPos().y - height, 2 * width, 2 * height);
            Rectangle2D item2 = new Rectangle2D(other.getPos().x - width, other.getPos().y - height, 2 * width, 2 * height);
            return item1.intersects(item2);
        }
    }
    */

    /**
     * Checks whether there is a collision with another item
     *
     * @param other the item which is checked for a collision
     */
    public boolean collisionWith(Item other, DoubleVec newPos) {
        if (getPos() == null || other.isDestroyed()) return false;

        if (other instanceof Block) {
            Block block = (Block) other;
            Ellipse2D item1 = new Ellipse2D.Double(newPos.x - (effectiveRadius / 2), newPos.y - (effectiveRadius / 2), effectiveRadius, effectiveRadius);
            return item1.intersects(other.getPos().x - (block.getWidth() / 2.0), other.getPos().y - (block.getHeight() / 2.0), block.getWidth(), block.getHeight());
        }
        else {
            return getPos().distance(other.getPos()) <= effectiveRadius + other.effectiveRadius;
        }
    }


    /**
     * Indicates that this item has been destroyed.
     */
    public void destroy() {
        data.destroy();
    }

    public abstract void isVisible();

    /**
     * Method to accept a visitor
     *
     * @param v visitor to be used
     */
    public abstract void accept(Visitor v);

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param delta time in seconds since the last update call
     */
    public abstract void update(double delta);



}
