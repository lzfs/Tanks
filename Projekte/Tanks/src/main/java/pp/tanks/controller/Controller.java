package pp.tanks.controller;

import pp.tanks.message.data.TankData;
import pp.tanks.model.item.ItemEnum;
import pp.tanks.model.item.MoveDirection;
import pp.util.DoubleVec;

import javafx.event.Event;

import java.util.List;

/**
 * Base class of all controllers of the tanks game
 */
public class Controller {
    final Engine engine;

    Controller(Engine engine) {this.engine = engine;}

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
    public void handle(Event event) {}

    /**
     * used to start an action after both players are synchronized
     */
    public void synchronizationFinished() {}

    /**
     * used to indicate if a player is disconnected
     */
    public void playerDisconnected() {}

    /**
     * used to indicate that a connection was lost
     */
    public void lostConnection() {}

    /**
     * loads the second level of the game
     */
    protected void loadLevelTwo() {
        TankData enemy1 = new TankData(new DoubleVec(20, 4), 1, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        TankData enemy2 = new TankData(new DoubleVec(20, 8), 2, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        TankData enemy3 = new TankData(new DoubleVec(10, 6), 3, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        engine.playGameController.constructionEnum.addAll(List.of(ItemEnum.ACP, ItemEnum.HOWITZER, ItemEnum.TANK_DESTROYER));
        engine.playGameController.constructionData.addAll(List.of(enemy1, enemy2, enemy3));
    }

    /**
     * called when level one gets loaded
     */
    protected void loadLevelOne() {
        TankData enemy1 = new TankData(new DoubleVec(18, 7), 1, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        TankData enemy2 = new TankData(new DoubleVec(20, 5), 3, 20, MoveDirection.STAY, 0, new DoubleVec(0, 0), false);
        engine.playGameController.constructionEnum.addAll(List.of(ItemEnum.ACP, ItemEnum.HOWITZER));
        engine.playGameController.constructionData.addAll(List.of(enemy1, enemy2));
    }

}
