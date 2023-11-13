
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class UI {

    Scanner scan;
    User loggedInUser;

    UI() {
        scan = new Scanner(System.in);
    }

    public void run() {
        this.loggedInUser = generateWelcomeScreen();
        generateMainMenu();
    }

    public void generateMainMenu() {
        switch (generateMenu(new String[]{"Send a Message", ""})) {
            case 0:
                sendMessageMenu();
                break;
            case 1:
            case 2:
        }
    }

    public void sendMessageMenu() {
        User recipientUser = null;
        while (recipientUser == null) {
            recipientUser = db.getUser(queryValue("what user would you like to send a message to?", true));

            if (recipientUser == null) {
                System.out.println("Sorry, a user with that username does not exist");
                continue;
            }
            if (recipientUser == this.loggedInUser) {
                System.out.println("You can not send a message to yourself");
                continue;
            }

            if (recipientUser.getUserBlockedStatus(this.loggedInUser) == userBlockStatus.BLOCKED) {
                System.out.println("Sorry, you can not send messages to that user");
            }
            if (recipientUser.getUserBlockedStatus(this.loggedInUser) == userBlockStatus.INVISIBLE) {
                System.out.println("Sorry, a user with that username does not exist");
            }

            String message = queryValue("What would you like to send?", true);
            this.loggedInUser.sendmessage(recipientUser, message);

        }
    }

    public String newMessageContents() {
        String res = null;
        do {
            if (queryYesNo("Would you like to import a file as your message?")) {
                try {
                    File f = new File(queryValue("path to file", false));
                    String fileContents = new String((Files.readAllBytes(f.toPath())));
                    res = fileContents;
                } catch (IOException ignored) {
                    System.out.println("Sorry, we can not ready any file with that path");
                    continue;
                }
            } else {
                res = queryValue("What is the message you would like to send?", true);
            }
        }
        while (res == null);
        return res;
    }


    //WELCOME SCREEN METHODS
    public User generateWelcomeScreen() {
        if (generateMenu(new String[]{"sign up", "log in"}) == 0) {
            return newUserMenu();
        }
        return loginMenu();
    }

    public User newUserMenu() {
        boolean userCreated = false;
        do {
            String username = queryValue("username", false);
            if (db.getUser(username) != null) {
                System.out.println("Sorry, that username is already taken");
                continue;
            }
            String password = queryValue("password", false);
            String email = queryValue("email", false);
            userType usertype = queryYesNo("Are you selling a product?") ? userType.SELLER : userType.CUSTOMER;
            User newUser = User.createUser(usertype, username, password, email);
            return newUser;
        } while (!userCreated);
        return null; // in case we want to add an exit case during the user creation process
    }

    public User loginMenu() {
        do {
            String username = queryValue("username", false);
            String password = queryValue("password", false);
            User user = db.getUser(username);
            if (user == null || !password.equals(user.password)) {
                System.out.println("Sorry, your username or password is incorrect.");
                continue;
            }
            return user;

        } while (true);
    }


    //HELPER METHODS
    public int generateMenu(String[] choices) {
        System.out.println("CHOOSE AN OPTION:");
        for (int i = 0; i < choices.length; i++) {
            System.out.println(i + ": " + choices[i]);
        }
        int res = scan.nextInt();
        scan.nextLine();
        return res;
    }


    public String queryValue(String query, boolean customPhrase) {
        if (!customPhrase) System.out.println("Please enter the following: (" + query + ")");
        else System.out.println(query);
        return scan.nextLine();
    }

    public boolean queryYesNo(String query) {
        System.out.println(query + " (Y/N):");
        return scan.nextLine().toLowerCase().startsWith("y");
    }

}
