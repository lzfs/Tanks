package pp.battleship.server.auto;

import pp.battleship.Resources;
import pp.battleship.model.ClientState;
import pp.battleship.server.Player;

class GameOver extends BattleshipState {
    private final BattleshipAutomaton parent;

    public GameOver(BattleshipAutomaton parent) {
        this.parent = parent;
    }

    @Override
    public BattleshipAutomaton containingState() {
        return parent;
    }

    /**
     * Sets the client state to WON and sends out the full map
     *
     * @param p the target player
     */
    private void hasWon(Player p) {
        p.setState(ClientState.WON);
        p.setInfoText(Resources.getString("you.won.the.game"));
        getAuto().bs.sendGameOverMap(p);
    }

    /**
     * Sets the client state to LOST and sends out the full map
     *
     * @param p the target player
     */
    private void hasLost(Player p) {
        p.setState(ClientState.LOST);
        p.setInfoText(Resources.getString("you.lost.the.game"));
        getAuto().bs.sendGameOverMap(p);
    }

    @Override
    public void entry() {
        super.entry();
        BattleshipAutomaton.LOGGER.info(getAuto().getActivePlayer() + " has won"); //NON-NLS
        hasWon(getAuto().getActivePlayer());
        hasLost(getAuto().getActivePlayer().opponent());
    }
}
