//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class Test {
//    public static void main(String[] args) {
//        //TEST CASES FOR METHODS ARE HERE
//        db.initializeDatabase();
//        User customer1 = User.createUser(userType.CUSTOMER, "customer1", "123", "customer1email@gmail.com");
//
//        User bob =
//        User joe = User.createUser(userType.SELLER, "joe", "123", "2@gmai.com");
//        User andy = User.createUser(userType.SELLER, "andy", "123", "2@gmai.com");
//        User user1 = User.createUser(userType.CUSTOMER, "john123", "abc123!!", "random@gmail.com");
//        User user2 = User.createUser(userType.SELLER, "MANDY", "51242", "random2@gmail.com");
//
//
//
//        System.out.println(user1);
//        System.out.println(db.getUser("john123"));
//
//        System.out.println(user2);
//        System.out.println(db.getUser("MANDY"));
//
//        assert user1 != null;
//        assert user2 != null;
//
//        bob.setUserBlockStatus("andy", userBlockStatus.BLOCKED);
//        bob.setUserBlockStatus("joe", userBlockStatus.INVISIBLE);
//
//        db.saveUser(bob);
//        System.out.println(bob.userBlockStatusMap);
//        System.out.println(db.getUser("bob").userBlockStatusMap);
//        db.getUser("andy");
//                 Store s = new Store("store1", "joe");
//                 db.saveStore(s);
//
//    }
//}
