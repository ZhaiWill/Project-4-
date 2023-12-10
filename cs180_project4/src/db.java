import java.io.*;
import java.util.*;

public class db {
    private static final Object lock = new Object();

    public db() {

    }

    private static String root = "storage";

    // Clear the entire database (CAUTION: WILL REMOVE ALL DATA)
    public static boolean clearDatabase() {
        synchronized (lock) {
            File rootDirectory = new File(root);
            if (rootDirectory.exists()) {
                deleteDirectory(rootDirectory);
                output.debugPrint("Database cleared.");
            } else {
                output.debugPrint("Database does not exist, nothing to clear.");
            }
            return true;
        }
    }

    private static void deleteDirectory(File directory) {
        synchronized (lock) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }

    // Clear the entire database (CAUTION: WILL REMOVE ALL DATA)
    public static boolean initializeDatabase() {
        synchronized (lock) {

            clearDatabase();
            // Create the root directory if it doesn't exist
            File rootDirectory = new File(root);
            if (!rootDirectory.exists()) {
                if (rootDirectory.mkdirs()) {
                    output.debugPrint("Database root directory created: " + root);
                } else {
                    output.debugPrint("Failed to create the root directory.");
                    return false;
                }
            }

            // Create directories for users and messages
            String usersDirectoryPath = root + "/users";
            String messagesDirectoryPath = root + "/messages";
            String storesDirectoryPath = root + "/stores";

            if (createDirectory(usersDirectoryPath) && createDirectory(messagesDirectoryPath) && createDirectory(storesDirectoryPath)) {
                output.debugPrint("User and messages directories created.");
                return true;
            } else {
                output.debugPrint("Failed to create user or messages directory.");
                return false;
            }
        }
    }

    private static boolean createDirectory(String path) {
        synchronized (lock) {
            File directory = new File(path);
            if (!directory.exists()) {
                return directory.mkdirs();
            }
            return true;
        }
    }

