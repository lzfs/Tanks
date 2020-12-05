package pp.tanks;

import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that manages all sound files associated with the values of the enumeration type E
 * and defined in a properties file.
 * <p>Concrete subclasses must implement {@linkplain #getResource(String)}, which is used
 * to find the actual sound files.
 * </p>
 *
 * @param <E> the enumeration type whose values refer to sound files.
 */
public abstract class SoundSupport<E extends Enum<E>> {
    protected static final Logger LOGGER = Logger.getLogger(SoundSupport.class.getName());
    private final Map<E, AudioClip> audioClips;

    /**
     * Creates a new sound support object.
     *
     * @param clazz      the class object of the enumeration type.
     * @param properties the properties object that maps enumeration values' names to sound file names
     */
    public SoundSupport(Class<E> clazz, Properties properties) {
        audioClips = new EnumMap<>(clazz);
        for (E prop : clazz.getEnumConstants()) {
            final String name = properties.getProperty(prop.toString());
            if (name == null)
                LOGGER.warning("No sound file declared for " + prop);
            else
                audioClips.put(prop, loadAudioClip(name));
        }
    }

    /**
     * Safe way for loading an audio clips from resource folder without
     * crashing on invalid url
     *
     * @param name file name of the audio clip
     * @return audio clip or null if name is null or the image could not be loaded.
     */
    private AudioClip loadAudioClip(String name) {
        LOGGER.finer("loading audio clip " + name);
        if (name == null) return null;

        // first try to load the audio clip from a local file
        final File file = new File(name);
        if (file.exists() && file.isFile() && file.canRead())
            try {
                final AudioClip clip = new AudioClip(file.toURI().toURL().toString());
                LOGGER.info(() -> "loaded audio clip " + name + " from local file");
                return clip;
            }
            catch (MediaException e) {
                LOGGER.info("failed to load local file " + name + ": " + e.getMessage());
            }
            catch (IllegalArgumentException | NullPointerException | IOException e) {
                LOGGER.log(Level.WARNING,
                           "failed to load audio clip " + name + ": " + e.getMessage(),
                           e);
            }

        // now try to load the audio clip via the class loader
        try {
            final URL resource = getResource(name);
            if (resource == null) {
                LOGGER.fine("no resource " + name);
                return null;
            }
            final AudioClip clip = new AudioClip(resource.toString());
            LOGGER.info(() -> "loaded audio clip " + name + " via class loader: " + resource);
            return clip;
        }
        catch (IllegalArgumentException | NullPointerException e) {
            LOGGER.log(Level.WARNING,
                       "failed to load audio clip " + name + ": " + e.getMessage(),
                       e);
        }
        return null;
    }

    /**
     * Returns an audio clip that is specified by the property argument.
     *
     * @param prop the property that has been used earlier to load the corresponding sound file.
     */
    public AudioClip getAudioClip(E prop) {
        return audioClips.get(prop);
    }

    /**
     * Plays an audio clip that is specified by the property argument.
     *
     * @param prop the property that has been used earlier to load the corresponding sound file.
     * @return true if there is a sound file associated with the specified argument
     */
    public boolean play(E prop) {
        final AudioClip audioClip = audioClips.get(prop);
        if (audioClip == null)
            return false;
        audioClip.play();
        return true;
    }

    /**
     * Finds a resource with a given name.
     *
     * @param name name of the desired resource
     * @return A {@link URL} object; {@code null} if no resource with
     * this name is found, the resource cannot be located by a URL, the
     * resource is in a package that is not
     * {@linkplain Module#isOpen(String, Module) open} to at least the caller
     * module, or access to the resource is denied by the security
     * manager.
     */
    protected abstract URL getResource(String name);
}
