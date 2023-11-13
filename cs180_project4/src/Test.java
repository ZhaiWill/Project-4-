import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        //TEST CASES FOR METHODS ARE HERE
        db.initializeDatabase();
        User bob = User.createUser(userType.CUSTOMER, "bob", "123", "1@gmai.com");
        User joe = User.createUser(userType.SELLER, "joe", "123", "2@gmai.com");
        User andy = User.createUser(userType.SELLER, "andy", "123", "2@gmai.com");
        User user1 = User.createUser(userType.CUSTOMER, "john123", "abc123!!", "random@gmail.com");
        User user2 = User.createUser(userType.SELLER, "MANDY", "51242", "random2@gmail.com");
        System.out.println(user1);
        System.out.println(db.getUser("john123"));

        System.out.println(user2);
        System.out.println(db.getUser("MANDY"));

        assert user1 != null;
        assert user2 != null;

        bob.setUserBlockStatus("andy", userBlockStatus.BLOCKED);
        bob.setUserBlockStatus("joe", userBlockStatus.INVISIBLE);
        
        db.saveUser(bob);
        System.out.println(bob.userBlockStatusMap);
        System.out.println(db.getUser("bob").userBlockStatusMap);
        db.getUser("andy");
                 Store s = new Store("store1", "joe");
                 db.saveStore(s);
        new UI().run();
        /*EXCPECTED: MANDY and john123 should be printed twice with the exact same info. Bob should not be able to send messages to andy and vice versa. 
         * Joe should be completely invisible to Bob (i.e. messages cannot be sent to Joe from bob and vise versa, bob should not be able to search for joe)
         * All messages should persist and only be accessable by the sender and recipient.  
         * A user should be able to sign into any existing account or create a new account. Any changes made to this account should be saved permanently. 
         * Any customers (bob, john123) should not be able to create stores and should be able to view them while all sellers (Joe, MANDY, andy) should be able to create new stores
         * store1 should be visible
         */
    }
}
