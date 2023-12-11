import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class DBServer {
    private static db dbInstance = new db();
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
//            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            while (true) {
                String methodName = (String) in.readObject();

                switch (methodName) {
                    case "clearDatabase":
                        out.writeObject(dbInstance.clearDatabase());
                        break;
                    case "initializeDatabase":
                        out.writeObject(dbInstance.initializeDatabase());
                        break;
                    case "saveUser":
                        User userToSave = (User) in.readObject();
                        out.writeObject(dbInstance.saveUser(userToSave));
                        break;
                    case "getUser":
                        String username = (String) in.readObject();
                        out.writeObject(dbInstance.getUser(username));
                        break;
                    case "deleteUser":
                        User userToDelete = (User) in.readObject();
                        out.writeObject(dbInstance.deleteUser(userToDelete));
                        break;
                    case "getAllUsers":
                        out.writeObject(dbInstance.getAllUsers());
                        break;
                    case "editUsername":
                        User userToEdit = (User) in.readObject();
                        String newUsername = (String) in.readObject();
                        out.writeObject(dbInstance.editUsername(userToEdit, newUsername));
                        break;
                    case "editPassword":
                        User userToEditPassword = (User) in.readObject();
                        String newPassword = (String) in.readObject();
                        out.writeObject(dbInstance.editPassword(userToEditPassword, newPassword));
                        break;
                    case "createMessage":
                        Message messageToCreate = (Message) in.readObject();
                        out.writeObject(dbInstance.createMessage(messageToCreate));
                        break;
                    case "getMessage":
                        String messageUuid = (String) in.readObject();
                        out.writeObject(dbInstance.getMessage(messageUuid));
                        break;
                    case "editMessage":
                        Message messageToEdit = (Message) in.readObject();
                        String newContent = (String) in.readObject();
                        dbInstance.editMessage(messageToEdit, newContent);
                        break;
                    case "deleteMessage":
                        User userDeletingMessage = (User) in.readObject();
                        Message messageToDelete = (Message) in.readObject();
                        out.writeObject(dbInstance.deleteMessage(userDeletingMessage, messageToDelete));
                        break;
                    case "getConversation":
                        User viewer = (User) in.readObject();
                        User otherParticipant = (User) in.readObject();
                        out.writeObject(dbInstance.getConversation(viewer, otherParticipant));
                        break;
                    case "getAllConversations":
                        User userForConversations = (User) in.readObject();
                        out.writeObject(dbInstance.getAllConversations(userForConversations));
                        break;
                    case "saveStore":
                        Store storeToSave = (Store) in.readObject();
                        out.writeObject(dbInstance.saveStore(storeToSave));
                        break;
                    case "removeStore":
                        String storeNameToRemove = (String) in.readObject();
                        out.writeObject(dbInstance.removeStore(storeNameToRemove));
                        break;
                    case "getAllStores":
                        out.writeObject(dbInstance.getAllStores());
                        break;
                    case "readStoreFromFile":
                        String storeNameToRead = (String) in.readObject();
                        out.writeObject(dbInstance.readStoreFromFile(storeNameToRead));
                        break;
                    default:
                        System.out.println("Unknown method: " + methodName);
                        break;
                }
                out.flush();
            }

        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
        }
    }
}
