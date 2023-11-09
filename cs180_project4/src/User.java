
import java.io.Serializable;

enum userType {
    CUSTOMER,
    SELLER
}

public class User implements Serializable {
    userType type; //false = customer. true = seller
    String username;
    String password;

    public static User createUser(userType type, String username, String password) {
        if(db.getUser(username) != null){
            output.debugPrint("User with username {" + username + "} already exists.");
            return null;
        }
        User user = new User(type, username, password);
        db.saveUser(user);
        output.debugPrint("Created and saved new User : " + user);
        return user;
    }

    private User(userType type, String username, String password) {
        this.type = type;
        this.username = username;
        this.password = password;
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

    @Override
    public String toString() {
        return "User{" +
                "type=" + type +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


}
