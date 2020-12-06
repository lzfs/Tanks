package pp.tanks.controller;

import pp.network.Connection;
import pp.tanks.*;
import pp.tanks.client.TanksApp;
import pp.tanks.TanksImageProperty;
import pp.tanks.TanksSoundProperty;
import pp.tanks.message.client.IClientMessage;
import pp.tanks.message.server.IServerMessage;
import pp.tanks.model.Model;
import pp.tanks.model.item.Enemy;
import pp.tanks.model.item.PlayerEnum;
import pp.tanks.model.item.Tank;
import pp.tanks.notification.TanksNotification;
import pp.tanks.notification.TanksNotificationReceiver;
import pp.tanks.server.GameMode;
import pp.tanks.view.MenuView;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;

import pp.tanks.view.TanksMapView;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The game engine using the state pattern to control the game in its different states, i.e., playing the game,
 * showing that the game has been won or lost, and the menu.
 */
public class Engine implements EventHandler<Event>, TanksNotificationReceiver {
    private static final Logger LOGGER = Logger.getLogger(Engine.class.getName());
    private final TanksApp tankApp;
    private MenuView menuView;
    private TanksMapView view;
    public boolean viewUpdate = true;

    public final MainMenuController mainMenuController;
    public final CreditsController creditsController;
    public final SettingsController settingsController;
    public final LevelController levelController;
    public final StartGameSPController StartGameSPController;
    public final TankConfigSPController tankConfigSPController;
    public final Mission1CompleteController mission1CompleteController;
    public final Mission2CompleteController mission2CompleteController;
    public final GameOverSPController gameOverSPController;
    public final PauseMenuSPController pauseMenuSPController;
    public final PlayGameController playGameController;
    public final TutorialOverviewController tutorialOverviewController;

    public final LobbyController lobbyController;
    public final SearchGameServerConfigController searchGameServerConfigController;
    public final TankConfigMPController tankConfigMPController;
    public final GameOverMPController gameOverMPController;
    public final GameWonMPController gameWonMPController;
    public final ConnectionLostController connectionLostController;

    private final Stage stage;
    private final Model model;
    private final ImageSupport<TanksImageProperty> images;
    private final SoundSupport<TanksSoundProperty> sound;

    private Controller controller;
    private Tank saveTank = null;
    private Enemy saveEnemyTank = null;
    private GameMode mode;
    private PlayerEnum playerEnum = PlayerEnum.PLAYER1;
    private int mapCounter = 0;
    private long animationTime;
    private boolean soundMuted = false;
    private boolean isTutorial = false;

    /**
     * Creates a new engine
     *
     * @param stage the game stage where the game is played and the menu is shown
     */
    public Engine(Stage stage, TanksApp tankApp, Properties properties) {
        this.mainMenuController = new MainMenuController(this);
        this.creditsController = new CreditsController(this);
        this.settingsController = new SettingsController(this);
        this.levelController = new LevelController(this);
        this.StartGameSPController = new StartGameSPController(this);
        this.tankConfigSPController = new TankConfigSPController(this);
        this.mission1CompleteController = new Mission1CompleteController(this);
        this.mission2CompleteController = new Mission2CompleteController(this);
        this.gameOverSPController = new GameOverSPController(this);
        this.pauseMenuSPController = new PauseMenuSPController(this);
        this.playGameController = new PlayGameController(this);
        this.tutorialOverviewController = new TutorialOverviewController(this);

        this.lobbyController = new LobbyController(this);
        this.searchGameServerConfigController = new SearchGameServerConfigController(this);
        this.tankConfigMPController = new TankConfigMPController(this);
        this.gameOverMPController = new GameOverMPController(this);
        this.gameWonMPController = new GameWonMPController(this);
        this.connectionLostController = new ConnectionLostController(this);

        this.tankApp = tankApp;
        this.stage = stage;
        this.model = new Model(properties);
        this.model.setEngine(this);
        model.addReceiver(this);

        images = new ImageSupport<>(TanksImageProperty.class, properties) {
            @Override
            protected InputStream getResourceAsStream(String name) {
                return Engine.class.getResourceAsStream(name);
            }
        };
        sound = new SoundSupport<>(TanksSoundProperty.class, properties) {
            @Override
            protected URL getResource(String name) {
                return Engine.class.getResource(name);
            }
        };

        setController(mainMenuController);
        this.menuView = MenuView.makeView(stage, mainMenuController.getFileName(), mainMenuController);
        this.view = null;
        stage.addEventHandler(InputEvent.ANY, this);
    }

