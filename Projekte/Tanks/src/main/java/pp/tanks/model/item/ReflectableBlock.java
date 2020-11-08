package pp.tanks.model.item;

import pp.tanks.model.Model;

/**
 * Represents a block that can reflect projectiles
 */
public class ReflectableBlock extends Block {

    public ReflectableBlock(Model model) {
        super(model);
    }

    /**
     * Accept method of the visitor pattern.
     */
    public void accept(Visitor v) {
        v.visit(this);
    }
}
