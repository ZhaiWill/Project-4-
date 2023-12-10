import java.io.Serializable;
import java.util.*;

enum userType {
    CUSTOMER, SELLER,
}


enum userBlockStatus {
    INVISIBLE, BLOCKED, ALLOWED
}

public class User implements Serializable {
    userType type; //false = customer. true = seller
    String username;
    String password;
    String email;
    Map<String, userBlockStatus> userBlockStatusMap;

    public static User createUser(userType type, String username, String password, String email) {
        if (DBClient.getUser(username) != null) {
            output.debugPrint("User with username {" + username + "} already exists.");
            return null;
        }
        User user = new User(type, username, password, email);
        DBClient.saveUser(user);
        output.debugPrint("Created and saved new User : " + user);
        return user;
    }


    /**
     * If return type is null the message can not be sent due to the user being blocked
     *
     * @param receiver
     * @param message
     * @return
     */
    public Message sendmessage(User receiver, String message) {
        if (receiver.getUserBlockedStatus(receiver) != userBlockStatus.ALLOWED) return null;
        return DBClient.createMessage(new Message(this, receiver, message));

    }

    public User(userType type, String username, String password, String email) {
        this.type = type;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userBlockStatusMap = new HashMap<String, userBlockStatus>();
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

    /**
     * Returns a user type object stating whether the user that userBlockedStatus is being called on has become invisble to or has blocked the user passed as input
     *
     * @param user
     * @return
     */
    public userBlockStatus getUserBlockedStatus(User user) {
        return this.userBlockStatusMap.getOrDefault(user.username, userBlockStatus.ALLOWED);
    }

    public String toString() {
        return "User{" + "type=" + type + ", username='" + username + '\'' + ", password='" + password + '\'' + ", email='" + email + '}';
    }

    public void setUserBlockStatus(String username, userBlockStatus blockStatus) {
        this.userBlockStatusMap.put(username, blockStatus);
    }

    public ArrayList<User> getAllAccessibleUsers() {
        //return all users that have getUserBlockedStatus return type of ALLOWED
        ArrayList<User> accessibleUsers = new ArrayList<>();

        for (User user : DBClient.getAllUsers()) {
            if (user.getUserBlockedStatus(this) != userBlockStatus.INVISIBLE && (this.type != user.type)) {
                accessibleUsers.add(user);
            }
        }

        return accessibleUsers;
    }
    public ArrayList<Store> getAllAccessibleStores() {
        //return all users that have getUserBlockedStatus return type of ALLOWED
        ArrayList<Store> accessibleStores = new ArrayList<>();

        for (Store store : DBClient.getAllStores()) {
            User storeOwner = DBClient.getUser(store.getownerUserName());
            if (storeOwner.getUserBlockedStatus(this) != userBlockStatus.INVISIBLE && (this.type != storeOwner.type)) {
                accessibleStores.add(store);
            }
        }

        return accessibleStores;
    }


    public ArrayList<String> getAllAccessibleConversations() {
        ArrayList<User> allConversations = DBClient.getAllConversations(this);
        ArrayList<String> accessibleConversations = new ArrayList<>();

        for (User user : allConversations) {
            if (user.getUserBlockedStatus(this) != userBlockStatus.INVISIBLE && (this.type != user.type)) {
                accessibleConversations.add(user.username);
            }
        }
        //remove all duplicates accessibleConversations and sort alphabetically

        ArrayList<String> uniqueAccessibleConversations = new ArrayList<>(new HashSet<>(accessibleConversations));
        Collections.sort(uniqueAccessibleConversations);

        return uniqueAccessibleConversations;
    }




}
