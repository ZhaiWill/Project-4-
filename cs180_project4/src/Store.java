import java.util.ArrayList;
public class Store {
    String name;
    User owner;
    ArrayList<User> customers;
    ArrayList<Item> items;
    
    public Store(String name, User owner, ArrayList<Item> items, ArrayList<User> customers) {
        this.name = name;
        this.owner = owner;
        this.items = items;
        this.customers = customers;
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

    public ArrayList<User> getCustomers() {
        return customers;
    }
    
    public void addCustomer(User u) {
        if (u.isType() == userType.CUSTOMER) {
            this.customers.add(u);
        }
        else {
            System.out.println("Sellers can't be customers!");
        }
    }


}
