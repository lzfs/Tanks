package pp.media;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that manages all pixel images associated with the values of the enumeration type E
 * and defined in a properties file.
 * <p>Concrete subclasses must implement {@linkplain #getResourceAsStream(String)}, which is used
 * to find the actual image files.
 * </p>
 *
 * @param <E> the enumeration type whose values refer to pixel images.
 */
public abstract class ImageSupport<E extends Enum<E>> {
    private static final Logger LOGGER = Logger.getLogger(ImageSupport.class.getName());

    private final Map<E, Image> images;

    /**
     * Creates a new image support object.
     *
     * @param clazz      the class object of the enumeration type.
     * @param properties the properties object that maps enumeration values' names to images file names
     */
    public ImageSupport(Class<E> clazz, Properties properties) {
        images = new EnumMap<>(clazz);
        for (E prop : clazz.getEnumConstants()) {
            final String name = properties.getProperty(prop.toString());
            if (name == null)
                LOGGER.warning("No image declared for " + prop);
            else
                images.put(prop, loadImage(name));
        }
    }

    /**
     * Returns an image that is specified by the property argument.
     *
     * @param prop the property that has been used earlier to load the corresponding image file.
     */
    public Image getImage(E prop) {
        return images.get(prop);
    }

    /**
     * Safe way for loading images without crashing on invalid url
     *
     * @param name file name of the image file
     * @return Image or null if name is null or the image could not be loaded.
     */
    private Image loadImage(String name) {
        LOGGER.finer("trying to load image " + name);
        if (name == null) return null;

        // first try to load the image from a local file
        final File file = new File(name);
        if (file.exists() && file.isFile() && file.canRead())
            try (InputStream is = new FileInputStream(file)) {
                Image image = new Image(is);
                LOGGER.info(() -> "loaded image " + name + " from local file");
                return image;
            }
            catch (FileNotFoundException e) {
                LOGGER.info("failed to load local file: " + e.getMessage());
            }
            catch (IllegalArgumentException | NullPointerException | IOException e) {
                LOGGER.log(Level.WARNING,
                           "failed to load image " + name + ": " + e.getMessage(),
                           e);
            }

        // now try to load the image via the class loader
        try {
            final InputStream stream = getResourceAsStream(name);
            if (stream == null) {
                LOGGER.fine("no resource " + name);
                return null;
            }
            Image image = new Image(stream);
            LOGGER.info(() -> "loaded image " + name + " via class loader");
            return image;
        }
        catch (IllegalArgumentException | NullPointerException e) {
            LOGGER.log(Level.WARNING,
                       "failed to load image " + name + ": " + e.getMessage(),
                       e);
        }
        return null;
    }

    /**
     * Finds a resource with a given name.
     *
     * @param name name of the desired resource
     * @return A {@link java.io.InputStream} object; {@code null} if no
     * resource with this name is found, the resource is in a package
     * that is not {@linkplain Module#isOpen(String, Module) open} to at
     * least the caller module, or access to the resource is denied
     * by the security manager.
     */
    protected abstract InputStream getResourceAsStream(String name);
}
