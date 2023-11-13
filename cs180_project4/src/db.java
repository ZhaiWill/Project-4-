import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;

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
        if (db.getUser(user.getUsername()) != null) {
            output.debugPrint("User with username {" + user.getUsername() + "} already exists.");
            return false;
        }
        user.setUsername(newUsername);
        saveUser(user);
        return true;
    }

    public static boolean editPassword(User user, String newUsername) {
        user.setPassword(newUsername);
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

    public static ArrayList<Message> findAllMessages(User user1, User user2) { // returns all messages between user1 and user2 in order
        ArrayList<Message> messageLog = new ArrayList<Message>();
        String username1 = user1.getUsername();
        String username2 = user2.getUsername();
        String filePath1 = "storage/messages" + username1 + "/sent/" + username2;
        String filePath2 = "storage/messages" + username2 + "/sent/" + username1;
        try {
            File f1 = new File(filePath1);
            File[] f1Array = f1.listFiles();
            Message[] f1MessageArray = new Message[f1Array.length];
            File f2 = new File(filePath2);
            File[] f2Array = f2.listFiles();
            Message[] f2MessageArray = new Message[f2Array.length];
            for (int i = 0; i < f1Array.length; i++) {
                Message m1 = readMessageFromFile(f1Array[i].getPath());
                Message m2 = readMessageFromFile(f2Array[i].getPath());
                f1MessageArray[i] = m1;
                f2MessageArray[i] = m2;
            }
            messageLog.add(f1MessageArray[0]);
            boolean added = false;
            for (int i = 1; i < f1MessageArray.length; i++) { //sorting
                Date ts = f1MessageArray[i].getTimestamp();
                for (int j = 0; j < messageLog.size(); j++) {
                    Date ts2 = messageLog.get(j).getTimestamp();
                    if (ts.compareTo(ts2) > 0) {
                        messageLog.add(j, f1MessageArray[i]);
                        added = true;
                    }
                    if (!added) {
                        messageLog.add(f1MessageArray[i]);
                    }
                    added = false;
                }
            }
            for (int i = 1; i < f2MessageArray.length; i++) { //sorting
                Date ts = f2MessageArray[i].getTimestamp();
                for (int j = 0; j < messageLog.size(); j++) {
                    Date ts2 = messageLog.get(j).getTimestamp();
                    if (ts.compareTo(ts2) > 0) {// compareto > 0 when ts is before ts2
                        messageLog.add(j, f2MessageArray[i]); // add it before whatever its before first
                        added = true;
                    }
                    if (!added) {
                        messageLog.add(f2MessageArray[i]); //if not inserted already, put at end
                    }
                    added = false;
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageLog;
    }
    
    public static ArrayList<Message> findAllMessages(User user1, User user2) { // returns all messages between user1 and user2 in order
        ArrayList<Message> messageLog = new ArrayList<Message>();
        String username1 = user1.getUsername();
        String username2 = user2.getUsername();
        String filePath1 = "storage/messages" + username1 + "/sent/" + username2;
        String filePath2 = "storage/messages" + username2 + "/sent/" + username1;
        try {
            File f1 = new File(filePath1);
            File[] f1Array = f1.listFiles();
            Message[] f1MessageArray = new Message[f1Array.length];
            File f2 = new File(filePath2);
            File[] f2Array = f2.listFiles();
            Message[] f2MessageArray = new Message[f2Array.length];
            for (int i = 0; i < f1Array.length; i++) {
                Message m1 = readMessageFromFile(f1Array[i].getPath());
                Message m2 = readMessageFromFile(f2Array[i].getPath());
                f1MessageArray[i] = m1;
                f2MessageArray[i] = m2;
            }
            messageLog.add(f1MessageArray[0]);
            boolean added = false;
            for (int i = 1; i < f1MessageArray.length; i++) { //sorting
                Date ts = f1MessageArray[i].getTimestamp();
                for (int j = 0; j < messageLog.size(); j++) {
                    Date ts2 = messageLog.get(j).getTimestamp();
                    if (ts.compareTo(ts2) > 0) {
                        messageLog.add(j, f1MessageArray[i]);
                        added = true;
                    }
                    if (!added) {
                        messageLog.add(f1MessageArray[i]);
                    }
                    added = false;
                }
            }
            for (int i = 1; i < f2MessageArray.length; i++) { //sorting
                Date ts = f2MessageArray[i].getTimestamp();
                for (int j = 0; j < messageLog.size(); j++) {
                    Date ts2 = messageLog.get(j).getTimestamp();
                    if (ts.compareTo(ts2) > 0) {// compareto > 0 when ts is before ts2
                        messageLog.add(j, f2MessageArray[i]); // add it before whatever its before first
                        added = true;
                    }
                    if (!added) {
                        messageLog.add(f2MessageArray[i]); //if not inserted already, put at end
                    }
                    added = false;
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
        if (db.readStoreFromFile(store.getName()) != null) {
            output.debugPrint("Stores cannot have the same name");
            return null;
        }
        String dirPath = "storage/stores/" + store.getName() + "/";
        String filePath = dirPath + store.getName() + ".storeInfo";
        createDirectory(dirPath);

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath); 
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(store);
            output.debugPrint("Store serialized and saved to " + store.getName() + ".storeInfo");
        } catch (IOException e) {
            output.debugPrint("Failed to save user to " + dirPath);
            output.debugPrint(Arrays.toString(e.getStackTrace()));
            return null;
        }
        return store;
    }

    public static Item saveItem(Store store, Item item) {
        String itemName = item.getName();
        String storeName = store.getName();
        
        String storeDirPath = root + "/stores/" + storeName;

        createDirectory(storeDirPath);

        String itemFilePath = storeDirPath + "/" + itemName + ".item";

        try (FileOutputStream senderOutputStream = new FileOutputStream(itemFilePath);
             ObjectOutputStream senderObjectOutputStream = new ObjectOutputStream(senderOutputStream)){

            senderObjectOutputStream.writeObject(item);
            output.debugPrint("Wrote object to " + itemFilePath);
        } catch (IOException e) {
            output.debugPrint("Failed to write item to " + itemFilePath);
            output.debugPrint(Arrays.toString(e.getStackTrace()));
            return null;
        }
        return item;
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
    
    public static boolean removeItem(Store store, Item item) {
        String itemName = item.getName();
        String storeName = store.getName();
        
        String storeDirPath = root + "/stores/" + storeName;
        String itemFilePath = storeDirPath + "/" + itemName + ".item";

        File f = new File(itemFilePath);
        f.delete();
        return true;
    }

    public static Item readItemFromFile(Store store, String itemName) {
        
        String storeName = store.getName();
        
        String storeDirPath = root + "/stores/" + storeName;
        String itemFilePath = storeDirPath + "/" + itemName + ".item";
        
        try (FileInputStream fileInputStream = new FileInputStream(itemFilePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            
            Item newItem = (Item) objectInputStream.readObject();
            output.debugPrint("Message deserialized from " + itemFilePath);
            return newItem;
        } catch (Exception e) {
            output.debugPrint("Failed to get message from " + itemFilePath);
            output.debugPrint(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    public static Store readStoreFromFile(String storeName){
        
        String storeDirPath = root + "/stores/" + storeName;
        String itemFilePath = storeDirPath + "/" + storeName + ".storeInfo";
        
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

   public static boolean buyItem(Store store, String itemName, int quantity) {
        Item item = readItemFromFile(store, itemName);
        if (item.getQuantity() - quantity < 0) {
            output.debugPrint("Cannot have items with quantity less than 0");
            return false;
        }
        item.setQuantity(item.getQuantity() - quantity);
        db.saveItem(store, item);
        output.debugPrint("Bought item successfully");
        return true;
    }
    
    public static boolean restockItem(Store store, String itemName, int quantity) {
        Item item = readItemFromFile(store, itemName);
        if (quantity <= 0) {
            output.debugPrint("Cannot restock item with quantity less than or equal to zero");
            return false;
        }
        item.setQuantity(item.getQuantity() + quantity);
        db.saveItem(store, item);
        output.debugPrint("Restocked item successfully");
        return true;
    }
    
}
