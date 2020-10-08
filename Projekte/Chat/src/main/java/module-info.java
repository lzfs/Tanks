/**
 * The module used to realize a multi-user chat.
 */
module pp.chat {
    requires pp.common;
    requires java.logging;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    opens pp.chat to javafx.fxml, javafx.graphics;
}