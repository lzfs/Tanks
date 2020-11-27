package pp.tanks.controller;

import pp.tanks.TanksImageProperty;
import pp.tanks.message.data.BBData;
import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.data.ProjectileData;
import pp.tanks.message.data.TankData;
import pp.tanks.message.server.GameEndingMessage;
import pp.tanks.model.ICollisionObserver;
import pp.tanks.model.TanksMap;
import pp.tanks.model.item.BreakableBlock;
import pp.tanks.model.item.COMEnemy;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.LightArmor;
import pp.tanks.model.item.LightTurret;
import pp.tanks.model.item.MoveDirection;
import pp.tanks.model.item.PlayerEnum;
import pp.tanks.model.item.PlayersTank;
import pp.tanks.model.item.Projectile;
import pp.tanks.model.item.Tank;
import pp.tanks.server.GameMode;
import pp.tanks.view.TanksMapView;
import pp.util.DoubleVec;
import pp.util.StopWatch;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * The controller realizing the game state when the game is really running.
 */
public class PlayGameController extends Controller implements ICollisionObserver {
    private static final Logger LOGGER = Logger.getLogger(PlayGameController.class.getName());

    public static final KeyCode W = KeyCode.W;
    public static final KeyCode A = KeyCode.A;
    public static final KeyCode S = KeyCode.S;
    public static final KeyCode D = KeyCode.D;

    private final List<KeyCode> pressed = new ArrayList<>();
    private final Set<KeyCode> processed = new HashSet<>();
    private final StopWatch stopWatch = new StopWatch();
    private double lastUpdate;
    private Scene scene;
    private boolean stopFlag = false;
    public final List<ItemEnum> constructionEnum = new ArrayList<>();
    public final List<TankData> constructionData = new ArrayList<>();
    private final List<DataTimeItem<ProjectileData>> projectiles = new ArrayList<>();
    private final List<DataTimeItem<TankData>> tanks = new ArrayList<>();
    private final List<BBData> bbDataList = new ArrayList<>();
    private GameEndingMessage endingMessage = null;
    private Scene menuMPController = new Scene(new PauseMenuMPController());

    /**
     * create a new PlayGameController
     *
     * @param engine the game engine this controller belongs to
     */
    public PlayGameController(Engine engine) {
        super(engine);
    }

    /**
     * Returns the current game map of this game
     */
    private TanksMap getTanksMap() {
        return engine.getModel().getTanksMap();
    }

