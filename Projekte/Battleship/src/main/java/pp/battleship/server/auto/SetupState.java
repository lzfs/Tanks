package pp.battleship.server.auto;

import java.util.function.Consumer;

class SetupState extends BattleshipState {
    private final BattleshipAutomaton parent;

    final PlayerXPlacingShips region1 = new PlayerXPlacingShips(this, 0);
    final PlayerXPlacingShips region2 = new PlayerXPlacingShips(this, 1);

    public SetupState(BattleshipAutomaton parent) {
        this.parent = parent;
    }

    @Override
    public BattleshipAutomaton containingState() {
        return parent;
    }

    @Override
    public void handle(Consumer<BattleshipState> method) {
        method.accept(region1);
        method.accept(region2);
    }

    @Override
    public void initializeState() {
        region1.entry();
        region2.entry();
    }

    public void checkTermination() {
        if (region1.isTerminated() && region2.isTerminated())
            parent.goToState(parent.playState);
    }
}
