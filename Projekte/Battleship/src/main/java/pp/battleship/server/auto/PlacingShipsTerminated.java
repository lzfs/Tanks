package pp.battleship.server.auto;

class PlacingShipsTerminated extends BattleshipState {
    private final PlayerXPlacingShips parent;

    public PlacingShipsTerminated(PlayerXPlacingShips parent) {
        this.parent = parent;
    }

    @Override
    public PlayerXPlacingShips containingState() {
        return parent;
    }

    @Override
    public void entry() {
        super.entry();
        parent.terminate();
    }
}
