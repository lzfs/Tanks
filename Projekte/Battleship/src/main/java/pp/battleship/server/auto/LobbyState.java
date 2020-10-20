package pp.battleship.server.auto;

import pp.battleship.message.server.ServerMessage;
import pp.battleship.server.Player;
import pp.network.IConnection;

class LobbyState extends BattleshipState {
    private final BattleshipAutomaton parent;

    public LobbyState(BattleshipAutomaton parent) {
        this.parent = parent;
    }

    @Override
    public BattleshipAutomaton containingState() {
        return parent;
    }

    @Override
    public void playerConnected(IConnection<ServerMessage> conn) {
        if (!getAuto().isNewRound()) {
            final Player player = getAuto().bs.getModel().addPlayer(conn);
            getAuto().setActivePlayer(player);
        }
        else getAuto().setOneConnected(true);
        parent.goToState(parent.waitingForPlayer2);
    }
}
