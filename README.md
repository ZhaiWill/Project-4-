# Project-4-Messaging System
Project 4 - William, Farhan, Kyle, Elijah
Option 2 - Messaging System

1. Instructions on how to compile and run our project. 
Select the .java file called "Main." Run the main method in order to start the program.

2. List of who submitted each part on Vocareum/Brightspace.

TBD (We need to start on the report)

3. Descriptions of each class.

User.java: The user class contains information about every user who has created an account. This information includes ther username, password, and whether they are a customer or seller. This also indetacts with the "db" class with the "createUser" method. If a user with a given username already exists, that user will not be saved to the file system. There are also associated getters and setters used to retreive and set properties.

Item.java: Describes an item of interest that can be sold in stores. Each item has a set quantity, price, and name associated with it. There are also associated getters and setters used to retreive and set properties.

Store.java: Contains information about a store including an array of items that can be sold at the store, its owner, and its name. There are also associated getters and setters used to retreive and set properties.

Message.java: This class contains describes a "message" object with a sender, receiver, the message content, a timestamp, and a UUID. The UUID is a unique string of bytes and works as a way of differentiating between messages, making it much easier to pull specific messages from storage. There are also associated getters and setters used to retreive and set properties.

db.java: This method handles saving data to and pulling data from the root directory called "storage." This class contains methods for initializing the database, clearing the database, and creating/deleting directories. When initializing the database, the entirety of the database is deleted before the directories storing users, messages, and stores are created (without any of their prior data). There are also methods for saving users, messages, store information, and items to their respective directories.There are also methods in place to pull a specific message from the databse based on its ID, a user based on their username, or a store/item based on its name. In addition, methods for editing a usrename or pwassword and buying or restocking items exist in this class. This class interacts with Main by providing it with most of methods for saving data based on user input in addition to the User, Item, Store, and Message classes because it has to save these objects to storage.

Main: This diisplays the UI so that users can easily create or login to accoutns, send/removee/edit messages, change account details, and interact with stores. This is all done through a variety of print statements and scanner inputs. As previously stated, the bulk of the work is done by "db" class ue to the fact that it is responsible for saving all data to the database. 

Test.java: This is simply a class with a main method that is used to test methods in order to verify that they work properly.

Output.java: This is a class that is used to print debugging statements to verify that each method is functioning as intended. The boolean "debugMode" should be set to false so that text used for debugging is not printed to the console by submission time. 
