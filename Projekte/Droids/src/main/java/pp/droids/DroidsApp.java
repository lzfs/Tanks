package pp.droids;

import pp.droids.controller.GameEngine;
import pp.logging.Logging;

import javafx.application.Application;
import javafx.event.Event;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class of the game.
 * <p>
 * The game is originally derived from
 * <a href="https://github.com/obviam/mvc-droids">github.com/obviam/mvc-droids</a>
 */
public class DroidsApp extends Application {
    private static final Logger LOGGER = Logger.getLogger(DroidsApp.class.getName());
    private static final String PROPERTIES_FILE = "droid.properties";

    private final Properties properties = new Properties();

    static {
        // initialize logging if not initialized by logging config file or config class
        Logging.init("pp", Level.FINE);
    }

    public DroidsApp() {
        load(PROPERTIES_FILE);
    }

    /**
     * Main method of the droids app
     *
     * @param args input args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the game. This method is automatically called when JavaFX starts up.
     *
     * @param stage the main stage provided by JavaFX
     */
    @Override
    public void start(Stage stage) {
        final GameEngine engine = new GameEngine(stage, properties);
        stage.setOnCloseRequest(Event::consume);
        stage.show();
        engine.gameLoop();
    }

    /**
     * Loads properties files with the specified name. This method first tries to locate such a file on the classpath,
     * i.e., possibly in a jar file, and loads it from there. After that, it additionally tries to load it from the
     * current working directory. If files with the specified name are loaded from both locations, the properties set
     * in the second location (the current working directory) override the ones set in the first location (on the
     * classpath).
     *
     * @param fileName the name of the file. The name may actually be a path.
     */
    private void load(String fileName) {
        // first load properties using class loader
        try {
            final InputStream resource = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
            if (resource == null)
                LOGGER.info("Class loader cannot find " + fileName);
            else
                try (Reader reader = new InputStreamReader(resource, StandardCharsets.UTF_8)) {
                    properties.load(reader);
                }
        }
        catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

        // and now try to read the properties file
        final File file = new File(fileName);
        if (file.exists() && file.isFile() && file.canRead()) {
            LOGGER.info("try to read file " + fileName);
            try (FileReader reader = new FileReader(file)) {
                properties.load(reader);
            }
            catch (FileNotFoundException e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }
            catch (IOException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        }
        else
            LOGGER.info("There is no file " + fileName);
        LOGGER.fine(() -> "properties: " + properties);
    }
}
