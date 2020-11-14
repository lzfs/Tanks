package pp.tanks;

/**
 * The enumeration type of all sound properties of this game. Each property can be used to read its value in the
 * configuration stored in a {@link java.util.Properties} object.
 */
public enum TanksSoundProperty {

    /**
     * Path to the sound file played when a tank shoots
     */
    tanksProjectileSound,

    /**
     * Path to the sound file played when a tank gets destroyed
     */
    destroyedSound,

    /**
     * Path to the sound file played when a block gets destroyed
     */
    blockDestroyedSound,
}

