import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
       db.initializeDatabase();
       User bob = User.createUser(userType.CUSTOMER, "bob", "123", "1@gmai.com");
       User joe = User.createUser(userType.SELLER, "joe", "123", "2@gmai.com");
       User andy = User.createUser(userType.SELLER, "andy", "123", "2@gmai.com");
       assert bob != null;
       assert andy != null;
       assert joe != null;
       bob.sendmessage(joe, "hi");
       //wait .1 milisecond
       try {
           Thread.sleep(100);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
       joe.sendmessage(bob, "hello");
       try {
           Thread.sleep(100);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
       bob.sendmessage(joe, "how are you");
       try {
           Thread.sleep(100);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
       bob.setUserBlockStatus("andy", userBlockStatus.BLOCKED);
       bob.setUserBlockStatus("joe", userBlockStatus.INVISIBLE);
       db.saveUser(bob);
       System.out.println(bob.userBlockStatusMap);
       System.out.println(db.getUser("bob").userBlockStatusMap);
       db.getUser("andy");
                Store s = new Store("store1", "joe");
                db.saveStore(s);

        new UI().run();
    }
}
