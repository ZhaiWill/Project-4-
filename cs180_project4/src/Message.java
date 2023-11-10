import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Message implements Serializable {
    User sender;
    User receiver;
    String message;
    Date timestamp;
    UUID uuid;
    boolean senderReadable;
    boolean receiverReadable;

    public Message(User sender, User receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderReadable = true;
        this.receiverReadable = true;
        this.message = message;
        this.timestamp = new Date();
        this.uuid = UUID.nameUUIDFromBytes((sender.getUsername() + receiver.getUsername() + message + timestamp.toString()).getBytes());
    }

    public Message(User sender, User receiver, String message, Date timestamp, UUID uuid) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderReadable = true;
        this.receiverReadable = true;
        this.message = message;
        this.timestamp = timestamp;
        this.uuid = uuid;
    }

    public Message(User sender, User receiver, String message, Date timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderReadable = true;
        this.receiverReadable = true;
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
    
    public void updateCurrentTimeStamp() {
        this.timestamp = new Date();
    }

    public String getMessage() {
        return message;
    }

    public boolean isSenderReadable() {
        return this.senderReadable;
    }
   
    public boolean isReceiverReadable() {
        return this.receiverReadable;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiverReadable(boolean receiverReadable) {
        this.receiverReadable = receiverReadable;
    }

    public void setSenderReadable(boolean senderReadable) {
        this.senderReadable = senderReadable;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender=" + sender +
                ", receiver=" + receiver +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", uuid=" + uuid +
                ", receiverReadable=" + 
                receiverReadable + 
                ", senderReadable=" + 
                senderReadable + '}';
    }
}
