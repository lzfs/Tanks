package pp.chat;

/**
 * An exception class used for signalling any chat specific exception happened in the chat application.
 */
class ChatException extends Exception {
    /**
     * Creates a new exception with the specified message.
     */
    public ChatException(String message) {
        super(message);
    }
}
