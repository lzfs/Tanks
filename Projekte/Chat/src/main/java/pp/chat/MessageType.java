package pp.chat;

/**
 * Message types
 */
public enum MessageType {
    /**
     * Text message for normal chat activity
     */
    TEXT,
    /**
     * Signals that user FROM has connected to the chat - serves as user propagation, too
     */
    CONNECTED,
    /**
     * Signals to the users, that user FROM has been disconnected - serves as user propagation, too
     */
    DISCONNECTED
}
