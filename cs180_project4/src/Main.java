import java.util.Scanner;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        System.out.println("Enter your password:");
        String newPassword = s.nextLine();
        System.out.println("Enter your email:");
        String email = s.nextLine();
        while (true) {
            System.out.println("Are you a seller or a buyer?");
            String sellOrBuy = s.nextLine();
            if (sellOrBuy.toLowerCase().equalsIgnoreCase("buyer")) {
                User newUser = User.createUser(userType.CUSTOMER, newUsername, newPassword, email);
                return newUser;
            } else if (sellOrBuy.toLowerCase().equalsIgnoreCase("seller")) {
                User newUser = User.createUser(userType.SELLER, newUsername, newPassword, email);
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
                case 2 -> System.out.println(input);
                case 3 -> manageAccount(s, user);
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
        while (repeat == true) {
            int input = s.nextInt();
            switch (input) {
                // TODO: implement menus for initial menu
                case 1 -> readMessages(s, user);
                case 2 -> System.out.println(input);
                case 3 -> System.out.println(input);
                case 4 -> System.out.println(input);
                case 5 -> block(s, user);
                case 6 -> invisible(s, user);
                case 7 -> manageAccount(s, user);
                case 8 -> repeat = false;
                default -> System.out.println("Error! Invalid input");
            }
        }
        return;
    }

    public static void sendMessage(String username, User sender, String content) {
        while (true) {
            User recepient = db.getUser(username);
            List<User> invisibleUsers1 = recepient.getInvisibleUsers();
            List<User> blockedUsers1 = sender.getBlockedUsers();
            if (invisibleUsers1.contains(sender)) {
                JOptionPane.showMessageDialog(null, "Error no username called " + sender.getUsername() + " exists. Try Again", null, JOptionPane.INFORMATION_MESSAGE);
            }
                Message message = new Message(sender, recepient, content);
                if (message == null || recepient == null) {
                    JOptionPane.showMessageDialog(null, "Error, invalid message, please try again", null, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    if (!blockedUsers1.contains(recepient)) {
                        db.saveMessage(message);
                        System.out.println("Message sent successfully");
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, sender.getUsername() + " is blocked by " + recepient.getUsername() + ". Message not sent.", null, JOptionPane.INFORMATION_MESSAGE);
                        break;
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
    public static void manageAccount(Scanner s, User user) {
        boolean repeat = true;
        System.out.print("What would you like to do?\n");
        System.out.println("1. Edit Username\n2. Edit Password\n3. Delete Account\n4. Exit");
        while (repeat) {
            int input = s.nextInt();
            switch (input) {
                case 1 -> {
                    System.out.println("Enter new username");
                    String username = s.nextLine();
                    if(db.editUsername(user, username) == true) {
                        System.out.print("Successfully changed username");
                    } else {
                        System.out.print("Username not available. Try again.");
                    }
                }
                case 2 -> {
                    System.out.println("Enter new password");
                    String password = s.nextLine();
                    db.editPassword(user, password);
                }
                case 3 -> {
                    db.deleteUser(user);
                    System.out.print("User deleted successfully");
                    return;
                }
                default -> System.out.println("Error! Invalid input");
            }
        }
        return;
    }
    public static void block(Scanner s, User thisUser) {
        boolean repeat = true;
        System.out.print("What would you like to do?\n");
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
        return;
    }

    public static void invisible(Scanner s, User thisUser) {
        boolean repeat = true;
        System.out.print("What would you like to do?\n");
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
        return;
    }
    public static void blockPrompt(Scanner s, User thisUser) {
        System.out.println("Enter the username of who you would like to block");
        s.nextLine();
        String blockedUser = s.nextLine();
        User blockerUser1 = db.getUser(blockedUser);
        thisUser.blockUser(blockerUser1);
        menuSystem(s, thisUser);
    }
    public static void unblockPrompt(Scanner s, User thisUser) {
        System.out.println("Enter the username of who you would like to unblock");
        s.nextLine();
        String unblockedUser = s.nextLine();
        User unblockerUser1 = db.getUser(unblockedUser);
        thisUser.unblockUser(unblockerUser1);
        menuSystem(s, thisUser);
    }
    public static void invisiblePrompt(Scanner s, User thisUser) {
        System.out.println("Enter the username of who you would like to become invisible to");
        s.nextLine();
        String invisibleUser = s.nextLine();
        User invisibleUser1 = db.getUser(invisibleUser);
        thisUser.becomeInvisible(invisibleUser1);
        menuSystem(s, thisUser);
    }
    public static void uninvisiblePrompt(Scanner s, User thisUser) {
        System.out.println("Enter the username of who you would like to become uninvisible to");
        s.nextLine();
        String uninvisibleUser = s.nextLine();
        User uninvisibleUser1 = db.getUser(uninvisibleUser);
        thisUser.becomeUninvisible(uninvisibleUser1);
        menuSystem(s, thisUser);
    }
    public static void menuSystem(Scanner s, User user){
        if (user.isType() == userType.CUSTOMER){
            initMenuBuyer(s, user);
        } else {
            initMenuSeller(s, user);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }
}
