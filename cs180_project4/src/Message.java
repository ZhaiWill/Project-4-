import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Message implements Serializable {
    User sender;
    User receiver;
    String contents;
    Date timestamp;
    UUID uuid;

    public Message(User sender, User receiver, String contents) {
        this.sender = sender;
        this.receiver = receiver;
        this.contents = contents;
        this.timestamp = new Date();
        this.uuid = UUID.nameUUIDFromBytes((sender.getUsername() + receiver.getUsername() + contents + timestamp.toString()).getBytes());
    }

    public Message(User sender, User receiver, String contents, Date timestamp, UUID uuid) {
        this.sender = sender;
        this.receiver = receiver;
        this.contents = contents;
        this.timestamp = timestamp;
        this.uuid = uuid;
    }

    public Message(User sender, User receiver, String contents, Date timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.contents = contents;
        this.timestamp = timestamp;
        this.uuid = UUID.nameUUIDFromBytes((sender.getUsername() + receiver.getUsername() + contents + timestamp.toString()).getBytes());
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }
    
    public void updateCurrentTimeStamp() {
        this.timestamp = new Date();
    }

    public String getContents() {
        return contents;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", message='" + contents + '\'' +
                ", timestamp=" + timestamp +
                ", uuid=" + uuid + '}';
    }

    public void setContent(String newContent) {
        this.contents = newContent;
    }

    public static boolean isValidMessage(Message message) {
        if (message.getReceiver().isType() == message.getSender().isType()) {
            return false;
        }
        return true;
    }
}
