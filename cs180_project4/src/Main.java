public class Main {
    public static void main(String[] args){
        db.initializeDatabase();

        User user = User.createUser(userType.CUSTOMER, "john123","abc123!!");
        if(user == null){

            return;
        }
        System.out.println(user);
        db.saveUser(user);
        System.out.println(db.getUser("john123"));
    }
}
