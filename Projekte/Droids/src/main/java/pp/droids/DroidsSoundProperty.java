package pp.droids;

/**
 * The enumeration type of all sound properties of this game. Each property can be used to read its value in the
 * configuration stored in a {@link java.util.Properties} object.
 */
public enum DroidsSoundProperty {
    /**
     * Path to the sound file played when the droid shoots
     */
    droidProjectileSound,
    /**
     * Path to the sound file played when an item is destroyed
     */
    destroyedSound,
    /**
     * Path to the sound file played when a rocket starts
     */
    rocketSound
}
