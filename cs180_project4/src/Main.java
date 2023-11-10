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

    public boolean loginToAccount() { // to log into an existing account

        Scanner s = new Scanner(System.in);
        System.out.println("What's your Username?");
        String username = s.nextLine();

        if (db.checkUsername(username)) {
            User u = db.getUser(username);
            System.out.println("What's your password?");
            String password = s.nextLine();
            if (password.equals(u.getPassword())) {
                System.out.println("Password is correct!");
                return true; // returns true only if login is successful
            } else {
                System.out.println("Password is incorrect.");
                return false;
            }
        } else {
            System.out.println("User with that username does not exist.");
            return false;
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

    
        db.removeMessage(user2,preSaveMessage);
    }
}
