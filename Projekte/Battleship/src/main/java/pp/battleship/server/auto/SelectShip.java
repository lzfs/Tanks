package pp.battleship.server.auto;

import pp.battleship.Resources;
import pp.battleship.model.Battleship;
import pp.battleship.model.ClientState;
import pp.battleship.server.Player;
import pp.util.IntVec;

class SelectShip extends BattleshipState {
    private final PlayerXPlacingShips parent;

    public SelectShip(PlayerXPlacingShips parent) {
        this.parent = parent;
    }

    @Override
    public PlayerXPlacingShips containingState() {
        return parent;
    }

    /**
     * Used to clean up and realize conditional transitions
     */
    @Override
    public void entry() {
        super.entry();
        parent.selected = null;
        parent.getPlayer().setPreview(null);
        if (parent.getPlayer().getHarbor().getShips().isEmpty())
            getAuto().setClientState(parent.getPlayer(),
                                     Resources.getString("select.a.ship.or.press.ready"),
                                     ClientState.ALL_PLACED);
        else
            getAuto().setClientState(parent.getPlayer(),
                                     Resources.getString("select.a.ship"),
                                     ClientState.SELECT_SHIP);
    }

    /**
     * Processes click on harbor
     *
     * @param p player that clicked
     * @param c position clicked
     */
    @Override
    public void clickHarbor(Player p, IntVec c) {
        if (p == parent.getPlayer() && (parent.selected = p.getHarbor().findShipAt(c)) != null) {
            p.setPreview(new Battleship(parent.selected.length));
            parent.goToState(parent.shipSelected);
        }
    }

    /**
     * Processes click on own map
     *
     * @param p player that clicked
     * @param c position clicked
     */
    @Override
    public void clickOwnMap(Player p, IntVec c) {
        if (p == parent.getPlayer() && (parent.selected = p.getMap().findShipAt(c)) != null) {
            p.getShips().remove(parent.selected);
            p.getHarbor().getShips().add(parent.selected);
            p.getHarbor().orderShips();
            p.setPreview(new Battleship(parent.selected.length));
            parent.goToState(parent.shipSelected);
        }
    }

    /**
     * method called if a player clicks ready
     *
     * @param p the player
     */
    @Override
    public void ready(Player p) {
        if (p == parent.getPlayer() && p.getHarbor().getShips().isEmpty()) {
            getAuto().setClientState(p, Resources.getString("wait.for.opponent"), ClientState.WAIT);
            parent.goToState(parent.terminated);
        }
    }
}
