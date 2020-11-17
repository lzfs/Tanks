package pp.tanks.model;

import pp.tanks.message.data.BBData;
import pp.tanks.message.data.Data;
import pp.tanks.message.data.TankData;
import pp.tanks.model.item.Armor;
import pp.tanks.model.item.BreakableBlock;
import pp.tanks.model.item.Howitzer;
import pp.tanks.model.item.LightTurret;
import pp.tanks.model.item.PlayersTank;
import pp.tanks.model.item.ReflectableBlock;
import pp.tanks.model.item.Tank;
import pp.tanks.model.item.Turret;
import pp.tanks.model.item.UnbreakableBlock;
import pp.util.DoubleVec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pp.tanks.TanksIntProperty.fieldSizeX;
import static pp.tanks.TanksIntProperty.fieldSizeY;

/**
 * Convenience class for generating random game maps.
 */
class TanksMapCreator {
    private final Model model;

    /**
     * Creates an instance of this class for the specified game model.
     */
    public TanksMapCreator(Model model) {
        this.model = model;
    }

    /**
     * @return empty map
     */
    public TanksMap makeEmptyMap() {
        final int width = fieldSizeX.value(model.getProperties());
        final int height = fieldSizeY.value(model.getProperties());
        TanksMap map = new TanksMap(model, width, height);
        return map;
    }
}

