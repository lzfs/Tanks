/**
 * The module used to realize the PegSolitaire game.
 */
module pp.pegsolitaire {
    requires java.logging;
    requires javafx.graphics;
    requires javafx.base;
    opens pp.pegsolitaire to javafx.graphics;
}