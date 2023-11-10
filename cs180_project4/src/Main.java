import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static int openingPrompt() {
        Scanner s = new Scanner(System.in);
        System.out.println("Welcome!\n 1. Sign in to account\n 2. Create new Account");
        while (true) {
            int choice = s.nextInt();
            if (choice < 1 || choice > 2) {
                System.out.println("Please select either 1 or 2");
            } else{
                return choice;
            }
        }
    }
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
        //ALL TESTS ARE NOW IN TEST.JAVA SO WE CAN START IMPLEMENTING MAIN METHOD

        int choice = openingPrompt();
        User thisUser;
        if (choice == 1) {
            thisUser = loginToAccount();
        } else {
            thisUser = createAccount();
        }
    }
}
