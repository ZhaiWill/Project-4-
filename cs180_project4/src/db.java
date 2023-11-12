import java.io.*;
import java.util.Arrays;


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
        String dirPath = "storage/stores/" + store.getName() + ".store";
        
        createDirectory(dirPath);

        try (FileOutputStream fileOutputStream = new FileOutputStream(dirPath); 
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(store);
            output.debugPrint("User serialized and saved to " + store + ".store");
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
        
        String storeDirPath = root + "/stores/" + storeName + ".store";

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

    public static boolean removeStore(Store store) {
        String storeDirPath = root + "/stores/" + store.getName() + ".store";
        File f = new File(storeDirPath);
        f.delete();
        return true;
    }
    
    public static boolean removeItem(Store store, Item item) {
        String itemName = item.getName();
        String storeName = store.getName();
        
        String storeDirPath = root + "/stores/" + storeName + ".store";
        String itemFilePath = storeDirPath + "/" + itemName + ".item";

        File f = new File(itemFilePath);
        f.delete();
        return true;
    }

    public static Item readItemFromFile(Store store, String itemName) {
        
        String storeName = store.getName();
        
        String storeDirPath = root + "/stores/" + storeName + ".store";
        String itemFilePath = storeDirPath + "/" + itemName + ".item";
        
        try (FileInputStream fileInputStream = new FileInputStream(itemFilePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            
            Item newItem = (Item) objectInputStream.readObject();
            output.debugPrint("Message deserialized from " + itemFilePath);
            return newItem;
        } catch (IOException | ClassNotFoundException e) {
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
