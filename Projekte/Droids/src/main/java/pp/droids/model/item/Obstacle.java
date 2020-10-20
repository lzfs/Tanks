package pp.droids.model.item;

import pp.droids.model.DroidsGameModel;
import pp.util.DoubleVec;

/**
 * A class representing an obstacle
 */
public class Obstacle extends Item {
    private final DoubleVec pos;

    /**
     * Creates an obstacle
     *
     * @param pos position
     */
    public Obstacle(DroidsGameModel model, DoubleVec pos) {
        super(model, .5);
        this.pos = pos;
    }

    /**
     * Returns the position
     *
     * @return position
     */
    @Override
    public DoubleVec getPos() {
        return pos;
    }

    /**
     * Accept method of the visitor pattern.
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    /**
     * Empty implementation of this method because an item does not move or do anything else.
     */
    @Override
    public void update(double delta) {}
}
