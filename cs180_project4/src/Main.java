import java.util.Scanner;

public class Main {
    public static int openingPrompt(Scanner s) {
        System.out.println("Welcome!\n 1. Sign in to account\n 2. Create new Account");
        while (true) {
            int choice = s.nextInt();
            if (choice < 1 || choice > 2) {
                System.out.println("Please select either 1 or 2");
            } else {
                return choice;
            }
        }
    }

    public static User createAccount(Scanner s) {
        System.out.println("Enter your new username: ");
        String newUsername = s.nextLine();
        System.out.println("Enter your password:");
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

    public static User loginToAccount(Scanner s) { // to log into an existing account
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

    public static void initMenuSeller(Scanner s, User user) {
        boolean repeat = true;
        System.out.println("1. Read all messages\n2. Send message\n3. Manage account\n" +
                "4. Manage stores\n5. View statistics\n6. Manage messages\n7. Exit");
        while (repeat) {
            int input = s.nextInt();
            switch (input) {
                // TODO: implement menus for initial menu 
                case 1 -> System.out.println(input);
                case 2 -> sendMessage(s, user);
                case 3 -> System.out.println(input);
                case 4 -> System.out.println(input);
                case 5 -> System.out.println(input);
                case 6 -> System.out.println(input);
                case 7 -> repeat = false;
                default -> System.out.println("Error! Invalid input");
            }
        }
    }

    public static void initMenuBuyer(Scanner s, User user) {
        boolean repeat = true;
        System.out.println("1. Read all messages\n2. Send message\n3. Manage messages\n" +
                "4. Browse stores\n5. View statistics\n6. Manage account\n7. Exit");
        while (repeat) {
            int input = s.nextInt();
            switch (input) {
                // TODO: implement menus for initial menu 
                case 1 -> System.out.println(input);
                case 2 -> sendMessage(s, user);
                case 3 -> System.out.println(input);
                case 4 -> System.out.println(input);
                case 5 -> System.out.println(input);
                case 6 -> System.out.println(input);
                case 7 -> repeat = false;
                default -> System.out.println("Error! Invalid input");
            }
        }
    }

    public static void sendMessage(Scanner s, User sender) {
        while (true) {
            System.out.println("Enter message recipient");
            String username = s.nextLine();
            User recepient = db.getUser(username);
            System.out.println("Enter message content");
            String content = s.nextLine();
            Message message = new Message(sender, recepient, content);
            if (message == null || recepient == null) {
                System.out.println("Error, invalid message, please try again");
            } else {
                db.createMessage(message);
                System.out.println("Message sent successfully");
                s.close();
                break;
            }
        }
    }

    public static void main(String[] args) {
        db.initializeDatabase();
        User bob = User.createUser(userType.CUSTOMER, "bob", "123", "1@gmai.com");
        User joe = User.createUser(userType.SELLER, "joe", "123", "2@gmai.com");
        User andy = User.createUser(userType.SELLER, "andy", "123", "2@gmai.com");
        assert bob != null;
        assert andy != null;
        assert joe != null;
        bob.sendmessage(joe, "hi");
        joe.sendmessage(bob, "hello");
        bob.sendmessage(joe, "how are you");

        bob.setUserBlockStatus("andy", userBlockStatus.BLOCKED);
        bob.setUserBlockStatus("joe", userBlockStatus.INVISIBLE);
        db.saveUser(bob);
//        System.out.println(bob.userBlockStatusMap);
//        System.out.println(db.getUser("bob").userBlockStatusMap);
        new UI().run();
    }
}