    /**
     * setter method for the mapCounter
     */
    public void setMapCounter(int counter) {
        this.mapCounter = counter;
    }

    /**
     * getter method for the mapCounter
     */
    public int getMapCounter() {
        return mapCounter;
    }

    /**
     * setter method for the mapCounter
     */
    public void setSaveEnemyTank(Enemy enemyTank) {
        this.saveEnemyTank = enemyTank;
    }

    /**
     * getter method for the mapCounter
     */
    public Enemy getSaveEnemyTank() {
        return saveEnemyTank;
    }

    /**
     * getter method for the isTutorial flag
     */
    public boolean isTutorial() {
        return isTutorial;
    }

    /**
     * setter method for the isTutorial flag
     */
    public void setTutorial(boolean tutorial) {
        isTutorial = tutorial;
    }

    /**
     * setter method for the saveTank
     */
    public void setSaveTank(Tank tank) {
        this.saveTank = tank;
    }

    /**
     * getter method for the saveTank
     */
    public Tank getSaveTank() {
        return this.saveTank;
    }

    /**
     * setter method for the game mode
     */
    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    /**
     * getter method for the game mode
     */
    public GameMode getMode() {
        return this.mode;
    }

    /**
     * getter method to show if the sound is muted
     */
    public boolean isSoundMuted() {
        return soundMuted;
    }

