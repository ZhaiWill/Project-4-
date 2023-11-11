import java.util.ArrayList;
public class Store {
    String name;
    User owner;
    ArrayList<Item> items;
    
    public Store(String name, User owner, ArrayList<Item> items) {
        this.name = name;
        this.owner = owner;
        this.items = items;
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
    public ArrayList<Item> getItems() {
        return items;
    }
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

}
