# Project-4-Messaging System
Project 4 - William, Farhan, Kyle, Elijah
Option 2 - Messaging System

1. Instructions on how to compile and run our project. 

Select the .java file called "Main." Run the main method to start the program.

2. List of who submitted each part on Vocareum/Brightspace.

Submitted report on Brightspace - Elijah

Submitted Vocareum workspace. - Farhan

3. Descriptions of each class.

User.java: The user class contains information about every user who has created an account. This information includes the username, password, and whether they are a customer or seller. This also interacts with the "DBClient" class with the "createUser" method. If a user with a given username already exists, that user will not be saved to the file system. There are also associated getters and setters used to retrieve and set properties.

Store.java: Contains information about a store including its owner and its name. There are also associated getters and setters used to retrieve and set properties.

Message.java: This class contains describes a "message" object with a sender, receiver, message content, timestamp, and UUID. The UUID is a unique string of bytes and works as a way of differentiating between messages, making it much easier to pull specific messages from storage. There are also associated getters and setters used to retrieve and set properties.

DBClient.java: This method handles saving data to and pulling data from the root directory called "storage." This class contains methods for initializing the database, clearing the database, and creating/deleting directories. When initializing the database, the entirety of the database is deleted before the directories storing users, messages, and stores are created (without any of their prior data). There are also methods for saving users, messages, and store information in their respective directories. There are also methods in place to pull a specific message from the database based on its ID, a user based on their username, or a store based on its name. In addition, methods for editing a username or password exist in this class. This class interacts with Main by providing it with most of the methods for saving data based on user input in addition to the User, Store, and Message classes because it has to save these objects to storage.

Main: This displays the UI so that users can easily create or log in to accounts, send/remove/edit messages, change account details, and interact with stores. This is all done through a variety of print statements and scanner inputs. As previously stated, the bulk of the work is done by the "DBClient" class because it is responsible for saving all data to the database. 

Test.java: This is simply a class with a main method that is used to test methods to verify that they work properly. Test cases that we used are here. 

Output.java: This is a class that is used to print debugging statements to verify that each method is functioning as intended. The boolean "debugMode" should be set to false so that the text used for debugging is not printed to the console by submission time. 

DBServer.java: This class is a server which only has one method, the HandleClient method. Using it, you can make a db clear or initialize its database, as well as interact with users by saving a user, getting a user, deleting a user, or getting all users. The class can also edit or delete both users and messages, and pull all conversations. Lastly, it can get all stores, remove stores and save stores. 

UI.java: The UI is what the user will see and interact with. It communicates with the user through JOptionPanes and will ask the user to input and commit actions. It will ask either for Strings or Integers and contains a user menu, welcome screen, a menu for creating a new users, and can show messages. 