    /**
     * setter method to mute/unmute the sound
     */
    public void setSoundMuted(boolean soundMuted) {
        this.soundMuted = soundMuted;
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
     * make a view that is suitable for the specified controller
     *
     * @param nameOfFile the file you want to load from
     * @param controller the type of the controller
     * @return the newly created view
     */
    public MenuView getViewForController(String nameOfFile, Controller controller) {
        this.menuView = MenuView.makeView(stage, nameOfFile, controller);
        return this.menuView;
    }

    /**
     * getter method for the menuView
     */
    public MenuView getMenuView() {
        return this.menuView;
    }

    /**
     * getter method for the game view
     */
    public TanksMapView getView() {
        return this.view;
    }

    /**
     * setter method for the game view
     */
    public void setView(TanksMapView view) {
        this.view = view;
    }

    /**
     * getter metho for the tankApp
     */
    public TanksApp getTankApp() {
        return this.tankApp;
    }

    /**
     * getter method for the images used with ImageSupport
     */
    public ImageSupport<TanksImageProperty> getImages() {
        return images;
    }

    /**
     * getter method for the model of the game
     */
    public Model getModel() {
        return this.model;
    }

    /**
     * getter method for the playerEnum
     */
    public PlayerEnum getPlayerEnum() {
        return playerEnum;
    }

    /**
     * setter method for the playerEnum
     */
    public void setPlayerEnum(PlayerEnum playerEnum) {
        this.playerEnum = playerEnum;
    }

    /**
     * convenience method to get the offset from the tankApp
     */
    public long getOffset() {
        return getTankApp().getOffset();
    }

    /**
     * convenience method to get the latency from the tankApp
     */
    public long getLatency() {
        return getTankApp().getLatency();
    }

    /**
     * convenience method to get the connection from the tankApp
     */
    public Connection<IClientMessage, IServerMessage> getConnection() {
        return getTankApp().getConnection();
    }

    /**
     * getter method for the current controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * checks if the current game is in singleplayer mode
     */
    public boolean isClientGame() {
        if (this.mode == null) throw new IllegalStateException("No game mode is set.");
        return this.mode != GameMode.MULTIPLAYER;
    }

    /**
     * getter method for the animationTime
     */
    public long getAnimationTime() {
        return animationTime;
    }

    /**
     * The game loop, which checks user inputs and updates the view periodically.
     */
    public void gameLoop() {
        new AnimationTimer() {
            /**
             * This method needs to be overridden by extending classes. It is going to
             * be called in every frame while the {@code AnimationTimer} is active.
             *
             * @param now The timestamp of the current frame given in nanoseconds. This
             *            value will be the same for all {@code AnimationTimers} called
             *            during one frame.
             */
            @Override
            public void handle(long now) {
                controller.update();
                menuView.update();
                if (view != null && viewUpdate) {
                    view.update();
                }
            }
        }.start();
    }

    /**
     * activate the CreditsController
     */
    public void activateCreditsController() {
        setController(creditsController);
    }

    /**
     * activate the MainMenuController
     */
    public void activateMainMenuController() {
        setController(mainMenuController);
    }

    /**
     * activate the SettingsController
     */
    public void activateSettingsController() {
        setController(settingsController);
    }

    /**
     * activate the LevelController
     */
    public void activateLevelController() {
        setController(levelController);
    }

    /**
     * activate the StartGameSPController
     */
    public void activateStartGameSPController() {
        setController(StartGameSPController);
    }

    /**
     * activate the TankConfigSPController
     */
    public void activateTankConfigSPController() {
        setController(tankConfigSPController);
    }

    /**
     * activate the PauseMenuSPController
     */
    public void activatePauseMenuSPController() {
        setController(pauseMenuSPController);
    }

    /**
     * activate the GameWonMPController
     */
    public void activateGameWonMPController() {
        setController(gameWonMPController);
    }

    /**
     * activate the TutorialOverviewController
     */
    public void activateTutorialOverviewController() {
        setController(tutorialOverviewController);
    }

    /**
     * activate the LobbyController
     */
    public void activateLobbyController() {
        setController(lobbyController);
    }

    /**
     * activate the SearchGameServerConfigController
     */
    public void activateSearchGameServerConfigController() {
        setController(searchGameServerConfigController);
    }

    /**
     * activate the TankConfigMPController
     */
    public void activateTankConfigMPController() {
        setController(tankConfigMPController);
    }

    /**
     * activate the GameOverSPController
     */
    public void activateGameOverSPController() {
        setController(gameOverSPController);
    }

    /**
     * activate the GameOverMPController
     */
    public void activateGameOverMPController() {
        setController(gameOverMPController);
    }

    /**
     * activate the Mission1CompleteController
     */
    public void activateMission1CompleteController() {
        setController(mission1CompleteController);
    }

    /**
     * activate the Mission2CompleteController
     */
    public void activateMission2CompleteController() {
        setController(mission2CompleteController);
    }

    /**
     * activate the PlayGameController
     */
    public void activatePlayGameController() {
        setController(playGameController);
    }

    /**
     * activate the ConnectionLostController
     */
    public void activateConnectionLostController() {
        setController(connectionLostController);
    }

    /**
     * Select the specified controller, i.e., switches the game into the state realized by this controller
     *
     * @param controller the controller realizing the new state of the game
     */
    private void setController(Controller controller) {
        if (this.controller != null) this.controller.exit();
        this.controller = controller;
        this.controller.entry();
    }

    /**
     * method to resume the game after exiting the pause menu in the game
     */
    public void resumeGame() {
        this.controller.exit();
        this.controller = playGameController;
        playGameController.resumeGame();
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
     * Subscriber method according to the subscriber pattern. The method plays audio clips depending on the
     * specified notification if sound is not muted.
     */
    @Override
    public void notify(TanksNotification notification) {
        LOGGER.finer("received " + notification);
        if (!soundMuted) {
            switch (notification) {
                case TANK_FIRED:
                    sound.play(TanksSoundProperty.tanksProjectileSound);
                    break;
                case TANK_DESTROYED:
                    sound.play(TanksSoundProperty.destroyedSound);
                    break;
                case BLOCK_DESTROYED:
                    sound.play(TanksSoundProperty.blockDestroyedSound);
                    break;
                case ARMOR_HIT:
                    sound.play(TanksSoundProperty.armorHit);
                    break;
            }
        }
    }

    /**
     * calculates the animation time
     */
    public void computeAnimationTime() {
        long tmp = getLatency();
        animationTime = -1 * tmp;
        LOGGER.log(Level.INFO, "animation time: " + animationTime);
    }
}
