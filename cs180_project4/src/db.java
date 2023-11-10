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

        if (createDirectory(usersDirectoryPath) && createDirectory(messagesDirectoryPath)) {
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
        if (user == null) return null;
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

    public static Message saveMessage(Message message) {
        if (message.getReceiver() == null || message.getSender() == null) return null;
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
                String senderReceiverDirPath = directory.getPath() + "/received";
                File[] senderReceiverDirs = new File(senderReceiverDirPath).listFiles();

                if (senderReceiverDirs != null) {
                    for (File senderReceiverDir : senderReceiverDirs) {
                        String filePath = senderReceiverDir.getPath() + "/" + uuid + ".message";
                        File messageFile = new File(filePath);

                        if (messageFile.exists()) {
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
                }
            }
        }

        return null; // Message not found
    }
    
    public static String editMessage (Message editMessage, String newMessage) {
        if (editMessage != null) {
            editMessage.setMessage(newMessage);
            saveMessage(editMessage);
            editMessage.updateCurrentTimeStamp();
            return editMessage.getMessage();
        }
        output.debugPrint("Error, message does not exist");
        return null;
    }

    public static boolean removeMessage(int senderReceiver, Message message) { // 0 to make undreable to senders, 1 for receivers
        
        //Add functionality in main method to not display messages to relevant users who want to delete messages
        
        if (senderReceiver == 0) {
            message.setReceiverReadable(false);
            output.debugPrint("Message made unreadable to receiver");
            db.saveMessage(message);
            return true;
        }

        if (senderReceiver == 1) {
            message.setSenderReadable(false);
            output.debugPrint("Message made unreadable to sender");
            db.saveMessage(message);
            return true;
        }

        output.debugPrint("Error, enter 0 or 1 to make the message unreadable to senders or receivers");
        return false;

    }
}

