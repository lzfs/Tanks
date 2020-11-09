package pp.tanks.model.item;

import pp.tanks.model.Model;
import pp.util.DoubleVec;

/**
 * Represents a block that can't be destroyed by projectiles and don't reflect them
 */
public class UnbreakableBlock extends Block{
    public UnbreakableBlock(Model model) {
        super(model);
    }

    /**
     * Accept method of the visitor pattern.
     */
    public void accept(Visitor v) {
        v.visit(this);
    }
}
