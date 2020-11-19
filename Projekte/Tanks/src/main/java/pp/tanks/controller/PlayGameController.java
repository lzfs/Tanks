package pp.tanks.controller;

import pp.tanks.message.data.DataTimeItem;
import pp.tanks.message.data.ProjectileData;
import pp.tanks.message.data.TankData;
import pp.tanks.model.TanksMap;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * The controller realizing the game state when the game is really running.
 */
public class PlayGameController extends Controller {
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
    private final List<DataTimeItem<ProjectileData>> projectiles = new ArrayList<>();
    private final List<DataTimeItem<TankData>> enemyTanks = new ArrayList<>();
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

        // update the model
        /*final double delta = stopWatch.getTime() - lastUpdate;
        lastUpdate = stopWatch.getTime();*/

        engine.getModel().update(System.nanoTime() + engine.getOffset());

        if (engine.getModel().gameWon()) {
            engine.setView(null);
            if (engine.getMode() == GameMode.TUTORIAL) {
                engine.activateLevelController();
            }
            else if (engine.getMode() == GameMode.SINGLEPLAYER) {
                if (engine.getMapCounter() == 1) {
                    engine.activateMission1CompleteController();
                }
                else {
                    engine.activateMission2CompleteController();
                }
            }
            else {
                //Multiplayer TODO
                System.out.println("FEHLER1");
            }
        }
        else if (engine.getModel().gameLost()) {
            engine.setView(null);
            if (engine.getMode() == GameMode.TUTORIAL) {
                engine.activateLevelController();
            }
            else if (engine.getMode() == GameMode.SINGLEPLAYER) {
                engine.getSaveTank().decreaseLives();
                if (engine.getSaveTank().getLives() > 0) {
                    engine.activateStartGameSPController();
                }
                else {
                    engine.activateGameOverSPController();
                }
            }
            else {
                //Multiplayer
                //TODO
                System.out.println("FEHLER2");
            }
        }
        else if (pressed.contains(KeyCode.ESCAPE))
            engine.activatePauseMenuSPController(); //TODO
    }

    /**
     * This method is called whenever this controller is activated, i.e., when the game state is entered.
     */
    @Override
    public void entry() {
        stopWatch.start();
        pressed.clear();
        processed.clear();
        engine.getModel().loadMap("map" + engine.getMapCounter() + ".xml");

        if (engine.getMapCounter() == 0 || engine.getMapCounter() == 3) {
            if (engine.getMapCounter() == 3) engine.getModel().setDebug(true);
            PlayersTank tank = new PlayersTank(engine.getModel(), 1, new LightArmor(), new LightTurret(), new TankData(new DoubleVec(3, 6), 0, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0)));
            engine.getModel().setTank(tank);
        }
        else {
            if (engine.getMode() == GameMode.SINGLEPLAYER) {
                engine.getSaveTank().setDestroyed(false);
                engine.getSaveTank().setPos(new DoubleVec(3, 6));
                engine.getModel().setTank(engine.getSaveTank());
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
            scene = new Scene(new Group(engine.getView()));
        }
        engine.setScene(scene);
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
        for (DataTimeItem<ProjectileData> item : projectiles) {
            Projectile p = Projectile.mkProjectile(engine.getModel(), item.data);
            engine.getModel().getTanksMap().addProjectile(p);
            p.interpolateData(item);
        }
        projectiles.clear();
    }

    /**
     * TODO: add JavaDoc
     */
    private void processEnemyTanks() {
        if (enemyTanks.isEmpty()) return;
        for (DataTimeItem<TankData> item : enemyTanks) {
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
        this.enemyTanks.addAll(list);
    }
}
