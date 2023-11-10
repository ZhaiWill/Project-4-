import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static User createAccount() {
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
            if (sellOrBuy.toLowerCase().equalsIgnoreCase("buyer")) {
                User newUser = User.createUser(userType.CUSTOMER, newUsername, newPassword, email);
                 s.close();
                return newUser;
            } else if (sellOrBuy.toLowerCase().equalsIgnoreCase("seller")) {
                User newUser = User.createUser(userType.SELLER, newUsername, newPassword, email);
                s.close();
                return newUser;
            } else {
                System.out.println("Sorry, not a valid option!");
            }
        }
    }

    public static User loginToAccount() { // to log into an existing account
        Scanner s = new Scanner(System.in);
        System.out.println("Please enter your username:");
            while (true) {
            String username = s.nextLine();
            User user = db.getUser(username);
            if (user != null) {
                System.out.println("Enter your password");
                while (true) {
                    String password = s.nextLine();
                    if (user.getPassword().equals(password)) {
                        System.out.println("Successfully signed in!");
                        s.close();
                        return user;
                    } else {
                        System.out.println("Error! Incorrect password. Please try again");
                    }
                }
            } else {
                System.out.println("Error, no user with username " + username + " found. Please try again");
            }
        }
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
        db.editUsername(user2, "john123");
        System.out.println(user2);
    }
}
