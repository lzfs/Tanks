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
    fieldSizeY(10),

    /**
     * The status of the music boolean.
     */
    musicMuted(0),

    /**
     * The status of the sound boolean.
     */
    soundMuted(0);

    private final int defaultValue;

    /**
     * create a new TankIntProperty
     *
     * @param defaultValue default value to use
     */
    TanksIntProperty(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Returns the default value for this property.
     */
    @Override
    public int getDefaultValue() {
        return defaultValue;
    }
}
