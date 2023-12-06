import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        db.initializeDatabase();

        User user1 = User.createUser(userType.CUSTOMER, "john123", "abc123!!", "random@gmail.com");
        User user2 = User.createUser(userType.SELLER, "ANDY", "51242", "random2@gmail.com");
        System.out.println(user1);
        System.out.println(db.getUser("john123"));

        System.out.println(user2);
        System.out.println(db.getUser("ANDY"));

        assert user1 != null;
        assert user2 != null;

        Message preSaveMessage = user1.sendmessage(user2, "Hello, how are you?");
        System.out.println(preSaveMessage);
        System.out.println(db.getMessage(String.valueOf(preSaveMessage.getUuid())));
        db.editMessage(preSaveMessage, "new message");
        System.out.println(db.getMessage(String.valueOf(preSaveMessage.getUuid())));
    
        //db.editUsername(user2, "john123");
        System.out.println("MAINTEST:" + user2);

        ArrayList<Item> items = new ArrayList();
        Item item = new Item(0, 5, "itemname");
        items.add(item);

        Store store = new Store("storename", user2, items);
        Store store2 = new Store("storename", user2, items);
        db.saveStore(store);
        db.saveStore(store2);
        db.saveItem(store2, item);
        db.restockItem(store, "itemname", 2);
        db.buyItem(store, "itemname", 56);
        System.out.println("TEST: " + db.readItemFromFile(store,"itemname"));
        System.out.println(db.readStoreFromFile("storename"));
    }
}
