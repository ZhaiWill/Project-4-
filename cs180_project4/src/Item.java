import java.io.Serializable;
public class Item implements Serializable{
    double price;
    int quantity;
    String name;
    public Item(double price, int quantity, String name) {
        this.price = price;
        this.quantity = quantity;
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String toString() {
        return "Item<Price=" + price +
        ", quantity=" + quantity +
        ", name=" + name + ">";
    }
}