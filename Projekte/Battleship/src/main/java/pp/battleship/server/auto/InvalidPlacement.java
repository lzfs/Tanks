package pp.battleship.server.auto;

import pp.battleship.Resources;
import pp.battleship.model.ClientState;
import pp.battleship.server.Player;

class InvalidPlacement extends BattleshipState {
    private final PlayerXPlacingShips parent;

    public InvalidPlacement(PlayerXPlacingShips parent) {
        this.parent = parent;
    }

    @Override
    public PlayerXPlacingShips containingState() {
        return parent;
    }

    /**
     * Sets client controls
     */
    @Override
    public void entry() {
        super.entry();
        getAuto().setClientState(parent.getPlayer(), Resources.getString("invalid.ship.placement"), ClientState.INVALID_PLACEMENT);
    }

    /**
     * Method called when a Payer sends the confirm
     *
     * @param p the player
     */
    @Override
    public void confirm(Player p) {
        if (p == parent.getPlayer())
            parent.goToState(parent.selectShip);
    }
}
