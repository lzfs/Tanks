package pp.automaton;

import java.util.logging.Logger;

/**
 * Base class for implementing states in a concrete application, which also must implement interface
 * {@linkplain pp.automaton.State}. It provides some default implementations
 */
public abstract class StateSupport<S extends State<S>> implements State<S> {
    static final Logger LOGGER = Logger.getLogger(StateSupport.class.getName());

    @Override
    public String toString() {
        final String simpleName = getClass().getSimpleName();
        return containingState() == null ? simpleName : containingState() + "." + simpleName;
    }

    @Override
    public void entry() {
        LOGGER.fine(() -> "entering " + this);
        initializeState();
    }

    @Override
    public void exit() {
        LOGGER.fine(() -> "exiting " + this);
    }
}
