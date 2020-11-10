package pp.tanks.model.item;

import pp.tanks.message.data.Data;
import pp.tanks.model.Model;

/**
 * Represents a block that can reflect projectiles
 */
public class ReflectableBlock extends Block<Data> {

    public ReflectableBlock(Model model, Data data) {
        super(model, data);
    }

    /**
     * Accept method of the visitor pattern.
     */
    public void accept(Visitor v) {
        v.visit(this);
    }
}
