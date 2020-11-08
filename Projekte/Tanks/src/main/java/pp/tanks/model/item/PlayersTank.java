package pp.tanks.model.item;

import pp.tanks.model.Model;

/**
 * Represents the tank of the current player
 */
public class PlayersTank extends Tank{
    protected PlayersTank(Model model, double effectiveRadius, Armor armor, Turret turret) {
        super(model, effectiveRadius, armor, turret);
    }

    /**
     * method for test cases
     */
    @Override
    public void isVisible() {

    }

    /**
     * Accept method of the visitor pattern.
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
