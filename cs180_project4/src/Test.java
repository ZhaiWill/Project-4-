import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        //TEST CASES FOR METHODS ARE HERE
        db.initializeDatabase();
        User customer1 = User.createUser(userType.CUSTOMER, "customer1", "123", "customer1email@gmail.com");
        User customer2 = User.createUser(userType.CUSTOMER, "customer2", "123", "customer2email@gmail.com");
        User customer3 = User.createUser(userType.CUSTOMER, "customer2", "123", "customer3email@gmail.com");

        User seller1 = User.createUser(userType.CUSTOMER, "seller1", "123", "seller1email@gmail.com");
        User seller2 = User.createUser(userType.CUSTOMER, "seller2", "123", "seller2email@gmail.com");
        User seller3 = User.createUser(userType.CUSTOMER, "seller3", "123", "seller3email@gmail.com");

        System.out.println(User.createUser(userType.CUSTOMER, "customer1", "wtv", "wtv") == null);
        System.out.println(User.createUser(userType.SELLER, "customer1", "wtv", "wtv") == null);
        customer1.setUserBlockStatus("seller2", userBlockStatus.BLOCKED);
        db.saveUser(customer1);
        System.out.println(db.getUser("customer1").userBlockStatusMap.get("seller2") == userBlockStatus.BLOCKED);

    }
}
