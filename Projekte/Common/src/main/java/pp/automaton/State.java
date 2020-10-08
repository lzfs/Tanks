package pp.automaton;

import java.util.function.Consumer;

/**
 * A generic interface for states
 *
 * @param <S> the class implementing states in a concrete application
 */
public interface State<S extends State<? extends S>> {
    /**
     * Method that takes a method to be applied to some state. The default implementation is empty
     * for regular states. Subinterfaces for statemachines and orthogonal states, however, override this method.
     *
     * @param method the method to be applied to a state
     */
    default void handle(Consumer<S> method) {}

    /**
     * Initializes this state. Called by {@linkplain #entry()}.
     */
    default void initializeState() {}

    /**
     * Method called when entering this state.
     */
    void entry();

    /**
     * Method called immediately before leaving this state.
     */
    void exit();

    /**
     * Returns the containing state.
     *
     * @return the containing state or null if this state is not contained.
     */
    S containingState();
}