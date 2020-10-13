package pp.droids.controller;

import pp.droids.DroidsImageProperty;
import pp.droids.DroidsSoundProperty;
import pp.droids.model.DroidsGameModel;
import pp.droids.notifications.DroidsNotification;
import pp.droids.notifications.DroidsNotificationReceiver;
import pp.droids.view.DroidsMapView;
import pp.media.ImageSupport;
import pp.media.SoundSupport;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * The game engine using the state pattern to control the game in its different states, i.e., playing the game,
 * showing that the game has been won or lost, and the menu.
 */
public class GameEngine implements EventHandler<Event>,     DroidsNotificationReceiver {
    private static final Logger LOGGER = Logger.getLogger(GameEngine.class.getName());

    // set up controllers according to state pattern
    private final Controller playGameController = new PlayGameController(this);
    private final Controller menuController = new MenuController(this);
    private final Controller gameWonController = new GameOverController(this, "Du hast gewonnen!");
    private final Controller gameLostController = new GameOverController(this, "Du hast verloren!");

    private final DroidsGameModel model;
    private final DroidsMapView view;
    private final Stage stage;
    private final ImageSupport<DroidsImageProperty> images;
    private final SoundSupport<DroidsSoundProperty> sound;

    private Controller controller;

    /**
     * Creates a new game engine
     *
     * @param stage      the game stage where the game is played and the menu is shown
     * @param properties the game properties, which - among others - configures resources like images and audio clips
     */
    public GameEngine(Stage stage, Properties properties) {
        this.stage = stage;

        images = new ImageSupport<>(DroidsImageProperty.class, properties) {
            @Override
            protected InputStream getResourceAsStream(String name) {
                return GameEngine.class.getResourceAsStream(name);
            }
        };
        sound = new SoundSupport<>(DroidsSoundProperty.class, properties) {
            @Override
            protected URL getResource(String name) {
                return GameEngine.class.getResource(name);
            }
        };
        model = new DroidsGameModel(properties);
        view = new DroidsMapView(model, images);
        model.addReceiver(this);
        model.loadRandomMap();

        setController(playGameController);

        // accept all events, which are forwarded to the current controller
        stage.addEventHandler(InputEvent.ANY, this);
    }

    /**
     * Returns the model
     *
     * @return the model, which is used in the DroidsGame
     */
    public DroidsGameModel getModel() {
        return model;
    }

    /**
     * Returns the view
     *
     * @return the view, which is used of the DroidsMap
     */
    public DroidsMapView getView() {
        return view;
    }

    /**
     * Switches the game into the game state by selecting its controller
     */
    void activatePlayGameController() {
        setController(playGameController);
    }

    /**
     * Switches the game into the menu state by selecting its controller
     */
    void activateMenuController() {
        setController(menuController);
    }

    /**
     * Switches the game into the state that indicates victory by selecting its controller
     */
    void activateGameWonController() {
        setController(gameWonController);
    }

    /**
     * Switches the game into the state that indicates defeat by selecting its controller
     */
    void activateGameLostController() {
        setController(gameLostController);
    }

    /**
     * Selects the specified controller, i.e., switches the game into the state realized by this controller
     *
     * @param controller the controller realizing the new state of the game
     */
    private void setController(Controller controller) {
        if (this.controller != null) this.controller.exit();
        this.controller = controller;
        this.controller.entry();
    }

    /**
     * The game loop, which checks user inputs and updates the view periodically.
     */
    public void gameLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long time) {
                controller.update();
                view.update();
            }
        }.start();
    }

    /**
     * The handle method of the state pattern. Any event is forwarded to the currently active controller, which then
     * selects the appropriate action, if any.
     *
     * @param event an event that happened in the game stage
     */
    @Override
    public void handle(Event event) {
        controller.handle(event);
    }

    /**
     * Sets the specified scene and changes the UI that way.
     *
     * @param scene the scene to be shown in the stage of the game.
     */
    void setScene(Scene scene) {
        stage.setScene(scene);
        stage.sizeToScene();
    }

    /**
     * Subscriber method according to the subscriber pattern. The method plays audio clips depending on the
     * specified notification if sound is not muted.
     */
    @Override
    public void notify(DroidsNotification notification) {
        LOGGER.finer("received " + notification);
        if (!getModel().isMuted())
            switch (notification) {
                case DROID_FIRED:
                    sound.play(DroidsSoundProperty.droidProjectileSound);
                    break;
                case DROID_DESTROYED:
                    sound.play(DroidsSoundProperty.destroyedSound);
                    break;
                case ENEMY_DESTROYED:
                case ROCKET_STARTS:
                    sound.play(DroidsSoundProperty.rocketSound);
                    break;
            }
    }
}
