package pp.droids.controller;

import pp.util.StopWatch;

import javafx.event.Event;

/**
 * Base class of all controllers of the droids game
 */
class Controller {
    final GameEngine engine;

    Controller(GameEngine engine) {this.engine = engine;}

    /**
     * This method is periodically called by the game loop.
     */
    void update() {}

    /**
     * This method is called whenever this controller is activated, i.e., when the game state realized by
     * this controller is entered.
     */
    void entry() {}

    /**
     * This method is called whenever this controller is deactivated, i.e., when a game has been in the state
     * realized by this controller, but this state is left and a different state is entered.
     */
    void exit() {}

    /**
     * The handle method of the state pattern. This method is called by the game engine whenever an event happens.
     *
     * @param event the event that happened and that is passed to this controller.
     */
    public void handle(Event event) { }

    public StopWatch getStopWatch() {
        return null;
    }

    public void setLastUpdate(double lastUpdate) { }
}
