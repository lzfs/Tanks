package pp.util;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface IntProperty {
    Logger LOGGER = Logger.getLogger(IntProperty.class.getName());

    /**
     * Returns the value of this property stored in the specified configuration.
     *
     * @param props stores the configuration
     * @return the configured value of this property or the value returned by
     * {@linkplain #getDefaultValue()} if this property is not configured.
     */
    default int value(Properties props) {
        return value(props, getDefaultValue());
    }

    /**
     * Returns the value of this property stored in the specified configuration.
     *
     * @param props        stores the configuration
     * @param defaultValue the default value of this property if it is not configured or
     *                     does not have an integer value.
     * @return the configured value of this property or the specified default value if
     * this property is not configured or if the configured value is not an integer.
     */
    default int value(Properties props, int defaultValue) {
        final String property = props.getProperty(getKey(), String.valueOf(defaultValue));
        try {
            return Integer.parseInt(property);
        }
        catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "wrong " + this + " value " + property, e);
            return defaultValue;
        }
    }

    /**
     * Returns the key of this property in a configuration.
     * Returns the String returned by toString() by default.
     *
     * @return the key of this property.
     */
    default String getKey() {
        return toString();
    }

    /**
     * Returns the default value for this property.
     */
    int getDefaultValue();
}
