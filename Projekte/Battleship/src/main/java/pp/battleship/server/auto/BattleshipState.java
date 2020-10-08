package pp.battleship.server.auto;

import pp.automaton.StateSupport;
import pp.battleship.message.server.ServerMessage;
import pp.battleship.server.Player;
import pp.network.IConnection;
import pp.util.IntVec;

/**
 * State for Battleship
 */
abstract class BattleshipState extends StateSupport<BattleshipState> {
    BattleshipAutomaton getAuto() {
        return containingState().getAuto();
    }

    //all possible methods used to interact with the automaton with default implementation

    public void playerConnected(IConnection<ServerMessage> conn) { handle(s -> s.playerConnected(conn)); }

    public void remove(Player p) { handle(s -> s.remove(p)); }

    public void rotate(Player p) { handle(s -> s.rotate(p)); }

    public void clickOwnMap(Player p, IntVec c) { handle(s -> s.clickOwnMap(p, c)); }

    public void clickOpponentMap(Player p, IntVec pos) { handle(s -> s.clickOpponentMap(p, pos)); }

    public void clickHarbor(Player p, IntVec c) { handle(s -> s.clickHarbor(p, c)); }

    public void ready(Player p) { handle(s -> s.ready(p)); }

    public void confirm(Player p) { handle(s -> s.confirm(p)); }
}
