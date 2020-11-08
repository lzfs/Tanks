package pp.tanks.server.auto;

/**
 * Playing State from statechart build as a statemachine to host two substates.
 *
 */
public class PlayingState extends TankStateMachine {
    private final TankAutomaton parent;

    /**
     * Constructor to PlayingState class
     * @param parent
     */
    public PlayingState (TankAutomaton parent) {
        this.parent = parent;
    }

    /**
     * The two substates
     */
    final TankState playerDisconnect = new PlayerDisconnectState(this);
    final TankState gameRunning = new GameRunningState(this);

    /**
     * Override method mandatory to use methods of StateSupport
     * @return the parent, in this case TankAutomaton
     */
    @Override
    public TankAutomaton containingState() {
        return parent;
    }

    /**
     * Method called upon entering the state
     * @return gameRunning State
     */
    @Override
    public TankState init() {
        return gameRunning;
    }
}
