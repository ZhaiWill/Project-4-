Test 1: User log in
Steps:
1. User launches application.
2. User selects the username textbox.
3. User enters username via the keyboard.
4. User selects the password textbox.
5. User selects the "Log in" button. 

Expected result: Application verifies the user's username and password and loads their homepage automatically. 

Test Status: Passed. 

Test 2: User sign up
Steps:
1. User launches application.
2. User selects the username textbox and enters username via the keyboard.
3. User selects the password textbox and enters a password.
4. User enters an email.
5. User selects if they are a buyer or seller.
6. User selects the "Create Account" button. 

Expected result: Application saves a new user and the home page is loaded.

Test Status: Passed. 

Test 3: Password verification
Steps:
1. User launches application.
2. User selects "Sign in."
3. User enters a username.
4. User enters a password that is not associated with that given username.
5. User selects the "Sign in" button.

Expected result: Application creates an error window and the user is not allowed to access the account

Test Status: Passed.

Test 4: Messaging
Steps:
1. User logs in.
2. User selects "Send Message" option.
3. User sends another user a message.
4. User logs out.
5. User logs in under different user.
6. User selects "Read message" option.
7. User enters other user's name.

Expected result: User is able to see message from other user.

Test status: Passed.
Steps:
Test 5: Editing Account detials
1. User signs in.
2. User selects "Manage Account."
3. User selects "Change Username."
4. User enters new username and presses "Change Username."

Expected result: The username of the user will change (they will not be able to re-sign in on the original username)

Test status: Passed.

Test 6: Editing store details
Steps:
1. User signs in.
2. User selects "Manage Stores."
3. User selects "Edit Store."
4. User enters new store name and hits "Edit Store."

Expected Result: Sellers should be able to see store under a different name.

Test status: Passed.

Test 7: Blocking Users
Steps:
1. User signs in.
2. User navigates to "Block User."
3. User enters another user's name and hits "Block User."
4. User navigates back to main menu and selects "Send Message"
5. User enters message and attempts to send it to blocked user.

Expected result: System does not allow for the message to be sent.

Test status: passed

Test 8: Concurrency
Steps:
1. User opens two separate windows of the program.
2. User signs into two different accounts.
3. On one account, the user navigates to the "send mesasage" button and sends a message.
4. On the other account, the user navigates to "Read Message" button.
5. User enters the username of the other user who messaged them.

Expected result: User is able to see the message contents sent from other user.

Test status: Passed

Test 9: Viewing stores
Steps:
1. User signs into an account as a "buyer."
2. User navigates to main menu.
3. User selects the option to view stores.
4. User selects the store they want to view.

Expected Result: User should be prompted to send a message to the store owner.

Test status: Passed

Test 10: Editing Message Details
Steps:
1. User signs in and navigates to "Manage Messages"
2. User selected "Edit Message"
3. User selects the message they want to edit.
4. User enters the new message content and confirms the message edit.

Expected Result: User should be able to see the updated message when viewing messages.

Test status: Passed
