import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        db.initializeDatabase();

        User user1 = User.createUser(userType.CUSTOMER, "john123", "abc123!!");
        User user2 = User.createUser(userType.SELLER, "ANDY", "51242");
        System.out.println(user1);
        System.out.println(db.getUser("john123"));

        System.out.println(user2);
        System.out.println(db.getUser("ANDY"));

        assert user1 != null;
        assert user2 != null;

        Message preSaveMessage = user1.sendmessage(user2, "Hello, how are you?");
        System.out.println(preSaveMessage);
        System.out.println(db.getMessage(String.valueOf(preSaveMessage.getUuid())));
    }
}
