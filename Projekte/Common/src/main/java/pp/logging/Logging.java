package pp.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to realize logging in various levels at different packages
 */
public class Logging {
    public static final String CONFIG_FILE = "java.util.logging.config.file";
    public static final String CONFIG_CLASS = "java.util.logging.config.class";

    /**
     * Sets the logging level of all packages with the specified prefix unless logging is
     * initialized by a logging config file or config class
     *
     * @param packagePrefix Prefix of packages whose logging level is set
     * @param level         the selected logging level for all packages whose name starts with the specified prefix
     */
    public static void init(String packagePrefix, Level level) {
        if (System.getProperty(CONFIG_FILE) == null && System.getProperty(CONFIG_CLASS) == null) {
            final ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.ALL);
            final Logger parent = Logger.getLogger(packagePrefix);
            parent.addHandler(handler);
            parent.setLevel(level);
            parent.setUseParentHandlers(false);
        }
    }
}


