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

        db.editMessage(preSaveMessage, "hello i edited you");
        System.out.println(db.getMessage(String.valueOf(preSaveMessage.getUuid())));
    
        db.removeMessage(user2,preSaveMessage);

        db.editMessage(preSaveMessage, "hello i TOO edited you");
        db.editUsername(user2, "john123");
        System.out.println(user2);

        Store store = new Store("name", user2, null);
        Item item = new Item(0, 0, "name");
        db.saveStore(store);
        db.saveItem(store,item);
    }
}
