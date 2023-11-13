
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class UI {

    Scanner scan;
    User loggedInUser;

    UI() {
        scan = new Scanner(System.in);
    }

    public void run() {
        this.loggedInUser = generateWelcomeScreen();
        while (true) {
            generateMainMenu();
        }
    }

    public void generateMainMenu() {
        switch (generateMenu(new String[]{"Send a Message", "View Conversations", "Block/Unblock Users"})) {
            case 0 -> sendMessageMenu();
            case 1 -> viewConversationsMenu();
            case 2 -> blockUsersMenu();
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
            if (this.loggedInUser.type == recipientUser.type) {
                System.out.println("Sorry, you can not send messages to other " + (this.loggedInUser.type == userType.CUSTOMER ? "customers" : "sellers"));
            }
            sendMessageFinalMenu(recipientUser);

        }
    }

    public void sendMessageFinalMenu(User recipientUser) {

        this.loggedInUser.sendmessage(recipientUser, getMessageContents());
    }

    public String getMessageContents() {
        return queryValue("What would you like the contents of the message to be?", true);
    }

    public String newMessageContentsFILE() {
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


    public void viewConversationsMenu() {
        List<String> userConvoList = loggedInUser.getAllAccessibleConversations();
        if (userConvoList.size() == 0) {
            System.out.println("You currently have no conversations");
            pressEnterToContinue();
            return;
        }
        System.out.println("What conversation would you like to see?");
        int userOption = generateMenu(userConvoList.toArray(new String[0]));
        viewConversationWith(userConvoList.get(userOption));
    }

    public void viewConversationWith(String username) {
        ArrayList<Message> messages = db.getConversation(this.loggedInUser, db.getUser(username));

        for (int i = 0; i < messages.size(); i++) {
            Message m = messages.get(i);
            String res = "";
            res += i + ": ";
            res += m.getTimestamp();
            res += ": ";
            res += m.sender.username;
            res += ": ";
            res += m.contents;
            System.out.println(res);
        }
        while (true) {
            pressEnterToContinue();
            switch (generateMenu(new String[]{"return", "edit message", "delete message", "reply to conversation"})) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    editMessageMenu(messages);
                }
            }

        }
    }

    private void editMessageMenu(ArrayList<Message> messages) {
        System.out.println("What message number would you like to edit?");
        int messageNumber = scan.nextInt();
        scan.nextLine();
        Message m = messages.get(messageNumber);
        if (!Objects.equals(m.sender.username, this.loggedInUser.username)) {
            System.out.println("You can not edit a message that you did not send");
            return;
        }
        db.editMessage(m, getMessageContents());
        System.out.println("Successfully updated contents");

    }


    public void blockUsersMenu() {
        while (true) {
            switch (generateMenu(new String[]{"Block a user", "Unblock a user", "See users you have blocked", "return"})) {
                case 0 -> {
                    System.out.println("Who would you like to block?");
                    String username = queryValue("username", false);
                    User user = db.getUser(username);
                    if (user == null) {
                        System.out.println("Sorry, a user with that username does not exist");
                        continue;
                    }
                    if (Objects.equals(user.username, this.loggedInUser.username)) {
                        System.out.println("You can not block yourself");
                        continue;
                    }
                    boolean invisibleModeBlock = queryYesNo(("Would you like to block this user in invisible mode?"));
                    this.loggedInUser.setUserBlockStatus(user.username, invisibleModeBlock ? userBlockStatus.INVISIBLE : userBlockStatus.BLOCKED);
                }
                case 1 -> {
                    System.out.println("Who would you like to unblock?");
                    String username = queryValue("username", false);
                    User user = db.getUser(username);
                    if (user == null) {
                        System.out.println("Sorry, a user with that username does not exist");
                        continue;
                    }
                    if (Objects.equals(user.username, this.loggedInUser.username)) {
                        System.out.println("You can not unblock yourself");
                        continue;
                    }
                    if (this.loggedInUser.getUserBlockedStatus(user) == userBlockStatus.ALLOWED) {
                        System.out.println("That user is not blocked");
                        continue;
                    }
                    this.loggedInUser.setUserBlockStatus(user.username, userBlockStatus.ALLOWED);
                }
                case 2 -> {
                    Map<String, userBlockStatus> stringuserBlockStatusMap = this.loggedInUser.userBlockStatusMap;
                    System.out.println("You have blocked the following users:");
                    for (Map.Entry<String, userBlockStatus> entry : stringuserBlockStatusMap.entrySet()) {
                        if (entry.getValue() == userBlockStatus.BLOCKED || entry.getValue() == userBlockStatus.INVISIBLE) {
                            System.out.println(entry.getKey() + " is blocked" + (entry.getValue() == userBlockStatus.INVISIBLE ? " (invisible)" : ""));
                        }
                    }
                }
                case 3 -> {
                    return;
                }
            }
            pressEnterToContinue();
        }
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

    public void pressEnterToContinue() {
        System.out.println("Press Enter to continue");
        scan.nextLine();
    }
}
