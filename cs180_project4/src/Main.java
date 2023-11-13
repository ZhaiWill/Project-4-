import java.util.Scanner;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
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
        s.nextLine();
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

    public static User loginToAccount(Scanner s) { // to log into an existing account
        System.out.println("Please enter your username:");
        s.nextLine();
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
                            "4. Manage stores\n5. Block User\n6. Become Invisible\n7. Manage messages\n8. Exit");
        while (repeat) {
            int input = s.nextInt();
            switch (input) {
                // TODO: implement menus for initial menu 
                case 1 -> readMessages(s, user);
                case 2 -> sendMessage(s,user);
                case 3 -> System.out.println(input);
                case 4 -> System.out.println(input);
                case 5 -> block(s, user);
                case 6 -> invisible(s, user);
                case 7 -> System.out.println(input);
                case 8 -> repeat = false;
                default -> System.out.println("Error! Invalid input");
            }
        }
    }

    public static void initMenuBuyer(Scanner s, User user) {
        boolean repeat = true;
        System.out.println("1. Read all messages\n2. Send message\n3. Manage messages\n" +
                            "4. Browse stores\n5. Block User\n6. Become Invisible\n7. Manage account\n8. Exit");
        while (repeat) {
            int input = s.nextInt();
            switch (input) {
                // TODO: implement menus for initial menu 
                case 1 -> readMessages(s, user);
                case 2 -> sendMessage(s,user);
                case 3 -> System.out.println(input);
                case 4 -> System.out.println(input);
                case 5 -> block(s, user);
                case 6 -> invisible(s, user);
                case 7 -> System.out.println(input);
                case 8 -> repeat = false;
                default -> System.out.println("Error! Invalid input");
            }
        }
    }

    public static void sendMessage(Scanner s,User sender) {
        while (true) {
            System.out.println("Enter message recipient");
            String username = s.nextLine();
            User recepient = db.getUser(username);
            if (recepient.invisibleUsers.contains(sender)) {
                System.out.println("Error, no user with username " + username + " found. Please try again");
            } else {
                System.out.println("Enter message content");
                String content = s.nextLine();
                Message message = new Message(sender, recepient, content);
                if (message == null || recepient == null) {
                    System.out.println("Error, invalid message, please try again");
                } else {
                    if (sender.blockedUsers.contains(recepient)) {
                        System.out.println(sender.getUsername() + " is blocked by " + recepient.getUsername() + ". Message not sent.");
                        s.close();
                        break;
                    } else {
                        db.saveMessage(message);
                        System.out.println("Message sent successfully");
                        s.close();
                        break;
                    }

                }
            }
        } 
    }
    
    public static void readMessages(Scanner s, User receiver) {
        while (true) {
            ArrayList<User> correspondents = db.getAllCorrespondents(receiver);
            System.out.println("Which User's messages would you like to read?");
            for (User u : correspondents) {
                System.out.println(u.getUsername());
            }
            String input = s.nextLine();
            boolean found = false;
            for (User u : correspondents) {
                if (u.getUsername().equals(input)) {
                    found = true;
                    ArrayList<Message> messages = db.findAllMessages(receiver, u);
                    for (Message m : messages) {
                        System.out.println(m.toString()); //maybe make it more readable
                    }
                }
            }
            if (!found) {
                System.out.println("Sorry, you don't have correspondence with that User yet.");
            }
            while (true) {
                System.out.println("Continue reading messages? (Y or N)");
                input = s.nextLine();
                if (input.toLowerCase().equals("y")) {
                    continue;
                } else if (input.toLowerCase().equals("n")) {
                    break;
                } else {
                    System.out.println("type Y or N.");
                }
            }
        }
    }
    
    public static void block(Scanner s, User thisUser) {
        boolean repeat = true;
        System.out.print("What would you like to do?");
        System.out.println("1. Block a user\n2. Unblock a user\n3. Exit");
        while (repeat) {
            int input = s.nextInt();
            switch (input) {
                case 1 -> blockPrompt(s,thisUser);
                case 2 -> unblockPrompt(s, thisUser);
                case 3 -> repeat = false;
                default -> System.out.println("Error! Invalid input");
            }
        }
    }
    
    public static void invisible(Scanner s, User thisUser) {
        boolean repeat = true;
        System.out.print("What would you like to do?");
        System.out.println("1. Become invisible to a user\n2. Become uninvisible to a user\n3. Exit");
        while (repeat) {
            int input = s.nextInt();
            switch (input) {
                case 1 -> invisiblePrompt(s,thisUser);
                case 2 -> uninvisiblePrompt(s, thisUser);
                case 3 -> repeat = false;
                default -> System.out.println("Error! Invalid input");
            }
        }
    }
    public static void blockPrompt(Scanner s, User thisUser) {
        System.out.println("Enter the username of who you would like to block");
        String blockedUser = s.nextLine();
        User blockerUser1 = db.getUser(blockedUser);
        thisUser.blockUser(blockerUser1);
    }
    public static void unblockPrompt(Scanner s, User thisUser) {
        System.out.println("Enter the username of who you would like to unblock");
        String unblockedUser = s.nextLine();
        User unblockerUser1 = db.getUser(unblockedUser);
        thisUser.unblockUser(unblockerUser1);
    }
    public static void invisiblePrompt(Scanner s, User thisUser) {
        System.out.println("Enter the username of who you would like to become invisible to");
        String invisibleUser = s.nextLine();
        User invisibleUser1 = db.getUser(invisibleUser);
        thisUser.becomeInvisible(invisibleUser1);
    }
    public static void uninvisiblePrompt(Scanner s, User thisUser) {
        System.out.println("Enter the username of who you would like to become uninvisible to");
        String uninvisibleUser = s.nextLine();
        User uninvisibleUser1 = db.getUser(uninvisibleUser);
        thisUser.becomeUninvisible(uninvisibleUser1);
    }
    public static void menuSystem(Scanner s, User user){
        if (user.type.equals(userType.CUSTOMER)){
            initMenuSeller(s, user);
        } else {
            initMenuBuyer(s, user);
        }
    }
    public static void main(String[] args) {
        // ALL TESTS ARE NOW IN TEST.JAVA SO WE CAN START IMPLEMENTING MAIN METHOD
        Scanner s = new Scanner(System.in);
        int choice = openingPrompt(s);
        User thisUser;
        if (choice == 1) {
            thisUser = loginToAccount(s);
            if (thisUser.isType() == userType.SELLER) {
               initMenuSeller(s, thisUser);
            } else {
                initMenuBuyer(s, thisUser);
            }
        } else {
            thisUser = createAccount(s);
        }
        sendMessage(s,thisUser);  
    }
}
