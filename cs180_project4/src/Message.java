import java.util.Date;
import java.util.UUID;

public class Message {
    User sender;
    User receiver;
    String message;
    Date timestamp;
    UUID uuid;

    public Message(User sender, User receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = new Date();
        this.uuid = UUID.nameUUIDFromBytes((sender.getUsername() + receiver.getUsername() + message + timestamp.toString()).getBytes());
    }

    public Message(User sender, User receiver, String message, Date timestamp, UUID uuid) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
        this.uuid = uuid;
    }
    public Message(User sender, User receiver, String message, Date timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
        this.uuid = UUID.nameUUIDFromBytes((sender.getUsername() + receiver.getUsername() + message + timestamp.toString()).getBytes());
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", uuid=" + uuid +
                '}';
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public UUID getUuid() {
        return uuid;
    }
}