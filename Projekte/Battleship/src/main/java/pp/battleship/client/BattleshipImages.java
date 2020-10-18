package pp.battleship.client;

import javafx.scene.image.Image;
import pp.battleship.model.Battleship;

import java.io.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A class that manages all pixel images for visualization in the Battleship game in a hash table.
 */
class BattleshipImages {
    private static final Logger logger = Logger.getLogger(BattleshipImages.class.getName());

    private final Properties props;
    private final Map<StringProperty, Image> images = new EnumMap<>(StringProperty.class);

    /**
     * Creates a new instance for the specified game model.
     */
    public BattleshipImages() {
        this.props = loadProp();
        loadImage(StringProperty.imgship1);
        loadImage(StringProperty.imgship2);
        loadImage(StringProperty.imgship3);
        loadImage(StringProperty.imgship4);
        loadImage(StringProperty.imghit);
        logger.info("Loaded Images");
    }

    /**
     * Loads properties files with the specified name. This method first tries to locate such a file on the classpath,
     * i.e., possibly in a jar file, and loads it from there. After that, it additionally tries to load it from the
     * current working directory. If files with the specified name are loaded from both locations, the properties set
     * in the second location (the current working directory) override the ones set in the first location (on the
     * classpath).
     *
     */
    private static Properties loadProp() {
        String fileName = "battleship.properties";
        Properties properties = new Properties();
        // first load properties using class loader
        try {
            final InputStream resource = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
            if (resource == null)
                logger.info("Class loader cannot find " + fileName);
            else
                properties.load(resource);
        }
        catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }

        // and now try to read the properties file
        try (FileReader reader = new FileReader(fileName)) {
            properties.load(reader);
        }
        catch (FileNotFoundException e) {
            logger.log(Level.INFO, e.getMessage(), e);
        }
        catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        logger.fine(() -> "properties: " + properties);

        return properties;
    }



    /**
     * Loads an image file specified by the property argument and stores the image in the hash table of
     * all images.
     *
     * @param prop the property whose value in the configuration indicates the path of the image file.
     */
    private void loadImage(StringProperty prop) {
        images.put(prop, loadImage(prop.value(props)));
    }

    /**
     * Returns an image that is specified by the property argument and must have been loaded previously using
     * {@link #loadImage(StringProperty)}
     *
     * @param prop the property that has been used earlier to load the corresponding image file.
     */
    public Image getImage(StringProperty prop) {
        return images.get(prop);
    }

    /**
     * Safe way for loading images without crashing on invalid url
     *
     * @param name file name of the image file
     * @return Image or null if name is null or the image could not be loaded.
     */
    private static Image loadImage(String name) {
        logger.finer("trying to load image " + name);
        if (name == null) return null;

        // first try to load the image from a local file
        final File file = new File(name);
        if (file.exists() && file.isFile() && file.canRead())
            try (InputStream is = new FileInputStream(file)) {
                Image image = new Image(is);
                logger.info(() -> "loaded image " + name + " from local file");
                return image;
            }
            catch (FileNotFoundException e) {
                logger.info("failed to load local file: " + e.getMessage());
                System.out.println("loadImage");
            }
            catch (IllegalArgumentException | NullPointerException | IOException e) {
                logger.log(Level.WARNING,
                           "failed to load image " + name + ": " + e.getMessage(),
                           e);
            }

        // now try to load the image via the class loader
        try {
            final InputStream stream = BattleshipImages.class.getResourceAsStream(name);
            if (stream == null) {
                logger.fine("no resource " + name);
                return null;
            }
            Image image = new Image(stream);
            logger.info(() -> "loaded image " + name + " via class loader");
            return image;
        }
        catch (IllegalArgumentException | NullPointerException e) {
            logger.log(Level.WARNING,
                       "failed to load image " + name + ": " + e.getMessage(),
                       e);
        }
        return null;
    }
}
