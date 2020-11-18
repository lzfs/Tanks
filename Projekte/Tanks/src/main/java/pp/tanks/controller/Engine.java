package pp.tanks.controller;

import pp.network.Connection;
import pp.tanks.*;
import pp.tanks.client.MiniController;
import pp.tanks.client.Sounds;
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

    public final MiniController miniController; // for testing

    private final Stage stage;
    private final Model model;
    private final ImageSupport<TanksImageProperty> images;
    private final SoundSupport<TanksSoundProperty> sound;

    private Controller controller;
    private Tank saveTank = null;
    private Enemy saveEnemyTank = null;
    private GameMode mode;
    private PlayerEnum playerEnum;
    private int mapCounter = 0;

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

        this.miniController = new MiniController(tankApp);

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
        // new TanksMapView(model, images)

        stage.addEventHandler(InputEvent.ANY, this);
    /*
        // setController(mainMenuController);
        stage.setScene(new Scene(view));
        // accept all events, which are forwarded to the current controller
        stage.addEventHandler(InputEvent.ANY, this);
        stage.show();
     */
    }

    /**
     * updates mapCounter
     *
     * @param counter new number
     */
    public void setMapCounter(int counter) {
        this.mapCounter = counter;
    }

    public void setSaveEnemyTank(Enemy enemyTank) {
        this.saveEnemyTank=enemyTank;
    }

    public Enemy getSaveEnemyTank() {
        return saveEnemyTank;
    }

    /**
     * @return mapCounter
     */
    public int getMapCounter() {
        return mapCounter;
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
                if (view != null) {
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
     * activate the playGameController
     * convenience method to start the level again if the player tank gets destroyed
     * TODO maybe we need to change that later on
     */
    public void activateGameLostController() {
        setController(playGameController);
    }

    /**
     * activate the GameWonController
     * TODO use the missionXCompleteControllers and make them generic
     */
    public void activateGameWonController() {
        setController(mission1CompleteController);
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
     * activate the Mission1CompleteSPController
     */
    public void activateMission1CompleteController() {
        setController(mission1CompleteController);
    }

    /**
     * activate the Mission2CompleteSPController
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
     * TODO: add JavaDoc
     *
     * @param tank
     */
    public void setSaveTank(Tank tank) {
        this.saveTank = tank;
    }

    /**
     * @return saveTank
     */
    public Tank getSaveTank() {
        return this.saveTank;
    }

    /**
     * updates gamemode
     *
     * @param mode new gamemode
     */
    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    /**
     * @return gamemode
     */
    public GameMode getMode() {
        return this.mode;
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
        //-------------------
        //Image image = images.getImage(TankImageProperty.cursor);
        //scene.setCursor(new ImageCursor(image));
        //---------------------------------

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
     * @return the menuView
     */
    public MenuView getMenuView() {
        return this.menuView;
    }

    /**
     * @return the view
     */
    public TanksMapView getView() {
        return this.view;
    }

    /**
     * @param view the view to set
     */
    public void setView(TanksMapView view) {
        this.view = view;
    }

    /**
     * @return the tank app
     */
    public TanksApp getTankApp() {
        return this.tankApp;
    }

    /**
     * @return the images
     */
    public ImageSupport<TanksImageProperty> getImages() {
        return images;
    }

    /**
     * @return the model of the game
     */
    public Model getModel() {
        return this.model;
    }

    /**
     * Subscriber method according to the subscriber pattern. The method plays audio clips depending on the
     * specified notification if sound is not muted.
     */
    @Override
    public void notify(TanksNotification notification) {
        LOGGER.finer("received " + notification);
        if (!getModel().isMuted())
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
            }
    }

    /**
     * @return playerEnum
     */
    public PlayerEnum getPlayerEnum() {
        return playerEnum;
    }

    /**
     * updates PlayerEnum
     *
     * @param playerEnum new playerEnum
     */
    public void setPlayerEnum(PlayerEnum playerEnum) {
        this.playerEnum = playerEnum;
    }

    /**
     * @return offset
     */
    public long getOffset() {
        return getTankApp().getOffset();
    }

    /**
     * @return connection
     */
    public Connection<IClientMessage, IServerMessage> getConnection() {
        return getTankApp().getConnection();
    }
}
