package pp.tanks.model.item;

import pp.tanks.message.data.TankData;
import pp.tanks.model.Model;

/**
 * Represents the tank of the current player
 */
public class PlayersTank extends Tank{



    public PlayersTank(Model model, double effectiveRadius, Armor armor, Turret turret, TankData data) {
        super(model, effectiveRadius, armor, turret, data);
        setLives(3);
    }

    /**
     * method for test cases
     */
    @Override
    public void isVisible() {
        //TODO
    }





    /**
     * Accept method of the visitor pattern.
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
