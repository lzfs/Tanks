package pp.automaton;

import java.util.function.Consumer;

/**
 * A generic interface for statemachines ("or-states")
 *
 * @param <S> the class implementing states in a concrete application
 */
public interface Statemachine<S extends State<S>> extends State<S> {
    /**
     * Returns the initial state of this statemachine
     */
    S init();

    /**
     * Returns the current state of this statemachine
     */
    S getState();

    /**
     * Sets the current state of this statemachine
     */
    void setState(S state);

    /**
     * Applies the given method to the current state of this statemachine if it has not yet terminated.
     *
     * @param method the method to be applied to the current state
     */
    @Override
    default void handle(Consumer<S> method) {
        method.accept(getState());
    }

    /**
     * Leaves the current state (calling its {@linkplain State#exit()}-method) and enters the new state,
     * calling its {@linkplain State#entry()}-method. It is possible to actually go to the current, which calls
     * its {@linkplain State#exit()}-method and then its {@linkplain State#entry()}-method.
     *
     * @param toState the new state to go to
     */
    default void goToState(S toState) {
        StateSupport.LOGGER.fine(() -> String.format("%s --> %s", getState(), toState));
        getState().exit();
        setState(toState);
        getState().entry();
    }

    /**
     * Method called when entering this statemachine. This method actually sets its initial state
     * (see {@linkplain #init()}) as its current state and calls its {@linkplain State#entry()}-method
     */
    @Override
    default void initializeState() {
        setState(init());
        getState().entry();
    }
}