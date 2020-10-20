package pp.droids;

import pp.util.IntProperty;

/**
 * The enumeration type of all integer properties of this game. Each property can be used to read its value in the
 * configuration stored in a {@link java.util.Properties} object.
 */
public enum DroidsIntProperty implements IntProperty {
    /**
     * The width of each single filed of the game map.
     */
    fieldSizeX(10),
    /**
     * The width of each single filed of the game map.
     */
    fieldSizeY(10),
    /**
     * The duration how long a hint shall be shown.
     */
    hintTime(4);

    DroidsIntProperty(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public int getDefaultValue() {
        return defaultValue;
    }

    private final int defaultValue;
}
