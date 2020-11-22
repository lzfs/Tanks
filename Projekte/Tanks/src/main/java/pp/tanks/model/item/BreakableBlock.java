package pp.tanks.model.item;

import pp.tanks.message.data.BBData;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.model.Model;

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
        return data.getLifePoints();
    }

    /**
     * reduces the lifepoints
     *
     * @param damage the points to reduce the lifepoints
     */
    @Override
    public void processDamage(int damage) {
        if (getLifepoints() - damage <= 0) {
            data.reduceLifepoints(data.getLifePoints());
            destroy();
            return;
        }
        data.reduceLifepoints(damage);
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

    @Override
    public void interpolateData(DataTimeItem<BBData> item) {
        this.data = item.data;
    }

}

