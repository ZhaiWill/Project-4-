import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class User implements Serializable {
    userType type; //false = customer. true = seller
    String username;
    String password;
    String email;
    List<String> blockedUsers;
    List<String> invisibleUsers;
    public static User createUser(userType type, String username, String password, String email) {
        if (db.getUser(username) != null) {
            output.debugPrint("User with username {" + username + "} already exists.");
            return null;
        }
        User user = new User(type, username, password, email);
        db.saveUser(user);
        output.debugPrint("Created and saved new User : " + user);
        return user;
    }


    public Message sendmessage(User receiver, String message) {

        return db.saveMessage(new Message(this, receiver, message));

    }

    public Message sendMessage(User receiver, File file) {
        try {
            String fileContents = new String(Files.readAllBytes(file.toPath()));
            return db.saveMessage(new Message(this, receiver, fileContents));
        } catch (IOException e) {
            e.printStackTrace();
            return null; 
        }
    }

    private User(userType type, String username, String password, String email) {
        this.type = type;
        this.username = username;
        this.password = password;
        this.email = email;
        this.blockedUsers = new ArrayList<>();
        this.invisibleUsers = new ArrayList<>();
    }
    public userType isType() {
        return type;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getInvisibleUsers() {
        return invisibleUsers;
    }
    public List<String> getBlockedUsers() {
        return blockedUsers;
    }

    @Override
    public String toString() {
        return "User{" + "type=" + type + ", username='" + username + '\'' + ", password='" + password + '\'' + ", email='" + email + '}';
    }
    public void blockUser(User userToBlock) {
        blockedUsers.add(userToBlock.getUsername());
        System.out.println(username + " has blocked " + userToBlock.getUsername());
    }
    public void unblockUser(User userToUnblock) {
        blockedUsers.remove(userToUnblock.getUsername());
        System.out.println(username + " has unblocked " + userToUnblock.getUsername());
    }
    public void becomeInvisible(User invisible) {
        invisibleUsers.add(invisible.getUsername());
        System.out.println("You have become invisible to: " + invisible.getUsername());
    }
    public void becomeUninvisible(User unInvisible) {
        invisibleUsers.remove(unInvisible.getUsername());
        System.out.println("You have become visible to: " + unInvisible.getUsername());
    }
}
