import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import javax.swing.*;
import java.awt.*;

public class db {
    private static String root = "storage";

    // Clear the entire database (CAUTION: WILL REMOVE ALL DATA)
    public static boolean clearDatabase() {
        File rootDirectory = new File(root);
        if (rootDirectory.exists()) {
            deleteDirectory(rootDirectory);
            output.debugPrint("Database cleared.");
        } else {
            output.debugPrint("Database does not exist, nothing to clear.");
        }
        return true;
    }

    private static void deleteDirectory(File directory) {
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

    // Clear the entire database (CAUTION: WILL REMOVE ALL DATA)
    public static boolean initializeDatabase() {
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

        if (createDirectory(usersDirectoryPath) && createDirectory(messagesDirectoryPath)  && createDirectory(storesDirectoryPath)) {
            output.debugPrint("User and messages directories created.");
            return true;
        } else {
            output.debugPrint("Failed to create user or messages directory.");
            return false;
        }
    }

    private static boolean createDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return true;
    }
    //CAUTION. WILL OVERWRITE
    public static User saveUser(User user) {
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

    public static User getUser(String username) {
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

    public static boolean deleteUser(User user) {
        String username = user.getUsername();
        String filePath = "storage/users/" + username + ".user";
        File file = new File(filePath);
        file.delete();
        output.debugPrint("User deleted");
        return true;
    }

    public static boolean editUsername(User user, String newUsername) {
        String oldUsername = user.getUsername();
        if (getUser(newUsername) != null) {
            output.debugPrint("User with username {" + newUsername + "} already exists.");
            return false;
        } else {
            user.setUsername(newUsername);
            String oldFilePath = "storage/users/" + oldUsername + ".user";
            String newFilePath = "storage/users/" + newUsername + ".user";
            File oldFile = new File(oldFilePath);
            File newFile = new File(newFilePath);
            if (oldFile.exists()) {
                if (oldFile.renameTo(newFile)) {
                    output.debugPrint("Username updated. User file moved from " + oldFilePath + " to " + newFilePath);
                } else {
                    output.debugPrint("Failed to update username.");
                    return false;
                }
            }
            return true;
        }
    }
    public static boolean editPassword(User user, String newPassword) {
        user.setPassword(newPassword);
        saveUser(user);
        return true;
    }

    public static Message saveMessage(Message message) {
        if (!Message.isValidMessage(message)) return null;
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

    // Retrieve a message from the database using its UUID
    public static Message getMessage(String uuid) {
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

    private static Message readMessageFromFile(String filePath) {
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

    public static void editMessage(Message message, String newContent) {
        if (!isMessageDeleted(message.sender, message)) {
            saveEditedMessage(message, newContent, message.sender);
        }
        if (!isMessageDeleted(message.receiver, message)) {
            saveEditedMessage(message, newContent, message.receiver);
        }
    }
    private static void saveEditedMessage(Message message, String newContent, User user) {
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

    public static boolean removeMessage(User user, Message message) {
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

    public static ArrayList<User> getAllCorrespondents(User u) { //returns an arraylist of all people who have sent a message to u
        ArrayList<User> correspondents = new ArrayList<User>();
        String fileName = root + "/messages/" + u.getUsername();
        String receivedName = fileName + "/received";
        String sentName = fileName + "/sent";
        try {
            File f = new File(fileName);
            File[] files = f.listFiles();
            for (File thing : files) {
                File[] deepestLayer = thing.listFiles();
                for (File deepestFile : deepestLayer) {
                    String absPath = deepestFile.getAbsolutePath();
                    int index = absPath.lastIndexOf("/");
                    String otherUser = absPath.substring(index);
                    if (correspondents.indexOf(getUser(otherUser)) == -1) {
                        correspondents.add(getUser(otherUser));
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return correspondents;
    }
    public static ArrayList<String> getAllStoreNames() {
        ArrayList<String> storeNames = new ArrayList<String>();
        String fileName = root + "/stores/";
        try {
            File f = new File(fileName);
            File[] files = f.listFiles();
            if(files != null) {
                for (File thing : files) {
                    File[] deepestLayer = thing.listFiles();
                    for (File deepestFile : files) {
                        String absPath = deepestFile.getName();
                        Store store2 = getStore(absPath);
                        if (store2 != null && storeNames.indexOf(absPath) == -1) {
                            storeNames.add(store2.getName());
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return storeNames;
    }
        public static ArrayList<String> getAllNames(User u) {
            ArrayList<String> correspondents = new ArrayList<String>();
            String fileName = root + "/messages/" + u.getUsername() + "/received";
            try {
                File f = new File(fileName);
                File[] files = f.listFiles();
                if(files != null) {
                    for (File thing : files) {
                        File[] deepestLayer = thing.listFiles();
                        for (File deepestFile : files) {
                            String absPath = deepestFile.getName();
                            User user2 = getUser(absPath);
                            if (user2 != null && correspondents.indexOf(absPath) == -1) {
                                correspondents.add(user2.getUsername());
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return correspondents;
        }

    public static ArrayList<Message> findAllMessages(User user1, User user2) {
        ArrayList<Message> messageLog = new ArrayList<>();
        String username1 = user1.getUsername();
        String username2 = user2.getUsername();
        String filePath1 = "storage/messages/" + username1 + "/sent/" + username2;
        String filePath2 = "storage/messages/" + username2 + "/sent/" + username1;

        try {
            File f1 = new File(filePath1);
            File[] f1Array = f1.listFiles();
            Message[] f1MessageArray = new Message[f1Array != null ? f1Array.length : 0];

            File f2 = new File(filePath2);
            File[] f2Array = f2.listFiles();
            Message[] f2MessageArray = new Message[f2Array != null ? f2Array.length : 0];

            for (int i = 0; i < f1MessageArray.length && i < f2MessageArray.length; i++) {
                f1MessageArray[i] = readMessageFromFile(f1Array[i].getPath());
                f2MessageArray[i] = readMessageFromFile(f2Array[i].getPath());
            }

            // Sorting both arrays based on timestamps
            Comparator<Message> timestampComparator = Comparator.comparing(Message::getTimestamp, Comparator.nullsLast(Comparator.naturalOrder()));
            Arrays.sort(f1MessageArray, timestampComparator);
            Arrays.sort(f2MessageArray, timestampComparator);

            // Merge sorted arrays into messageLog
            int i = 0, j = 0;
            while (i < f1MessageArray.length || j < f2MessageArray.length) {
                if (i < f1MessageArray.length && (j == f2MessageArray.length || timestampComparator.compare(f1MessageArray[i], f2MessageArray[j]) <= 0)) {
                    messageLog.add(f1MessageArray[i]);
                    i++;
                } else {
                    messageLog.add(f2MessageArray[j]);
                    j++;
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageLog;
    }


    //HELPER METHODS
    private static String getMessageFilePath(User user, Message message) {
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
    private static boolean isMessageDeleted(User user, Message message) {
        String filePath = getMessageFilePath(user, message);

        File messageFile = new File(filePath);
        return !messageFile.exists();
    }
    public static Store saveStore(Store store) {
        String filePath = "storage/stores/" + store.getName() + "/";
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(store);
            output.debugPrint("Store serialized and saved to " + filePath);
        } catch (IOException e) {
            output.debugPrint("Failed to save store to " + filePath);
            output.debugPrint(Arrays.toString(e.getStackTrace()));
            return null;
        }
        return store;
    }
    public static Store getStore(String storeName) {
        String filePath = "storage/stores/" + storeName;
        try (FileInputStream fileInputStream = new FileInputStream(filePath); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            Store store = (Store) objectInputStream.readObject();
            output.debugPrint("Store deserialized from " + filePath);
            return store;
        } catch (FileNotFoundException e){
            output.debugPrint("Store file not found at " + filePath);
        } catch (IOException | ClassNotFoundException e) {
            output.debugPrint("Failed to get store from " + filePath);
            output.debugPrint(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return null;
    }


    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                deleteDir(f);
                }
            }
        }
        file.delete();
    }

    public static boolean removeStore(String storeName) {
        String storeDirPath = root + "/stores/" + storeName;
        File f = new File(storeDirPath);
        deleteDir(f);
        return true;
    }
    public static boolean editStore(String newName, String oldName) {
        Store store1 = getStore(oldName);
        if (getStore(newName) != null) {
            output.debugPrint("Store with username {" + newName + "} already exists.");
            return false;
        } else {
            store1.setName(newName);
            if (store1.getName() == newName) {
                saveStore(store1);
                removeStore(oldName);
                output.debugPrint("Store updated." );
            } else {
                output.debugPrint("Failed to update store name.");
                return false;
            }
        }
        return true;
    }
   

    public static Store readStoreFromFile(String storeName){

        String storeDirPath = root + "/stores/" + storeName;
        String itemFilePath = storeDirPath + "/" + storeName;

        try (FileInputStream fileInputStream = new FileInputStream(itemFilePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            Store newStore = (Store) objectInputStream.readObject();
            output.debugPrint("Message deserialized from " + itemFilePath);
            return newStore;
        } catch (Exception e) {
            output.debugPrint("Failed to get message from " + itemFilePath);
            output.debugPrint(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }


     //HELPER METHODS



    public static ArrayList<Message> getConversation(User viewer, User otherParticipant) {
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

    private static void getConversationHelper(ArrayList<Message> conversation, String folderPath) {
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


    public static ArrayList<User> getAllConversations(User user) {
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


    //handling stores and all that stuff


    public static ArrayList<Store> getAllStores() {
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
