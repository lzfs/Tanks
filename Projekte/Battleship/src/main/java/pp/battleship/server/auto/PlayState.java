package pp.battleship.server.auto;

import pp.battleship.Resources;
import pp.battleship.model.ClientState;
import pp.battleship.model.Shot;
import pp.battleship.server.Player;
import pp.util.IntVec;

class PlayState extends BattleshipState {
    private final BattleshipAutomaton parent;

    public PlayState(BattleshipAutomaton parent) {
        this.parent = parent;
    }

    @Override
    public BattleshipAutomaton containingState() {
        return parent;
    }

    /**
     * Sets up the state
     */
    @Override
    public void entry() {
        super.entry();
        getAuto().setClientState(getAuto().getActivePlayer(),
                                 Resources.getString("its.your.turn.click.on.the.opponents.field.to.shoot"),
                                 ClientState.SHOOT);
        getAuto().setClientState(getAuto().getActivePlayer().opponent(),
                                 Resources.getString("wait.for.opponent"),
                                 ClientState.WAIT);
    }

    /**
     * Method processes a shot
     *
     * @param p   player initiating the shot
     * @param pos position to shoot
     */
    @Override
    public void clickOpponentMap(Player p, IntVec pos) {
        if (p == getAuto().getActivePlayer()) {
            final Shot shot = p.opponent().getMap().shoot(pos);
            if (getAuto().getActivePlayer().opponent().hasLost())
                parent.goToState(parent.gameOver);
            else if (shot.hit)
                parent.goToState(parent.playState);
            else {
                getAuto().setActivePlayer(getAuto().getActivePlayer().opponent());
                parent.goToState(parent.playState);
            }
        }
    }
}
