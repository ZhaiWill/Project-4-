import java.io.Serializable;

enum userType {
    CUSTOMER, SELLER
}

public class User implements Serializable {
    userType type; //false = customer. true = seller
    String username;
    String password;
    String email;

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


}
