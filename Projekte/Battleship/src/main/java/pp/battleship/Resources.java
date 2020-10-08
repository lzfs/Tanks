package pp.battleship;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to access properties
 */
public class Resources {
    private static final Logger LOGGER = Logger.getLogger(Resources.class.getName());
    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("battleship"); //NON-NLS

    /**
     * Returns the value to it's key
     *
     * @param key key to the value
     * @return value to the key
     */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException ex) {
            LOGGER.log(Level.SEVERE, "Missing resource for key " + key, ex); //NON-NLS
        }
        return key;
    }
}