    /**
     * The handle method of the state pattern. This method is called by the game engine whenever an event happens.
     */
    @Override
    public void handle(Event e) {
        if (engine.getView() == null) return;
        if (e.getEventType() == KeyEvent.KEY_PRESSED) {
            final KeyCode code = ((KeyEvent) e).getCode();
            if (!pressed.contains(code)) {
                pressed.add(code);
            }
        }
        else if (e.getEventType() == KeyEvent.KEY_RELEASED) {
            final KeyCode code = ((KeyEvent) e).getCode();
            pressed.remove(code);
            processed.remove(code);

            if (pressed.size() == 0) {
                stopFlag = true;
            }
        }
        else if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
            final MouseEvent me = (MouseEvent) e;
            DoubleVec pos = engine.getView().viewToModel(me.getX(), me.getY());
            getTank().shoot(pos);
        }
        else if (e.getEventType() == MouseEvent.MOUSE_MOVED) {
            MouseEvent event = (MouseEvent) e;
            double x1 = event.getX();
            double y1 = event.getY();
            DoubleVec dir = engine.getView().viewToModel(x1, y1).sub(getTank().getPos());
            //maybe norm
            //getTank().getTurret().setDirection(dir.normalize());
            getTank().getData().setTurretDir(dir.normalize());
            //für model umwandeln und turret als DoubleVec übergeben
        }
    }

    /**
     * Called once per frame to process keys, update the model, and
     * activate the next state if the game is over.
     */
    @Override
    public void update() {
        if (endingMessage != null) gameEnd();
        // process input events that occurred since the last game step
        if (pressed.size() >= 2) {
            keyPressed(pressed.get(0), pressed.get(1));
        }
        else if (pressed.size() == 1) {
            keyPressed(pressed.get(0));
        }
        else {
            getTank().setMove(false);
        }
        if (stopFlag) {
            getTank().stopMovement();
            stopFlag = false;
        }
        processEnemyTanks();
        //put new projectiles from the server into the game
        addProjectiles();
        handleCollision();

        if (engine.getMode() != GameMode.MULTIPLAYER) {
            if (engine.getModel().gameWon()) {
                handleLocalGameWon();
            }

            else if (engine.getModel().gameLost()) {
                handleLocalGameLost();
            }
            else if (pressed.contains(KeyCode.ESCAPE)) {
                engine.activatePauseMenuSPController(); //TODO
            }
        }
        else {
            if (pressed.contains(KeyCode.ESCAPE)) engine.setScene(menuMPController);
        }

        engine.getModel().update(System.nanoTime() + engine.getOffset());

        if (engine.getView() != null || engine.getMode() == GameMode.MULTIPLAYER) {
            engine.getView().updateProgressBar(((1.0 * engine.getModel().getTanksMap().getTank(engine.getPlayerEnum()).getArmor().getArmorPoints() / engine.getModel().getTanksMap().getTank(engine.getPlayerEnum()).getArmor().getMaxPoints())));
        }
    }

    /**
     * This method is called whenever this controller is activated, i.e., when the game state is entered.
     */
    @Override
    public void entry() {
        pressed.clear();
        processed.clear();
        engine.getModel().loadMap("map" + engine.getMapCounter() + ".xml");

        if (engine.getMapCounter() == 0 || engine.getMapCounter() == 3) {
            PlayersTank tank = new PlayersTank(engine.getModel(), 3, new LightArmor(), new LightTurret(), new TankData(new DoubleVec(3, 6), 0, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false));
            engine.getModel().setTank(tank);
        }
        else {
            if (engine.getMode() == GameMode.SINGLEPLAYER) {
                engine.getSaveTank().setDestroyed(false);
                engine.getSaveTank().setPos(new DoubleVec(3, 6));
                engine.getModel().setTank(engine.getSaveTank());
                loadSingleplayerEnemy();
            }
            else {
                if (engine.getPlayerEnum() == PlayerEnum.PLAYER1) {
                    engine.getSaveTank().setDestroyed(false);
                    engine.getModel().setTank(engine.getSaveTank());
                    engine.getSaveEnemyTank().setDestroyed(false);
                    engine.getModel().setTank(engine.getSaveEnemyTank());
                }
                else {
                    engine.getSaveEnemyTank().setDestroyed(false);
                    engine.getModel().setTank(engine.getSaveEnemyTank());
                    engine.getSaveTank().setDestroyed(false);
                    engine.getModel().setTank(engine.getSaveTank());
                }
            }
        }
        TanksMapView mapview = new TanksMapView(engine.getModel(), engine.getImages());
        engine.setView(mapview);

        if (scene == null) {
            ProgressBar progressBar = new ProgressBar(1.0);
            engine.getView().setProgressBar(progressBar);
            Group group = new Group(engine.getView(), progressBar);
            scene = new Scene(group);
        }
        if (engine.isClientGame()) {
            engine.getModel().getTanksMap().addObserver(this);
        }
        engine.setScene(scene);
        engine.getModel().getTanksMap().updateHashMap();
    }

    /**
     * loads new COM-Enemy
     */
    private void loadSingleplayerEnemy() {
        if (!constructionEnum.isEmpty()) {
            for (int i = 0; i < constructionEnum.size(); i++) {
                engine.getModel().getTanksMap().addCOMTank(COMEnemy.mkComEnemy(constructionEnum.get(i), engine.getModel(), constructionData.get(i)));
            }
        }
        constructionData.clear();
        constructionEnum.clear();
    }

    /**
     * This method is called whenever this controller is deactivated, i.e., when the game state is left.
     */
    @Override
    void exit() {
        System.out.println("exit playgame");
        scene = null;
        stopWatch.stop();
    }

    /**
     * Handles any keyboard events with two keys pressed.
     *
     * @param k1 key code
     * @param k2 key code
     **/
    private void keyPressed(KeyCode k1, KeyCode k2) {
        if ((k1 == W && k2 == A) || (k1 == A && k2 == W)) {
            getTank().setMove(true);
            getTank().setMoveDirection(MoveDirection.LEFT_UP);
        }
        else if ((k1 == W && k2 == D) || (k1 == D && k2 == W)) {
            getTank().setMove(true);
            getTank().setMoveDirection(MoveDirection.RIGHT_UP);
        }
        else if ((k1 == D && k2 == S) || (k1 == S && k2 == D)) {
            getTank().setMove(true);
            getTank().setMoveDirection(MoveDirection.RIGHT_DOWN);
        }
        else if ((k1 == S && k2 == A) || (k1 == A && k2 == S)) {
            getTank().setMove(true);
            getTank().setMoveDirection(MoveDirection.LEFT_DOWN);
        }
    }

    /**
     * Handles any keyboard events with one key pressed.
     *
     * @param k1 key code
     **/
    private void keyPressed(KeyCode k1) {
        if (k1 == W) {
            getTank().setMove(true);
            getTank().setMoveDirection(MoveDirection.UP);
        }
        else if (k1 == A) {
            getTank().setMove(true);
            getTank().setMoveDirection(MoveDirection.LEFT);
        }

        else if (k1 == S) {
            getTank().setMove(true);
            getTank().setMoveDirection(MoveDirection.DOWN);
        }
        else if (k1 == D) {
            getTank().setMove(true);
            getTank().setMoveDirection(MoveDirection.RIGHT);
        }
    }

    /**
     * @return the players tank
     */
    private Tank getTank() {
        return engine.getModel().getTanksMap().getTank(engine.getPlayerEnum());
    }

    /**
     * creates for each projectile in the list "projectiles" a new "existing" projectile
     */
    private void addProjectiles() {
        if (projectiles.isEmpty()) return;
        List<DataTimeItem<ProjectileData>> tmpList = new ArrayList<>(projectiles);
        projectiles.clear();
        for (DataTimeItem<ProjectileData> item : tmpList) {
            Projectile p = getTanksMap().getHashProjectiles().get(item.data.id);
            if (p == null) {
                p = Projectile.mkProjectile(engine.getModel(), item.data);
                engine.getModel().getTanksMap().addProjectile(p);
            }
            if (item.data.isDestroyed()) {
                p.destroy();
            }
            p.interpolateData(item);
        }
    }

    /**
     * TODO: add JavaDoc
     */
    private void processEnemyTanks() {
        if (tanks.isEmpty()) return;
        List<DataTimeItem<TankData>> tmpList = new ArrayList<>(tanks);
        tanks.clear();
        for (DataTimeItem<TankData> item : tmpList) {
            Tank tmp = engine.getModel().getTanksMap().getAllTanks().get(item.getId());
            tmp.interpolateData(item);
        }
    }

    /**
     * adds elements to the projectiles-list
     *
     * @param list processing projectiles
     */
    public void addServerProjectiles(List<DataTimeItem<ProjectileData>> list) {
        this.projectiles.addAll(list);
    }

    /**
     * adds enemy-actions to the enemyTanks list
     *
     * @param list processing enemy-actions
     */
    public void addServerEnemyData(List<DataTimeItem<TankData>> list) {
        this.tanks.addAll(list);
    }

    public void addServerBBlockData(List<BBData> list) {
        this.bbDataList.addAll(list);
    }

    public void handleCollision() {
        if (bbDataList.isEmpty()) return;
        List<BBData> tmpList = new ArrayList<>(bbDataList);
        bbDataList.clear();
        for (BBData bbData : tmpList) {
            BreakableBlock tmp = (BreakableBlock) engine.getModel().getTanksMap().getFromID(bbData.id);
            tmp.interpolateData(new DataTimeItem<>(bbData, 0));
        }
    }

    public void setGameEnd(GameEndingMessage msg) {
        endingMessage = msg;
    }

    public void gameEnd() {
        if (endingMessage.won) engine.activateGameWonMPController();
        else engine.activateGameOverMPController();
        endingMessage = null;
    }

    public void handleLocalGameWon() {
        engine.setView(null);
        if (engine.getMode() == GameMode.TUTORIAL) {
            engine.activateLevelController();
        }
        else {
            if (engine.getMapCounter() == 1) {
                engine.activateMission1CompleteController();
            }
            else {
                engine.activateMission2CompleteController();
            }
        }
        engine.getModel().getTanksMap().deleteAllObservers();
    }

    public void handleLocalGameLost() {
        engine.setView(null);
        if (engine.getMode() == GameMode.TUTORIAL) {
            engine.activateLevelController();
        }
        else {
            engine.getSaveTank().decreaseLives();
            if (engine.getSaveTank().getLives() > 0) {
                engine.activateStartGameSPController();
            }
            else {
                engine.activateGameOverSPController();
            }
        }
        engine.getModel().getTanksMap().deleteAllObservers();
    }

    @Override
    public void notifyProjTank(Projectile proj, Tank tank, int damage, boolean dest) {
        if (dest) {
            tank.destroy();
        }
        else {
            tank.processDamage(damage);
        }
        proj.destroy();
    }

    @Override
    public void notifyProjBBlock(Projectile proj, BreakableBlock block, int damage, boolean dest) {
        if (dest) {
            block.destroy();
        }
        else {
            block.processDamage(damage);
        }
        proj.destroy();
    }

    @Override
    public void notifyProjProj(Projectile proj1, Projectile proj2) {
        proj1.destroy();
        proj2.destroy();
    }

    @Override
    public void playerDisconnected() {
        engine.gameWonMPController.playerDisconnected();
    }

    private class PauseMenuMPController extends GridPane {
        private static final String PAUSE_MENU_MP_FXML = "PauseMenuMP.fxml"; //NON-NLS

        PauseMenuMPController() {
            final URL location = getClass().getResource(PAUSE_MENU_MP_FXML);
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            try {
                fxmlLoader.load();
            }
            catch (
                    IOException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * the button to continue the game
         */
        @FXML
        private Button continueGame;

        /**
         * the image to display the status icon of the music preferences
         */
        @FXML
        private ImageView musicImage;

        /**
         * the image to display the status icon of the sound preferences
         */
        @FXML
        private ImageView soundImage;

        /**
         * the button to get back to the main menu
         */
        @FXML
        private Button mainMenu;

        /**
         * method to continue the game
         */
        @FXML
        private void continueGame() {
            engine.setScene(scene);
        }

        /**
         * method to go back to the main menu
         */
        @FXML
        private void mainMenu() {
            engine.getConnection().shutdown();
            engine.activateMainMenuController();
        }

        /**
         * method for the sound button
         */
        @FXML
        private void sound() {
            // TODO
            /*if (engine.getTankApp().sounds.getMuted()) {
                engine.getTankApp().sounds.mute(false);
            }
            else {
                engine.getTankApp().sounds.mute(true);
            }

            if (engine.getTankApp().sounds.getMuted()) {
                soundImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOff));
            }
            else {
                soundImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOn));
            }
            LOGGER.log(Level.INFO, "clicked SOUND");*/
        }

        /**
         * method for the music button
         */
        @FXML
        private void music() {
            //TODO
           /* engine.getTankApp().sounds.mute(!engine.getTankApp().sounds.getMuted());
            if (engine.getTankApp().sounds.getMuted()) {
                musicImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOff));
            }
            else {
                musicImage.setImage(engine.getImages().getImage(TanksImageProperty.soundOn));
            }
            LOGGER.log(Level.INFO, "clicked MUSIC");*/
        }
    }
}
