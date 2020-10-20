package pp.droids.controller;

import pp.droids.model.DroidsMap;
import pp.droids.model.item.Droid;
import pp.util.DoubleVec;
import pp.util.StopWatch;

import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * The controller realizing the game state when the game is really running.
 */
class PlayGameController extends Controller {
    public static final KeyCode TURN_LEFT = KeyCode.LEFT;
    public static final KeyCode TURN_RIGHT = KeyCode.RIGHT;
    public static final KeyCode MOVE_BACKWARD = KeyCode.DOWN;
    public static final KeyCode MOVE_FORWARD = KeyCode.UP;
    public static final KeyCode FIRE = KeyCode.SPACE;
    public static final KeyCode TOGGLE_DEBUG = KeyCode.D;
    public static final KeyCode INCREASE_SPEED = KeyCode.F;
    public static final KeyCode TOGGLE_MUTING = KeyCode.M;

    private static final Logger LOGGER = Logger.getLogger(PlayGameController.class.getName());

    private final Set<KeyCode> pressed = new HashSet<>();
    private final Set<KeyCode> processed = new HashSet<>();
    private final StopWatch stopWatch = new StopWatch();
    private double lastUpdate;
    private Scene scene;

    /**
     * Creates the controller.
     *
     * @param engine the game engine this controller belongs to
     */
    public PlayGameController(GameEngine engine) {
        super(engine);
    }

    /**
     * Returns the current game map of this game
     */
    private DroidsMap getDroidsMap() {
        return engine.getModel().getDroidsMap();
    }

    /**
     * Returns the droid of this game.
     */
    private Droid getDroid() {
        return getDroidsMap().getDroid();
    }

    /**
     * The handle method of the state pattern. This method is called by the game engine whenever an event happens.
     */
    @Override
    public void handle(Event e) {
        if (e.getEventType() == KeyEvent.KEY_PRESSED) {
            final KeyCode code = ((KeyEvent) e).getCode();
            pressed.add(code);
        }
        else if (e.getEventType() == KeyEvent.KEY_RELEASED) {
            final KeyCode code = ((KeyEvent) e).getCode();
            pressed.remove(code);
            processed.remove(code);
        }
        else if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
            final MouseEvent me = (MouseEvent) e;
            DoubleVec pos = engine.getView().viewToModel(me.getX(), me.getY());
            getDroid().navigateTo(pos);
        }
    }

    /**
     * Called once per frame to process keys, update the model, and
     * activate the next state if the game is over.
     */
    @Override
    public void update() {

        // process input events that occurred since the last game step
        for (KeyCode k : pressed)
            keyPressed(k);

        // update the model
        final double delta = stopWatch.getTime() - lastUpdate;
        lastUpdate = stopWatch.getTime();
        engine.getModel().update(delta);

        if (engine.getModel().gameWon())
            engine.activateGameWonController();
        else if (engine.getModel().gameLost())
            engine.activateGameLostController();
        else if (pressed.contains(KeyCode.ESCAPE))
            engine.activateMenuController();
    }

    /**
     * This method is called whenever this controller is activated, i.e., when the game state is entered.
     */
    @Override
    public void entry() {
        engine.getModel().setShowHint();
        stopWatch.start();
        pressed.clear();
        processed.clear();
        if (scene == null)
            scene = new Scene(new Group(engine.getView()));
        engine.setScene(scene);
    }

    /**
     * This method is called whenever this controller is deactivated, i.e., when the game state is left.
     */
    @Override
    void exit() {
        stopWatch.stop();
    }

    /**
     * Handles any keyboard events.
     *
     * @param kc key code
     **/
    private void keyPressed(KeyCode kc) {
        if (kc == TURN_LEFT)
            getDroid().setTurnLeft(true);
        else if (kc == TURN_RIGHT)
            getDroid().setTurnRight(true);
        else if (kc == MOVE_FORWARD)
            getDroid().setMoveForward(true);
        else if (kc == MOVE_BACKWARD)
            getDroid().setMoveBackward(true);
        else if (kc == FIRE)
            getDroid().fire();
        else if (kc == TOGGLE_DEBUG) {
            if (!processed.contains(kc)) {
                engine.getModel().setDebugMode(!engine.getModel().isDebugMode());
                processed.add(kc);
            }
        }
        else if (kc == INCREASE_SPEED) {
            if (!processed.contains(kc)) {
                getDroidsMap().increaseMovingItemsSpeed(1);
                processed.add(kc);
            }
        }
        else if (kc == TOGGLE_MUTING) {
            if (!processed.contains(kc)) {
                engine.getModel().setMuted(!engine.getModel().isMuted());
                processed.add(kc);
            }
        }
    }

    /**
     * getter methode for the stopwatch
     *
     * @return the Stopwatch
     */
    @Override
    public StopWatch getStopWatch() {
        return stopWatch;
    }

    /**
     * setter method for the last updated time
     *
     * @param lastUpdate the new last updated time
     */
    @Override
    public void setLastUpdate(double lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
