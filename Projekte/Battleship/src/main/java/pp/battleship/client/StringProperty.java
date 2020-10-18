package pp.battleship.client;

import java.util.Properties;

/**
 * The enumeration type of all string properties of this game. Each property can be used to read its value in the
 * configuration stored in a {@link java.util.Properties} object.
 */
public enum StringProperty {
    /**
     * Path of the image of ship1
     */
    imgship1,
    /**
     * Path of the image of ship2
     */
    imgship2,
    /**
     * Path of the image of ship3
     */
    imgship3,
    /**
     * Path of the image of ship4
     */
    imgship4,
    /**
     * Path of the image of imghit
     */
    imghit;

    /**
     * Returns the value of this property stored in the specified configuration.
     *
     * @param props stores the configuration
     * @return the configured value of this property or null if this property is not configured.
     */
    public String value(Properties props) {return value(props, null);
    }

    /**
     * Returns the value of this property stored in the specified configuration.
     *
     * @param props        stores the configuration
     * @param defaultValue the default value of this property if it is not configured.
     * @return the configured value of this property or the specified default value if this property is not configured.
     */
    public String value(Properties props, String defaultValue) {
        return props.getProperty(toString(), defaultValue);
    }
}
