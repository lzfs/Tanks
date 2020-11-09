package pp.tanks.server.auto;

import pp.automaton.Statemachine;

public abstract class TankStateMachine extends TankState implements Statemachine<TankState> {
    private TankState state;

    /**
     * Returns the current state of the statemachine
     *
     * @return substate
     */
    @Override
    public TankState getState() {
        return state;
    }

    /**
     * Sets the current state of the statemachine
     *
     * @param state the new state
     */
    @Override
    public void setState(TankState state) {
        this.state = state;
    }

}
