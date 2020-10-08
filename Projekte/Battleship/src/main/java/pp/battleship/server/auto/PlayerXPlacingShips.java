package pp.battleship.server.auto;

import pp.battleship.model.Battleship;
import pp.battleship.server.Player;

/**
 * Represents the regions used in SETUP_STATE
 */
class PlayerXPlacingShips extends BattleshipStatemachine {
    private final SetupState parent;

    /**
     * The player who places his ships.
     */
    private final int playerIndex;
    /**
     * The ship within the harbor whose preview is shown in the player's map.
     */
    Battleship selected;

    final BattleshipState selectShip = new SelectShip(this);
    final BattleshipState shipSelected = new ShipSelected(this);
    final BattleshipState invalidPlacement = new InvalidPlacement(this);
    final BattleshipState terminated = new PlacingShipsTerminated(this);

    /**
     * Creates a state for a specific player
     *
     * @param playerIndex specifies the player linked to this region
     */
    PlayerXPlacingShips(SetupState parent, int playerIndex) {
        this.parent = parent;
        this.playerIndex = playerIndex;
    }

    @Override
    public SetupState containingState() {
        return parent;
    }

    @Override
    public String toString() {
        return containingState() + ".Player" + (playerIndex + 1) + "PlacingShips";
    }

    /**
     * Returns the linked player
     *
     * @return player linked to this region
     */
    Player getPlayer() {
        return getAuto().bs.getModel().getPlayer(playerIndex);
    }

    /**
     * Returns the initial state for this region
     *
     * @return initial state
     */
    @Override
    public BattleshipState init() {
        return selectShip;
    }

    boolean isTerminated() {
        return getState() == terminated;
    }

    void terminate() {
        parent.checkTermination();
    }
}