    //CAUTION. WILL OVERWRITE
    public static User saveUser(User user) {
        synchronized (lock) {

            //if (user == null) return null;
            String filePath = "storage/users/" + user.getUsername() + ".user";

            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

                objectOutputStream.writeObject(user);
                output.debugPrint("User serialized and saved to " + filePath);
            } catch (IOException e) {
                output.debugPrint("Failed to save user to " + filePath);
                output.debugPrint(Arrays.toString(e.getStackTrace()));
                return null;
            }
            return user;
        }
    }

    public static User getUser(String username) {
        synchronized (lock) {
            String filePath = "storage/users/" + username + ".user";

            try (FileInputStream fileInputStream = new FileInputStream(filePath); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

                User user = (User) objectInputStream.readObject();
                output.debugPrint("User deserialized from " + filePath);
                return user;
            } catch (IOException | ClassNotFoundException e) {
                output.debugPrint("Failed to get user from " + filePath);
                output.debugPrint(Arrays.toString(e.getStackTrace()));
                return null;
            }
        }
    }

    public static boolean deleteUser(User user) {
        synchronized (lock) {
            String username = user.getUsername();
            String filePath = "storage/users/" + username + ".user";

            File file = new File(filePath);
            file.delete();
            output.debugPrint("User deleted");
            return true;
        }
    }

    public static ArrayList<User> getAllUsers() {
        synchronized (lock) {
            ArrayList<User> users = new ArrayList<>();
            File usersDirectory = new File(root + "/users");

            if (usersDirectory.exists() && usersDirectory.isDirectory()) {
                File[] userFiles = usersDirectory.listFiles();

                if (userFiles != null) {
                    for (File userFile : userFiles) {
                        try (FileInputStream fileInputStream = new FileInputStream(userFile);
                             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

                            User user = (User) objectInputStream.readObject();
                            users.add(user);
                        } catch (IOException | ClassNotFoundException e) {
                            output.debugPrint("Failed to read user from " + userFile.getPath());
                            output.debugPrint(Arrays.toString(e.getStackTrace()));
                        }
                    }
                }
            }

            return users;
        }
    }

    public static boolean editUsername(User user, String newUsername) {
        synchronized (lock) {
            if (db.getUser(user.getUsername()) != null) {
                output.debugPrint("User with username {" + user.getUsername() + "} already exists.");
                return false;
            }
            user.setUsername(newUsername);
            saveUser(user);
            return true;
        }
    }

    public static boolean editPassword(User user, String newUsername) {
        synchronized (lock) {
            user.setPassword(newUsername);
            saveUser(user);
            return true;
        }
    }

    public static Message createMessage(Message message) {
        synchronized (lock) {
            String senderUsername = message.getSender().getUsername();
            String receiverUsername = message.getReceiver().getUsername();
            String uuid = message.getUuid().toString();

            // Create directories for sender and receiver if they don't exist
            String senderDirPath = root + "/messages/" + senderUsername + "/sent/" + receiverUsername;
            String receiverDirPath = root + "/messages/" + receiverUsername + "/received/" + senderUsername;

            createDirectory(senderDirPath);
            createDirectory(receiverDirPath);

            String senderFilePath = senderDirPath + "/" + uuid + ".message";
            String receiverFilePath = receiverDirPath + "/" + uuid + ".message";

            try (FileOutputStream senderOutputStream = new FileOutputStream(senderFilePath);
                 ObjectOutputStream senderObjectOutputStream = new ObjectOutputStream(senderOutputStream);
                 FileOutputStream receiverOutputStream = new FileOutputStream(receiverFilePath);
                 ObjectOutputStream receiverObjectOutputStream = new ObjectOutputStream(receiverOutputStream)) {

                senderObjectOutputStream.writeObject(message);
                receiverObjectOutputStream.writeObject(message);
                output.debugPrint("Message serialized and saved to " + senderFilePath + " and " + receiverFilePath);
            } catch (IOException e) {
                output.debugPrint("Failed to save message to " + senderFilePath + " and " + receiverFilePath);
                output.debugPrint(Arrays.toString(e.getStackTrace()));
                return null;
            }
            return message;
        }
    }

    // Retrieve a message from the database using its UUID
    public static Message getMessage(String uuid) {
        synchronized (lock) {
            File rootDirectory = new File(root + "/messages");
            File[] messageDirectories = rootDirectory.listFiles();

            if (messageDirectories != null) {
                for (File directory : messageDirectories) {
                    File[] receivedDirs = new File(directory.getPath() + "/received").listFiles();
                    File[] sentDirs = new File(directory.getPath() + "/sent").listFiles();

                    if (receivedDirs != null) {
                        for (File receiverDir : receivedDirs) {
                            String filePath = receiverDir.getPath() + "/" + uuid + ".message";
                            File messageFile = new File(filePath);

                            if (messageFile.exists()) {
                                return readMessageFromFile(filePath);
                            }
                        }
                    }

                    if (sentDirs != null) {
                        for (File senderDir : sentDirs) {
                            String filePath = senderDir.getPath() + "/" + uuid + ".message";
                            File messageFile = new File(filePath);

                            if (messageFile.exists()) {
                                return readMessageFromFile(filePath);
                            }
                        }
                    }
                }
            }
            return null; // Message not found
        }
    }

    private static Message readMessageFromFile(String filePath) {
        synchronized (lock) {
            try (FileInputStream fileInputStream = new FileInputStream(filePath);
                 ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

                Message message = (Message) objectInputStream.readObject();
                output.debugPrint("Message deserialized from " + filePath);
                return message;
            } catch (IOException | ClassNotFoundException e) {
                output.debugPrint("Failed to get message from " + filePath);
                output.debugPrint(Arrays.toString(e.getStackTrace()));
                return null;
            }
        }
    }

    public static void editMessage(Message message, String newContent) {
        synchronized (lock) {
            if (!isMessageDeleted(message.sender, message)) {
                saveEditedMessage(message, newContent, message.sender);
            }
            if (!isMessageDeleted(message.receiver, message)) {
                saveEditedMessage(message, newContent, message.receiver);
            }
        }
    }

    private static void saveEditedMessage(Message message, String newContent, User user) {
        synchronized (lock) {
            String filePath = getMessageFilePath(user, message);

            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                message.setContent(newContent);
                objectOutputStream.writeObject(message);
                output.debugPrint("Message edited and saved to " + filePath);
            } catch (IOException e) {
                output.debugPrint("Failed to save edited message to " + filePath);
                output.debugPrint(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    public static boolean deleteMessage(User user, Message message) {
        synchronized (lock) {
            String uuid = message.getUuid().toString();
            boolean isDeleterSender = user.username.equals(message.getSender().username);
            String filepath = root + "/messages/";
            if (isDeleterSender) {
                filepath += message.getSender().username + "/sent/" + message.getReceiver().username;

            } else {
                filepath += message.getReceiver().username + "/received/" + message.getSender().username;
            }
            filepath += "/" + message.uuid + ".message";
            File messageFile = new File(filepath);
            if (!messageFile.exists()) {
                output.debugPrint("User " + user + " does not have access to " + message);
                return false;
            }

            messageFile.delete();
            output.debugPrint("User " + user + " successfully deleted " + message);
            return true;
        }
    }

    //HELPER METHODS
    private static String getMessageFilePath(User user, Message message) {
        synchronized (lock) {
            String senderUsername = message.getSender().getUsername();
            String receiverUsername = message.getReceiver().getUsername();
            String uuid = message.getUuid().toString();

            boolean isDeleterSender = user.getUsername().equals(senderUsername);
            String filePath = root + "/messages/";

            if (isDeleterSender) {
                filePath += senderUsername + "/sent/" + receiverUsername;
            } else {
                filePath += receiverUsername + "/received/" + senderUsername;
            }

            filePath += "/" + uuid + ".message";

            return filePath;
        }
    }

    private static boolean isMessageDeleted(User user, Message message) {
        synchronized (lock) {
            String filePath = getMessageFilePath(user, message);

            File messageFile = new File(filePath);
            return !messageFile.exists();
        }
    }

    public static ArrayList<Message> getConversation(User viewer, User otherParticipant) {
        synchronized (lock) {
            ArrayList<Message> conversation = new ArrayList<>();

            // Retrieve messages sent from the viewer to the other participant
            String sentMessagesPath = root + "/messages/" + viewer.getUsername() + "/sent/" + otherParticipant.getUsername();
            getConversationHelper(conversation, sentMessagesPath);

            // Retrieve messages received by the viewer from the other participant
            String receivedMessagesPath = root + "/messages/" + viewer.getUsername() + "/received/" + otherParticipant.getUsername();
            getConversationHelper(conversation, receivedMessagesPath);

            // Sort messages by timestamp
            conversation.sort(Comparator.comparing(Message::getTimestamp));

            return conversation;
        }
    }

    private static void getConversationHelper(ArrayList<Message> conversation, String folderPath) {
        synchronized (lock) {
            File folder = new File(folderPath);
            if (folder.exists() && folder.isDirectory()) {
                File[] messageFiles = folder.listFiles();

                if (messageFiles != null) {
                    for (File messageFile : messageFiles) {
                        Message message = readMessageFromFile(messageFile.getPath());
                        if (message != null) {
                            conversation.add(message);
                        }
                    }
                }
            }
        }
    }


    public static ArrayList<User> getAllConversations(User user) {
        synchronized (lock) {
            ArrayList<User> conversations = new ArrayList<>();
            String userMessagesPath = root + "/messages/" + user.getUsername();

            File sentDirectory = new File(userMessagesPath + "/sent");
            File receivedDirectory = new File(userMessagesPath + "/received");

            // Get unique User objects from sent messages
            if (sentDirectory.exists() && sentDirectory.isDirectory()) {
                File[] sentDirs = sentDirectory.listFiles();

                if (sentDirs != null) {
                    for (File receiverDir : sentDirs) {
                        String username = receiverDir.getName();
                        User conversationUser = getUser(username);
                        if (conversationUser != null) {
                            conversations.add(conversationUser);
                        }
                    }
                }
            }

            // Get unique User objects from received messages
            if (receivedDirectory.exists() && receivedDirectory.isDirectory()) {
                File[] receivedDirs = receivedDirectory.listFiles();

                if (receivedDirs != null) {
                    for (File senderDir : receivedDirs) {
                        String username = senderDir.getName();
                        User conversationUser = getUser(username);
                        if (conversationUser != null) {
                            conversations.add(conversationUser);
                        }
                    }
                }
            }

            // Remove duplicates and alphabetically sort
            HashSet<User> uniqueUsers = new HashSet<>(conversations);
            conversations.clear();
            conversations.addAll(uniqueUsers);
            Collections.sort(conversations, Comparator.comparing(User::getUsername));

            return conversations;
        }
    }


    //handling stores and all that stuff
    public static Store saveStore(Store store) {
        synchronized (lock) {
            if (db.readStoreFromFile(store.getStoreName()) != null) {
                output.debugPrint("Stores cannot have the same name");
                return null;
            }
            String dirPath = "storage/stores/";
            String filePath = dirPath + store.getStoreName() + ".storeInfo";
            createDirectory(dirPath);

            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

                objectOutputStream.writeObject(store);
                output.debugPrint("Store serialized and saved to " + store.getStoreName() + ".storeInfo");
            } catch (IOException e) {
                output.debugPrint("Failed to save user to " + dirPath);
                output.debugPrint(Arrays.toString(e.getStackTrace()));
                return null;
            }
            return store;
        }
    }

    public static boolean removeStore(String storeName) {
        synchronized (lock) {
            String storeDirPath = root + "/stores/" + storeName;
            File f = new File(storeDirPath);
            deleteDirectory(f);
            return true;
        }
    }

    public static ArrayList<Store> getAllStores() {
        synchronized (lock) {
            ArrayList<Store> Stores = new ArrayList<>();
            File storeDirectory = new File(root + "/stores");
            if (storeDirectory.exists() && storeDirectory.isDirectory()) {
                File[] userFiles = storeDirectory.listFiles();

                if (userFiles != null) {
                    for (File userFile : userFiles) {
                        try (FileInputStream fileInputStream = new FileInputStream(userFile);
                             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

                            Store store = (Store) objectInputStream.readObject();
                            Stores.add(store);
                        } catch (IOException | ClassNotFoundException e) {
                            output.debugPrint("Failed to read store from " + userFile.getPath());
                            output.debugPrint(Arrays.toString(e.getStackTrace()));
                        }
                    }
                }
            }

            return Stores;
        }
    }

    public static Store readStoreFromFile(String storeName) {
        synchronized (lock) {
            String storeDirPath = "storage/stores/";
            String itemFilePath = storeDirPath + "/" + storeName + ".storeInfo";

            try (FileInputStream fileInputStream = new FileInputStream(itemFilePath);
                 ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

                Store newStore = (Store) objectInputStream.readObject();
                output.debugPrint("Store deserialized from " + itemFilePath);
                return newStore;
            } catch (Exception e) {
                output.debugPrint("Failed to get message from " + itemFilePath);
                output.debugPrint(Arrays.toString(e.getStackTrace()));
                return null;
            }
        }
    }

}
