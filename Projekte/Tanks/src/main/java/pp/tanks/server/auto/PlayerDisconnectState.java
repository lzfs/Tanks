package pp.tanks.server.auto;

/**
 * TankState entered when one Players disconnects due to network problems
 */
public class PlayerDisconnectState extends TankState {
    private final PlayingState parent;

    /**
     * Constructor of the PlayerDisconnectState
     *
     * @param parent in this case, the PlayingState
     */
    public PlayerDisconnectState(PlayingState parent) {
        this.parent = parent;
    }

    /**
     * Override method mandatory to use methods of StateSupport
     *
     * @return the parent, in this case PlayingState
     */
    @Override
    public PlayingState containingState() {
        return parent;
    }

    /**
     * Method called upon entering the state
     *
     * @return Atm just a println statement to let us know that we entered the state successfully
     */
    @Override
    public void entry() {
        parent.getLogger().info("Player Disconnected");
    }
}
