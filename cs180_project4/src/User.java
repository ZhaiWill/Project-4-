import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
enum userType {
    CUSTOMER, SELLER
}

public class User implements Serializable {
    userType type; //false = customer. true = seller
    String username;
    String password;
    String email;
    List<User> blockedUsers;
    List<User> invisibleUsers;
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

    @Override
    public String toString() {
        return "User{" + "type=" + type + ", username='" + username + '\'' + ", password='" + password + '\'' + ", email='" + email + '}';
    }
    public void blockUser(User userToBlock) {
        blockedUsers.add(userToBlock);
        System.out.println(username + " has blocked " + userToBlock.getUsername());
    }
    public void unblockUser(User userToUnblock) {
        blockedUsers.remove(userToUnblock);
        System.out.println(username + " has unblocked " + userToUnblock.getUsername());
    }
    public void becomeInvisible(User invisible) {
        invisibleUsers.add(invisible);
        System.out.println("You have become invisible to: " + invisible.getUsername());
    }
    public void becomeUninvisible(User unInvisible) {
        invisibleUsers.remove(unInvisible);
        System.out.println("You have become visible to: " + unInvisible.getUsername());
    }
}
