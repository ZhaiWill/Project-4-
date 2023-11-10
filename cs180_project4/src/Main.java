import java.util.Scanner;
import java.util.UUID;

public class Main {
    public void createAccount() {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter your new username: ");
        String newUsername = s.nextLine();
        System.out.println( "Enter your password:");
        String newPassword = s.nextLine();
        System.out.println("Enter your email:");
        String email = s.nextLine();
        while (true) {
            System.out.println("Are you a seller or a buyer?");
            String sellOrBuy = s.nextLine();
            if (sellOrBuy.toLowerCase().equals("buyer")) {
                User newUser = User.createUser(userType.CUSTOMER, newUsername, newPassword, email);
                break;
            } else if (sellOrBuy.toLowerCase().equals("seller")) {
                User newUser = User.createUser(userType.SELLER, newUsername, newPassword, email);
                break;
            } else {
                System.out.println("Sorry, not a valid option!");
            }
        }
        s.close();
    }
    public static void main(String[] args) {
        db.initializeDatabase();

        User user1 = User.createUser(userType.CUSTOMER, "john123", "abc123!!", "random@gmail.com");
        User user2 = User.createUser(userType.SELLER, "ANDY", "51242", "random2@gmail.com");
        System.out.println(user1);
        System.out.println(db.getUser("john123"));

        System.out.println(user2);
        System.out.println(db.getUser("ANDY"));

        assert user1 != null;
        assert user2 != null;

        Message preSaveMessage = user1.sendmessage(user2, "Hello, how are you?");
        System.out.println(preSaveMessage);
        System.out.println(db.getMessage(String.valueOf(preSaveMessage.getUuid())));
        db.editMessage(preSaveMessage, "new message");
        System.out.println(db.getMessage(String.valueOf(preSaveMessage.getUuid())));

       db.editMessage(preSaveMessage, "hello i edited you");
        System.out.println(db.getMessage(String.valueOf(preSaveMessage.getUuid())));
    
        db.removeMessage(user2,preSaveMessage);

        db.editMessage(preSaveMessage, "hello i TOO edited you");
        System.out.println(db.getMessage(String.valueOf(preSaveMessage.getUuid())));

    }
}
