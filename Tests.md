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
2. User selects the username textbox and enters username via the keyboard..
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
6. User selects "Read message" option
7. User enters other user's name

Expected result: User is able to see message from other user.

Test status: Passed.

Test 5: Editing Account detials
1. User signs in
2. User selects "Manage Account"
3. User selects "Change Username"
4. User enters new username and presses "Change Username"

Expected result: The username of the user will change (they will not be able to re-sign in on the original username)

Test status: Passed.

Test 6: Editing store details
1. User signs in
2. User selects "Manage Stores"
3. User selects "Edit Store"
4. User enters new store name and hits "Edit Store"

Expected Result: Sellers should be able to see store under a different name.

Test status: Passed.