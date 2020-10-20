package pp.droids;

/**
 * The enumeration type of all image properties of this game. Each property can be used to read its value in the
 * configuration stored in a {@link java.util.Properties} object.
 */
public enum DroidsImageProperty {
    /**
     * Path of the background image
     */
    backgroundImage,
    /**
     * Path of the image of an enemy
     */
    redShipImage,
    /**
     * Path of the image of the droid
     */
    greenShipImage,
    /**
     * Path of the image of an obstacle
     */
    asteroidImage,
    /**
     * Path of the image of a rocket
     */
    rocketImage,
    /**
     * Path of the image of a projectile shot by the droid
     */
    droidProjectileImage,
    /**
     * Path of the image of a moon
     */
    moonImage
}
