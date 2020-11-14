package pp.tanks.server.auto;

import pp.tanks.message.client.BackMessage;
import pp.tanks.server.Player;

public class PlayerReadyState extends TankState {
    private final TankAutomaton parent;

    public PlayerReadyState(TankAutomaton parent){
        this.parent = parent;
    }
    /**
     * the state where the players choose their tank
     */
    @Override
    public TankAutomaton containingState() {
        return this.parent;
    }

    @Override
    public void entry() {
        System.out.println("Player Ready State");
    }

    @Override
    public void back(BackMessage msg) {
        for (Player p : parent.getPlayers()) {
            p.getConnection().shutdown();
        }
        parent.getPlayers().clear();
        containingState().goToState(parent.init);
    }
}
