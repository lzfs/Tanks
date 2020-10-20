/**
 * The module used to realize the Droids game.
 */
module pp.droids {
    requires pp.media;
    requires java.xml;
    requires java.prefs;
    requires javafx.controls;
    opens pp.droids to javafx.graphics;
}