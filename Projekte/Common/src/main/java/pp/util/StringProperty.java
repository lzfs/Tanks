package pp.util;

import java.util.Properties;

public interface StringProperty {
    /**
     * Returns the value of this property stored in the specified configuration.
     *
     * @param props stores the configuration
     * @return the configured value of this property or the value returned by
     * {@linkplain #getDefaultValue()} if this property is not configured.
     */
    default String value(Properties props) {
        return value(props, getDefaultValue());
    }

    /**
     * Returns the value of this property stored in the specified configuration.
     *
     * @param props        stores the configuration
     * @param defaultValue the default value of this property if it is not configured.
     * @return the configured value of this property or the specified default value if this property is not configured.
     */
    default String value(Properties props, String defaultValue) {
        return props.getProperty(getKey(), defaultValue);
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
    String getDefaultValue();
}
