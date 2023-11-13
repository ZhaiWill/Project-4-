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

        if (this.loggedInUser.type == userType.CUSTOMER) {
            //CUSTOMER MENU
            switch (generateMenu(new String[]{"Send a Message", "View Conversations", "Block/Unblock Users", "Find Sellers", "Find Stores",})) {
                case 0 -> sendMessageMenu();
                case 1 -> viewConversationsMenu();
                case 2 -> blockUsersMenu();
                case 3 -> findAlternateUsersMenu();
                case 4 -> findStoresMenu();
            }
        } else {
            //SELLER MENU
            switch (generateMenu(new String[]{"Send a Message", "View Conversations", "Block/Unblock Users", "Find Customers", "Manage Store(s)",})) {
                case 0 -> sendMessageMenu();
                case 1 -> viewConversationsMenu();
                case 2 -> blockUsersMenu();
                case 3 -> findAlternateUsersMenu();
                case 4 -> manageStoresMenu();
            }
        }
    }

    private void manageStoresMenu() {
        //print menu asking if you want to view stored you own or make a new store
        switch (generateMenu(new String[]{"View Stores", "Create a new Store"})) {
            case 0 -> viewStoresMenu();
            case 1 -> createNewStoreMenu();
        }

    }

    private void createNewStoreMenu() {
        String storeName = queryValue("What would you like to name your store?", true);
        Store newStore = new Store(storeName, this.loggedInUser.username);
        if (db.readStoreFromFile(storeName) != null) {
            System.out.println("Sorry, a store with that name already exists");
            pressEnterToContinue();
            return;
        }
        db.saveStore(newStore);
        System.out.println("Successfully created store " + newStore.storeName);
        pressEnterToContinue();
    }

    private void viewStoresMenu() {
        ArrayList<Store> stores = db.getAllStores();
        ArrayList<Store> storesOwnedByUser = new ArrayList<>();
        for (Store store : stores) {
            if (Objects.equals(store.OwnerUserName, this.loggedInUser.username)) {
                storesOwnedByUser.add(store);
            }
        }
        if (storesOwnedByUser.size() == 0) {
            System.out.println("You currently have no stores");
            pressEnterToContinue();
            return;
        }
        for (Store store : storesOwnedByUser) {
            System.out.println(store.storeName);
        }
        pressEnterToContinue();
    }

    private void findStoresMenu() {
        System.out.println("Here is a list of stores you can message:");
        ArrayList<Store> stores = this.loggedInUser.getAllAccessibleStores();
        for (int i = 0; i < stores.size(); i++) {
            Store store = stores.get(i);
            System.out.println(i + ": " + store.storeName + " owned by " + store.OwnerUserName);
        }

        if (queryYesNo("Would you like to message one?")) {
            System.out.println("What store # would you like to message?");
            int storeNumber = getIntegerValue(0, stores.size() - 1);
            sendMessageFinalMenu(db.getUser(stores.get(storeNumber).OwnerUserName));
        }
    }

    private void findAlternateUsersMenu() {
        System.out.println("Here is a list of " + (this.loggedInUser.type == userType.CUSTOMER ? "sellers" : "customers") + " you can message:");

        for (User user : this.loggedInUser.getAllAccessibleUsers()) {
            System.out.println(user.username);
        }

        if (queryYesNo("Would you like to message one?")) {
            sendMessageMenu();
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
                continue;
            }
            if (recipientUser.getUserBlockedStatus(this.loggedInUser) == userBlockStatus.INVISIBLE) {
                System.out.println("Sorry, a user with that username does not exist");
                continue;
            }
            if (this.loggedInUser.type == recipientUser.type) {
                System.out.println("Sorry, you can not send messages to other " + (this.loggedInUser.type == userType.CUSTOMER ? "customers" : "sellers"));
                continue;
            }
            sendMessageFinalMenu(recipientUser);

        }
    }

    public void sendMessageFinalMenu(User recipientUser) {

        this.loggedInUser.sendmessage(recipientUser, getMessageContents());
    }

    public String getMessageContents() {
        if (queryYesNo("Would you like to send the message from a file?")) {
            String res = newMessageContentsFILE();
            return res;
        }
        return queryValue("What would you like the contents of the message to be?", true);
    }

    public String newMessageContentsFILE() {
        String res = null;
        do {
            try {
                File f = new File(queryValue("path to file", false));
                String fileContents = new String((Files.readAllBytes(f.toPath())));
                res = fileContents;
            } catch (IOException ignored) {
                System.out.println("Sorry, we can not ready any file with that path");
                continue;
            }

        } while (res == null);
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
            switch (generateMenu(new String[]{"return", "edit message", "delete message"})) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    editMessageMenu(messages);
                }
                case 2 -> {
                    deleteMessageMenu(messages);
                }
            }

        }
    }

    private void deleteMessageMenu(ArrayList<Message> messages) {
        System.out.println("What message number would you like to delete?");
        int messageNumber = getIntegerValue(0, messages.size() - 1);
        Message m = messages.get(messageNumber);

        db.deleteMessage(this.loggedInUser, m);
        System.out.println("Successfully updated contents");

    }

    private void editMessageMenu(ArrayList<Message> messages) {
        System.out.println("What message number would you like to edit?");
        int messageNumber = getIntegerValue(0, messages.size() - 1);
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
                    db.saveUser(this.loggedInUser);
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

        return getIntegerValue(0, choices.length - 1);
    }


    public String queryValue(String query, boolean customPhrase) {
        if (!customPhrase) System.out.println("Please enter the following: (" + query + ")");
        else System.out.println(query);
        //ensure that input is alphanumeric

        return getStringValue();
    }

    public boolean queryYesNo(String query) {
        System.out.println(query + " (Y/N):");
        return getStringValue().toLowerCase().startsWith("y");
    }

    public void pressEnterToContinue() {
        System.out.println("Press Enter to continue");
        scan.nextLine();
    }

    public int getIntegerValue(int min, int max) {
        Integer res = null;
        while (res == null) {
            try {
                res = Integer.valueOf(scan.nextLine());
            } catch (Exception ignored) {
                System.out.println("Please enter a valid integer");
                continue;
            }
            if (res < min || res > max) {
                System.out.println("Please enter an integer between " + min + " and " + max + " inclusive");
                res = null;
                continue;
            }
        }
        return res;
    }

    public String getStringValue() {
        String res = null;
        while (res == null) {
            res = scan.nextLine();
            if (!isValidString(res)) {
                System.out.println("Please enter a valid string with only letters, periods, /, and @");
                res = null;
            }
        }
        return res;
    }

    private boolean isValidString(String input) {
        // Check if the input contains only letters, periods, and /
        for (char c : input.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isDigit(c) && c != '.' && c != '/' && c != '@') {
                return false;
            }
        }
        return true;
    }
}
