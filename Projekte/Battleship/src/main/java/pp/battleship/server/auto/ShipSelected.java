package pp.battleship.server.auto;

import pp.battleship.Resources;
import pp.battleship.model.Battleship;
import pp.battleship.model.ClientState;
import pp.battleship.server.Player;
import pp.util.IntVec;

class ShipSelected extends BattleshipState {
    private final PlayerXPlacingShips parent;

    public ShipSelected(PlayerXPlacingShips parent) {
        this.parent = parent;
    }

    @Override
    public PlayerXPlacingShips containingState() {
        return parent;
    }

    /**
     * sets client controls
     */
    @Override
    public void entry() {
        super.entry();
        getAuto().setClientState(parent.getPlayer(),
                                 Resources.getString("place.ship.in.your.map"),
                                 ClientState.PLACE_SHIP);
    }

    /**
     * Method called if a player wants to remove a ship
     *
     * @param p the player
     */
    @Override
    public void remove(Player p) {
        if (p == parent.getPlayer())
            parent.goToState(parent.selectShip);
    }

    /**
     * Method called if a player wants to rotate a ship
     *
     * @param p the player
     */
    @Override
    public void rotate(Player p) {
        if (p == parent.getPlayer()) {
            p.rotatePreview();
            parent.goToState(parent.shipSelected);
        }
    }

    /**
     * Method called if a player clicks the own map
     *
     * @param p the player
     * @param c the position clicked
     */
    @Override
    public void clickOwnMap(Player p, IntVec c) {
        if (p == parent.getPlayer()) {
            p.getPreview().setPos(c);
            if (p.getMap().placedCorrectly(p.getPreview())) {
                parent.selected.setParamsTo(p.getPreview());
                p.getShips().add(parent.selected);
                p.getHarbor().getShips().remove(parent.selected);
                p.getHarbor().orderShips();
                parent.goToState(parent.selectShip);
            }
            else
                parent.goToState(parent.invalidPlacement);
        }
    }

    /**
     * Method called if a player clicks the harbor
     *
     * @param p the player
     * @param c the position clicked
     */
    @Override
    public void clickHarbor(Player p, IntVec c) {
        Battleship ship;
        if (p == parent.getPlayer() && (ship = p.getHarbor().findShipAt(c)) != null) {
            parent.selected = ship;
            p.setPreview(new Battleship(parent.selected.length));
            parent.goToState(parent.shipSelected);
        }
    }
}
