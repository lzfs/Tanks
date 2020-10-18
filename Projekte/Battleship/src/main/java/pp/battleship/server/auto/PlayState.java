package pp.battleship.server.auto;

import pp.battleship.Resources;
import pp.battleship.model.ClientState;
import pp.battleship.model.Projectile;
import pp.battleship.model.Shot;
import pp.battleship.server.Player;
import pp.util.IntVec;

class PlayState extends BattleshipState {
    private final BattleshipAutomaton parent;
    private int counter = 0;

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
        Shot shot;
        if (p == getAuto().getActivePlayer()) {
            if (p.getTypeUsed() == Projectile.TYPE1 && p.getAmountType1() > 0) {
                p.setAmountType1(p.getAmountType1() - 1);
                shot = p.opponent().getMap().shoot(pos, Projectile.TYPE1);
            }
            else if (p.getTypeUsed() == Projectile.TYPE2 && p.getAmountType2() > 0) {
                p.setAmountType2(p.getAmountType2() - 1);
                shot = p.opponent().getMap().shoot(pos, Projectile.TYPE2);
            }
            else { // projectile type == normal
                p.setTypeUsed(Projectile.NORMAL);
                shot = p.opponent().getMap().shoot(pos, Projectile.NORMAL);
            }

            if (getAuto().getActivePlayer().opponent().hasLost()) {
                parent.goToState(parent.gameOver);
                counter = 0;
            }
            else if (shot.hit) {
                counter++;
                if (p.opponent().getMap().findShipAt(pos).isDestroyed()) {
                    p.setDestroyed();
                    if (p.getDestroyed() == 0) {
                        p.setAmountType1(p.getAmountType1() + 1);
                    }
                }
                parent.goToState(parent.playState);
            }
            else {
                getAuto().setActivePlayer(getAuto().getActivePlayer().opponent());
                parent.goToState(parent.playState);
                counter = 0;
            }
        }
        if (counter == 4) {
            p.setAmountType2(p.getAmountType2() + 1);
            counter = 0;
            parent.goToState(this);
        }
    }
}
