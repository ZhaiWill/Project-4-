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
    public static void saveUser(User user) {
        String filePath = "storage/users/" + user.getUsername() + ".user";

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(user);
            output.debugPrint("User serialized and saved to " + filePath);
        } catch (IOException e) {
            output.debugPrint("Failed to save user to " + filePath);
            output.debugPrint(Arrays.toString(e.getStackTrace()));
        }
    }

    public static User getUser(String username) {
        String filePath = "storage/users/" + username + ".user";

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

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


