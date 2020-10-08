package pp.battleship.server.auto;

import pp.battleship.message.server.ServerMessage;
import pp.network.IConnection;

class WaitingForPlayer2 extends BattleshipState {
    private final BattleshipAutomaton parent;

    public WaitingForPlayer2(BattleshipAutomaton parent) {
        this.parent = parent;
    }

    @Override
    public BattleshipAutomaton containingState() {
        return parent;
    }

    /**
     * Called when the second player connected
     *
     * @param conn connection to the second client
     */
    @Override
    public void playerConnected(IConnection<ServerMessage> conn) {
        getAuto().bs.getModel().addPlayer(conn);
        parent.goToState(parent.setupState);
    }
}
