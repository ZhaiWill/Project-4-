import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class DBClient {
    private static final String SERVER_IP = "localhost"; // Use the server's IP address
    private static final int PORT = 12345; // Use the same port as the server

    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    public DBClient() {
        try {
            socket = new Socket(SERVER_IP, PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean clearDatabase() {
        try {
            out.writeObject("clearDatabase");
            out.flush();
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean initializeDatabase() {
        try {
            out.writeObject("initializeDatabase");
            out.flush();
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User saveUser(User user) {
        try {
            out.writeObject("saveUser");
            out.writeObject(user);
            out.flush();
            return (User) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User getUser(String username) {
        try {
            out.writeObject("getUser");
            out.writeObject(username);
            out.flush();
            return (User) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean deleteUser(User user) {
        try {
            out.writeObject("deleteUser");
            out.writeObject(user);
            out.flush();
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<User> getAllUsers() {
        try {
            out.writeObject("getAllUsers");
            out.flush();
            return (ArrayList<User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean editUsername(User user, String newUsername) {
        try {
            out.writeObject("editUsername");
            out.writeObject(user);
            out.writeObject(newUsername);
            out.flush();
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean editPassword(User user, String newPassword) {
        try {
            out.writeObject("editPassword");
            out.writeObject(user);
            out.writeObject(newPassword);
            out.flush();
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Message createMessage(Message message) {
        try {
            out.writeObject("createMessage");
            out.writeObject(message);
            out.flush();
            return (Message) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Message getMessage(String uuid) {
        try {
            out.writeObject("getMessage");
            out.writeObject(uuid);
            out.flush();
            return (Message) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void editMessage(Message message, String newContent) {
        try {
            out.writeObject("editMessage");
            out.writeObject(message);
            out.writeObject(newContent);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteMessage(User user, Message message) {
        try {
            out.writeObject("deleteMessage");
            out.writeObject(user);
            out.writeObject(message);
            out.flush();
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Message> getConversation(User viewer, User otherParticipant) {
        try {
            out.writeObject("getConversation");
            out.writeObject(viewer);
            out.writeObject(otherParticipant);
            out.flush();
            return (ArrayList<Message>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<User> getAllConversations(User user) {
        try {
            out.writeObject("getAllConversations");
            out.writeObject(user);
            out.flush();
            return (ArrayList<User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Store saveStore(Store store) {
        try {
            out.writeObject("saveStore");
            out.writeObject(store);
            out.flush();
            return (Store) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean removeStore(String storeName) {
        try {
            out.writeObject("removeStore");
            out.writeObject(storeName);
            out.flush();
            return (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Store> getAllStores() {
        try {
            out.writeObject("getAllStores");
            out.flush();
            return (ArrayList<Store>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Store readStoreFromFile(String storeName) {
        try {
            out.writeObject("readStoreFromFile");
            out.writeObject(storeName);
            out.flush();
            return (Store) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        DBClient dbClient = new DBClient();

        // Example: Clear Database
        boolean clearResult = dbClient.clearDatabase();
//        System.out.println("Database cleared: " + clearResult);

        // Example: Initialize Database
        boolean initResult = dbClient.initializeDatabase();
//        System.out.println("Database initialized: " + initResult);

        // Example: Save User
        User userToSave = new User(userType.CUSTOMER, "john_doe", "123", "seller1email@gmail.com");
        User savedUser = dbClient.saveUser(userToSave);
//        System.out.println("User saved: " + savedUser);

        // Example: Get User
        User retrievedUser = dbClient.getUser("john_doe");
//        System.out.println("User retrieved: " + retrievedUser);

        // Add more examples for other methods
    }
}
