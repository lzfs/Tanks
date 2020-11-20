package pp.tanks.model.item;

import pp.tanks.message.data.BBData;
import pp.tanks.model.Model;
import pp.tanks.message.data.BBData;
import pp.util.DoubleVec;

/**
 * Represents a block that can be destroyed by getting hit by a projectile
 */
public class BreakableBlock extends Block<BBData> {

    public BreakableBlock(Model model, BBData data) {
        super(model, data);
    }

    /**
     * @return the actual lifepoints of the breakable block
     */
    public int getLifepoints() {
        return data.getLifepoints();
    }

    /**
     * reduces the lifepoints
     *
     * @param points the points to reduce the lifepoints
     */
    @Override
    public void processDamage(int points) {
        if (getLifepoints() - points <= 0) {
            data.reduceLifepoints(data.getLifepoints());
            destroy();
            return;
        }
        data.reduceLifepoints(points);
    }

    /**
     * checks if the incoming damage destroys the block
     *
     * @param damage incoming damage as int
     * @return boolean-value
     */
    public boolean processDestruction(int damage) {
        return getLifepoints() - damage <= 0;
    }

    /**
     * method to accept the visitor
     *
     * @param v a Visitor
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
