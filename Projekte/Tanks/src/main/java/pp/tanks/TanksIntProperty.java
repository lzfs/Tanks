package pp.tanks;

import pp.util.IntProperty;

/**
 * The enumeration type of all integer properties of this game. Each property can be used to read its value in the
 * configuration stored in a {@link java.util.Properties} object.
 */
public enum TanksIntProperty implements IntProperty {
    /**
     * The width of each single filed of the game map.
     */
    fieldSizeX(10),
    /**
     * The width of each single filed of the game map.
     */
    fieldSizeY(10);

    TanksIntProperty(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public int getDefaultValue() {
        return defaultValue;
    }

    private final int defaultValue;
}
