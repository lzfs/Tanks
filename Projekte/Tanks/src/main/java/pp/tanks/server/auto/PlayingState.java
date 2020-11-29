package pp.tanks.server.auto;

import pp.tanks.model.Model;
import pp.tanks.server.GameMode;
import pp.tanks.server.Player;

import java.util.logging.Logger;

import java.util.List;

/**
 * Playing State from statechart build as a statemachine to host two substates.
 */
public class PlayingState extends TankStateMachine {
    private final TankAutomaton parent;

    /**
     * Constructor to PlayingState class
     *
     * @param parent
     */
    public PlayingState(TankAutomaton parent) {
        this.parent = parent;
    }

    /**
     * The two substates
     */
    final TankState playerDisconnect = new PlayerDisconnectState(this);
    private TankState gameRunning;

    /**
     * initializes a new game
     *
     * @param model    model
     * @param gameMode gameMode
     */
    public void initializeGame(Model model, GameMode gameMode) {
        gameRunning = new GameRunningState(this, model, gameMode);
    }

    /**
     * @return list of all players
     */
    public List<Player> getPlayers() {
        return parent.getPlayers();
    }

    /**
     * Override method mandatory to use methods of StateSupport
     *
     * @return the parent, in this case TankAutomaton
     */
    @Override
    public TankAutomaton containingState() {
        return parent;
    }

    /**
     * Method called upon entering the state
     *
     * @return gameRunning State
     */
    @Override
    public TankState init() {
        return gameRunning;
    }

    /**
     * called when game finished
     */
    public void gameFinished() {
        gameRunning = null;
    }

    /**
     * @return logger
     */
    public Logger getLogger() {
        return parent.getLogger();
    }
}
