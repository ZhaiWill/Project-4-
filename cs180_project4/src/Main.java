public class Main {
    public static void main(String[] args) {
        db.initializeDatabase();

        User user = User.createUser(userType.CUSTOMER, "john123", "abc123!!");
        User user2 = User.createUser(userType.SELLER, "ANDY", "51242");
        System.out.println(user);
        System.out.println(db.getUser("john123"));

        System.out.println(user2);
        System.out.println(db.getUser("ANDY"));

        assert user != null;
        assert user2 != null;
        Message message = new Message(user, user2, "Hello");
        System.out.println(message);
        Message message2 = new Message(user, user2, "Hello", message.getTimestamp());
        System.out.println(message2);
    }
}
