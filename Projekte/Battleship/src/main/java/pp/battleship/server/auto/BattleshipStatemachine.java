package pp.battleship.server.auto;

import pp.automaton.Statemachine;

/**
 * StateMachine for Battleship
 */
abstract class BattleshipStatemachine extends BattleshipState implements Statemachine<BattleshipState> {
    private BattleshipState state;

    /**
     * Returns the current state of the statemachine
     *
     * @return substate
     */
    @Override
    public BattleshipState getState() {
        return state;
    }

    /**
     * Sets the current state of the statemachine
     *
     * @param state the new state
     */
    @Override
    public void setState(BattleshipState state) {
        this.state = state;
    }
}
