import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        db.initializeDatabase();

        User user1 = User.createUser(userType.CUSTOMER, "john123", "abc123!!", "random@gmail.com");
        User user2 = User.createUser(userType.SELLER, "ANDY", "51242", "random2@gmail.com");
        User user3 = User.createUser(userType.CUSTOMER, "test", "test", "test@gmail.com");

        assert user1 != null;
        assert user2 != null;
    
        user1.sendmessage(user2, "Hello, how are you?");
        user1.sendmessage(user2, "I am doing well");
        user2.sendmessage(user1, "That's nice to hear");
        user2.sendmessage(user1, "How are you doing?");
    }
}
