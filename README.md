# Project 5: Messaging System (with concurrency, network IO, and GUI)
Project 5 - William, Farhan, Kyle, Elijah
Option 2 - Messaging System

1. Instructions on how to compile and run our project. 

Select the .java file called "Main." Run the main method to start the program.

2. List of who submitted each part on Vocareum/Brightspace.

Submitted report on Brightspace - Elijah

Submitted video/presentation on Brightspace - William

Submitted Vocareum workspace. - Farhan

3. Descriptions of each class.

User.java: The user class contains information about every user who has created an account. This information includes the username, password, and whether they are a customer or seller. This also interacts with the "db" class with the "createUser" method. If a user with a given username already exists, that user will not be saved to the file system. There are also associated getters and setters used to retrieve and set properties.

Store.java: Contains information about a store including its owner and its name. There are also associated getters and setters used to retrieve and set properties.

Message.java: This class contains describes a "message" object with a sender, receiver, message content, timestamp, and UUID. The UUID is a unique string of bytes and works as a way of differentiating between messages, making it much easier to pull specific messages from storage. There are also associated getters and setters used to retrieve and set properties.

db.java: This method handles saving data to and pulling data from the root directory called "storage." This class contains methods for initializing the database, clearing the database, and creating/deleting directories. When initializing the database, the entirety of the database is deleted before the directories storing users, messages, and stores are created (without any of their prior data). There are also methods for saving users, messages, and store information in their respective directories. There are also methods in place to pull a specific message from the database based on its ID, a user based on their username, or a store based on its name. In addition, methods for editing a username or password exist in this class. This class interacts with Main by providing it with most of the methods for saving data based on user input in addition to the User, Store, and Message classes because it has to save these objects to storage.

Main: This simply runs the code from the "GUI" class, displaying the GUI. 

GUI.java: This contains the code that displays a variety of different menus to the user. Each panel is slightly different in its functionality for the overall program, but all of them display prompts to the user and text boxes to input information to be processed by the program. This class heavily interacts with the "db" class in order to store and retrieve data. 

Test.java: This is simply a class with a main method that is used to test methods to verify that they work properly. 

Output.java: This is a class that is used to print debugging statements to verify that each method is functioning as intended. The boolean "debugMode" should be set to false so that the text used for debugging is not printed to the console by submission time. 

status.java: This class contains the different enums used to distinguish if a user in invisible or not to another user. Used in GUI class.

userType.java: Contains enums to distinguish if someone is a customer or seller. Used in GUI class.  
