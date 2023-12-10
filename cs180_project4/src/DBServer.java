import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class DBServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Handle client in a separate thread
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            while (true) {
                String methodName = (String) in.readObject();

                switch (methodName) {
                    case "clearDatabase":
                        out.writeObject(db.clearDatabase());
                        break;
                    case "initializeDatabase":
                        out.writeObject(db.initializeDatabase());
                        break;
                    case "saveUser":
                        User userToSave = (User) in.readObject();
                        out.writeObject(db.saveUser(userToSave));
                        break;
                    case "getUser":
                        String username = (String) in.readObject();
                        out.writeObject(db.getUser(username));
                        break;
                    case "deleteUser":
                        User userToDelete = (User) in.readObject();
                        out.writeObject(db.deleteUser(userToDelete));
                        break;
                    case "getAllUsers":
                        out.writeObject(db.getAllUsers());
                        break;
                    case "editUsername":
                        User userToEdit = (User) in.readObject();
                        String newUsername = (String) in.readObject();
                        out.writeObject(db.editUsername(userToEdit, newUsername));
                        break;
                    case "editPassword":
                        User userToEditPassword = (User) in.readObject();
                        String newPassword = (String) in.readObject();
                        out.writeObject(db.editPassword(userToEditPassword, newPassword));
                        break;
                    case "createMessage":
                        Message messageToCreate = (Message) in.readObject();
                        out.writeObject(db.createMessage(messageToCreate));
                        break;
                    case "getMessage":
                        String messageUuid = (String) in.readObject();
                        out.writeObject(db.getMessage(messageUuid));
                        break;
                    case "editMessage":
                        Message messageToEdit = (Message) in.readObject();
                        String newContent = (String) in.readObject();
                        db.editMessage(messageToEdit, newContent);
                        break;
                    case "deleteMessage":
                        User userDeletingMessage = (User) in.readObject();
                        Message messageToDelete = (Message) in.readObject();
                        out.writeObject(db.deleteMessage(userDeletingMessage, messageToDelete));
                        break;
                    case "getConversation":
                        User viewer = (User) in.readObject();
                        User otherParticipant = (User) in.readObject();
                        out.writeObject(db.getConversation(viewer, otherParticipant));
                        break;
                    case "getAllConversations":
                        User userForConversations = (User) in.readObject();
                        out.writeObject(db.getAllConversations(userForConversations));
                        break;
                    case "saveStore":
                        Store storeToSave = (Store) in.readObject();
                        out.writeObject(db.saveStore(storeToSave));
                        break;
                    case "removeStore":
                        String storeNameToRemove = (String) in.readObject();
                        out.writeObject(db.removeStore(storeNameToRemove));
                        break;
                    case "getAllStores":
                        out.writeObject(db.getAllStores());
                        break;
                    case "readStoreFromFile":
                        String storeNameToRead = (String) in.readObject();
                        out.writeObject(db.readStoreFromFile(storeNameToRead));
                        break;
                    default:
                        System.out.println("Unknown method: " + methodName);
                        break;
                }
                out.flush();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
