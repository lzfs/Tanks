package pp.droids.model.item;

import pp.droids.model.DroidsGameModel;
import pp.util.DoubleVec;

/**
 * Represents a flying rocket. This is a background object, collision can be checked
 * using method {@linkplain #collisionWith(Item)}
 */
public class Rocket extends Mover {
    private DoubleVec pos;

    /**
     * Creates a new rocket with the specified starting position and its speed set to 1
     *
     * @param pos position
     */
    public Rocket(DroidsGameModel model, DoubleVec pos) {
        super(model, .5);
        this.pos = pos;
        setMoving(true);
        speed = 1.;
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
     * Sets the position
     *
     * @param pos new position
     */
    @Override
    public void setPos(DoubleVec pos) {
        this.pos = pos;
    }

    /**
     * Accept method of the visitor pattern.
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
