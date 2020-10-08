/**
 * The module used to realize the Battleship game.
 */
module pp.battleship {
    requires pp.common;
    requires java.logging;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    opens pp.battleship.client to javafx.fxml, javafx.graphics;
}