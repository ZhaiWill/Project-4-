import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
public class Store implements Serializable {
    String name;
    User owner;

    public static Store createStore(String name, User user) {
        if (db.getStore(name) != null) {
            output.debugPrint("Store with name {" + name + "} already exists.");
            return null;
        }
        Store store = new Store(name, user);
        db.saveStore(store);
        output.debugPrint("Created and saved new Store : " + store);
        return store;
    }
    public Store(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
    public String toString() {
        return "Item<Owner=" + owner +
        ", Name=" + name + ">";
    }
}
