/**
 * Contains common and often shared functionality in a general and basic implementation.
 */
module pp.common {
    requires transitive java.logging;
    exports pp.automaton;
    exports pp.logging;
    exports pp.navigation;
    exports pp.network;
    exports pp.util;
}