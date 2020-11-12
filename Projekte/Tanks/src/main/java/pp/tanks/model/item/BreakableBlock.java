package pp.tanks.model.item;

import pp.tanks.message.data.BBData;
import pp.tanks.model.Model;
import pp.tanks.message.data.BBData;
import pp.util.DoubleVec;

/**
 * Represents a block that can be destroyed by getting hit by a projectile
 */
public class BreakableBlock extends Block<BBData>{

    public BreakableBlock(Model model, BBData data) {
        super(model, data);
    }

    /**
     *
     * @return the actual lifepoints of the breakable block
     */
    public int getLifepoints() {
        return data.getLifepoints();
    }

    /**
     * reduces the lifepoints
     * @param points the points to reduce the lifepoints
     */
    public void reduce(int points) {
        if(getLifepoints() - points <= 0) {
            data.reduceLifepoints(data.getLifepoints());
            destroy();
            return;
        }
        data.reduceLifepoints(points);
    }

    /**
     * method to accept the visitor
     * @param v a Visitor
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public void update(double delta) {
        super.update(delta);
    }
}
