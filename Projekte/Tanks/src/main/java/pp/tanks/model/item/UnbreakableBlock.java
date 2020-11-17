package pp.tanks.model.item;

import pp.tanks.message.data.Data;
import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a block that can't be destroyed by projectiles and don't reflect them
 */
public class UnbreakableBlock extends Block<Data> {
    public UnbreakableBlock(Model model, Data data) {
        super(model, data);
    }

    /**
     * Method to accept a visitor
     *
     * @param v visitor to be used
     */
    public void accept(Visitor v) {
        v.visit(this);
    }

    /**
     * Called once per frame. Used for updating this item's position etc.
     *
     * @param delta time in seconds since the last update call
     */
    @Override
    public void update(double delta) {
        super.update(delta);
    }
}
