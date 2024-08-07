package queue;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 2154779845414708399L;

    private String messages;
    private String senderUsername;
    private long timestamp;

    public Message(String username, String messages) {
        this.messages = messages;
        this.senderUsername = username;
        this.timestamp = System.currentTimeMillis(); // store the timestamp when the message was sent
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    @Override
    public String toString() {
        return "Message{" + "messages=" + messages + ", senderUsername=" + senderUsername + '}';
    }
}