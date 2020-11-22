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
     * Method to accept a visitor
     *
     * @param v visitor to be used
     */
    public void accept(Visitor v) {
        v.visit(this);
    }
}
