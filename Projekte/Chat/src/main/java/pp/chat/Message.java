package pp.chat;

import java.io.Serializable;

/**
 * Message container for network transfer
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String from;
    private final String body;
    private final MessageType type;

    /**
     * Creates a new message
     *
     * @param from user name of the message sender
     * @param body message text
     * @param type message type
     */
    public Message(String from, String body, MessageType type) {
        this.from = from;
        this.body = body;
        this.type = type;
    }

    /**
     * Returns a string representation of this message
     */
    @Override
    public String toString() {
        return String.format("[%s] %s [%s]", from, body, type);
    }

    /**
     * Returns the user name of the message sender
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the message text
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns the message type
     */
    public MessageType getType() {
        return type;
    }
}
