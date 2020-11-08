/**
 * The module used to realize your own game.
 */
module pp.tanks {
    requires pp.common;
    requires java.xml;
    requires java.logging;
    requires java.prefs;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.fxml;
    opens pp.tanks.client to javafx.fxml, javafx.graphics;
}