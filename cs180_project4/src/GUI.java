import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
public class GUI extends Main {
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    public User user;
    public Store store;
    private JTextField createMessageRecipient;
    public GUI() {
        frame = new JFrame("Messaging App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        JPanel sellerView = createNewSellerView();
        JPanel sendMessage = createSendMessage();
        JPanel manageAccount = createNewManageAccount();
        JPanel browseStores = browseStores();
        JPanel editUsername = createNewUsername();
        JPanel editPassword = createNewPassword();
        JPanel deleteAccount = newDeleteAccount();
        JPanel blockUser = newBlockUser();
        JPanel invisibleUser = newInvisibleUser();
        JPanel getAll = newGetAll();
        JPanel readAll = readAllMessages();
        JPanel manageMessages = createManageMessages();
        JPanel editMessage = createEditMessagePanel();
        JPanel deleteMessage = createDeleteMessage();
        JPanel manageStores = newManageStores();
        JPanel createInitMenu = createInitMenu();
        JPanel signInCard = createSignInCard();
        JPanel createAccountCard = createCreateAccountCard();
        JPanel buyerView = createNewBuyerView();
        JPanel addStore = createAddStore();
        JPanel editStore = createEditStore();
        JPanel deleteStore = createDeleteStore();

        cardPanel.add(sellerView, "SellerView");
        cardPanel.add(sendMessage, "SendMessage");
        cardPanel.add(manageAccount, "ManageAccount");
        cardPanel.add(editPassword, "EditPassword");
        cardPanel.add(readAll, "ReadAll");
        cardPanel.add(editUsername, "EditUsername");
        cardPanel.add(deleteAccount, "DeleteAccount");
        cardPanel.add(blockUser, "BlockUser");
        cardPanel.add(browseStores, "BrowseStores");
        cardPanel.add(manageStores, "ManageStores");
        cardPanel.add(invisibleUser, "InvisibleUser");
        cardPanel.add(getAll, "GetMessages");
        cardPanel.add(createInitMenu, "InitMenu");
        cardPanel.add(signInCard, "SignInCard");
        cardPanel.add(createAccountCard, "CreateAccountCard");
        cardPanel.add(buyerView, "BuyerView");
        cardPanel.add(editMessage, "EditMessage");
        cardPanel.add(manageMessages, "ManageMessages");
        cardPanel.add(deleteMessage, "DeleteMessage");
        cardPanel.add(addStore, "AddStore");
        cardPanel.add(editStore, "EditStore");
        cardPanel.add(deleteStore, "DeleteStore");
        cardPanel.add(manageStores, "ManageStores");
        frame.add(cardPanel);
        cardLayout.show(cardPanel, "InitMenu");
        frame.setVisible(true);

    }

    public JPanel createInitMenu() {
        JPanel newInitMenu = new JPanel();

        JButton signInButton = new JButton("Sign In");
        JButton createAccountButton = new JButton("Create Account");

        newInitMenu.add(createAccountButton);
        newInitMenu.add(signInButton);

        createAccountButton.setPreferredSize(new Dimension(350, 250));
        signInButton.setPreferredSize(new Dimension(350, 250));

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "SignInCard");
            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "CreateAccountCard");
            }
        });

        return newInitMenu;
    }

    public JPanel createSignInCard() {

        JPanel signInCard = new JPanel();
        signInCard.setLayout(new GridLayout(4, 2));
        JTextField signInUsernameField = new JTextField( 10);
        signInUsernameField.setText("Username: ");
        signInCard.add(signInUsernameField);

        JTextField signInPasswordField = new JTextField(10);
        signInPasswordField.setText("Password: ");
        signInCard.add(signInPasswordField);
        JButton signInAction = new JButton("Sign In");
        JButton backButton = new JButton("Back");
        signInAction.setPreferredSize(new Dimension(350, 250));
        signInAction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = signInUsernameField.getText();
                username = username.substring(username.indexOf(":") + 2, username.length());
                user = db.getUser(username);
                String password = signInPasswordField.getText();
                password = password.substring(password.indexOf(":") + 2, password.length());
                    if (user != null) {
                        if (user.getPassword().equals(password)) {
                            frame.setTitle(username);
                            if (user.isType() == userType.SELLER) {
                                cardLayout.show(cardPanel, "SellerView");
                            } else if (user.isType() == userType.CUSTOMER) {
                                cardLayout.show(cardPanel, "BuyerView");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Error, invalid password", null, JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Error, no user with username " + username + " found. Please try again", null, JOptionPane.INFORMATION_MESSAGE);
                    }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "InitMenu");
            }
        });

    signInCard.add(signInAction);
    signInCard.add(backButton);

    return signInCard;
}

    public JPanel createCreateAccountCard() {
        JPanel createAccountCard = new JPanel();
        createAccountCard.setLayout(new GridLayout(6, 1));

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "CreateAccountCard");
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "InitMenu");
            }
        });

        createAccountCard.add(createAccountButton);
        createAccountButton.setPreferredSize(new Dimension(350, 250));
        JTextField createAccountUsernameField = new JTextField("Username: " , 30);
        createAccountCard.add(createAccountUsernameField);

        JTextField createAccountPasswordField = new JTextField("Password: ", 30);
        createAccountCard.add(createAccountPasswordField);

        JTextField createAccountEmailField = new JTextField("Email: ", 30);
        createAccountCard.add(createAccountEmailField);

        JTextField createAccountQuestionField = new JTextField("Are you a Buyer or Seller? ", 30);
        createAccountCard.add(createAccountQuestionField);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = createAccountUsernameField.getText();
                newUsername = newUsername.substring(newUsername.indexOf(":") + 2, newUsername.length());
                String newPassword = createAccountPasswordField.getText();
                newPassword = newPassword.substring(newPassword.indexOf(":") + 2, newPassword.length());
                String email = createAccountEmailField.getText();
                email = email.substring(email.indexOf(":") + 2, email.length());
                String type = createAccountQuestionField.getText();
                type = type.substring(type.indexOf("?") + 2, type.length());
                if(db.getUser(newUsername) != null) {
                    JOptionPane.showMessageDialog(null, "Username already exists", null, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    frame.setTitle(newUsername);
                    if (createAccountQuestionField.getText().equalsIgnoreCase("seller")) {
                        user = User.createUser(userType.SELLER, newUsername, newPassword, email);
                        cardLayout.show(cardPanel, "SellerView");
                    } else if (createAccountQuestionField.getText().equalsIgnoreCase("buyer")) {
                        user = User.createUser(userType.CUSTOMER, newUsername, newPassword, email);
                        cardLayout.show(cardPanel, "BuyerView");
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid input", null, JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        createAccountCard.add(createAccountButton);
        createAccountCard.add(backButton);
        
        return createAccountCard;
    }

    private JPanel createNewSellerView() {
        JPanel createSellerView = new JPanel();
        createSellerView.setLayout(new GridLayout(8, 1));

        JButton createReadAll = new JButton("Read Messages");
        createReadAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ReadAll");
            }
        });
        createSellerView.add(createReadAll);
        JButton createSendMessage = new JButton("Send Message");
        createSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "SendMessage");
            }
        });
        createSellerView.add(createSendMessage);
        JButton createManageMessages = new JButton("Manage Messages");
        createManageMessages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ManageMessages");
            }
        });
        createSellerView.add(createManageMessages);
        JButton createManageAccount = new JButton("Manage Account");
        createManageAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ManageAccount");
            }
        });
        createSellerView.add(createManageAccount);
        JButton createManageStores = new JButton("Manage Stores");
        createManageStores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ManageStores");
            }
        });
        createSellerView.add(createManageStores);
        JButton createBlockUser = new JButton("Block User");
        createBlockUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "BlockUser");
            }
        });
        createSellerView.add(createBlockUser);
        JButton createBecomeInvisible = new JButton("Become Invisible");
        createBecomeInvisible.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "InvisibleUser");
            }
        });
        createSellerView.add(createBecomeInvisible);
        JButton logOut = new JButton("Log Out");
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "SignInCard");
            }
        });
        createSellerView.add(logOut);
        return createSellerView;
    }
    
    private JPanel readAllMessages() {
        JPanel getAll = new JPanel();
        getAll.setLayout(new GridLayout(5, 2));

        JLabel label = new JLabel("Which User's messages would you like to read?");
        JTextField createGetAllUser = new JTextField();
        JButton getAllButton = new JButton("Search for Messages");
        JButton searchAllButton = new JButton("Search for all users who have messaged you");
        JButton backButton = new JButton("Back");

        getAll.add(label);
        getAll.add(createGetAllUser);
        getAll.add(getAllButton);
        getAll.add(searchAllButton);
        getAll.add(backButton);
        searchAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> correspondents = db.getAllNames(user);
                if (correspondents.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No one has messaged you.", null, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "These people have messaged you: " + correspondents, null, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        getAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = createGetAllUser.getText();
                boolean found = false;

                ArrayList<User> correspondents = db.getAllConversations(user);

                for (User u : correspondents) {
                    if (found) break;
                    if (u.getUsername().equals(input)) {
                        found = true;

                        ArrayList<Message> messages = db.getConversation(user, u);

                        JPanel messagePanel = new JPanel();
                        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

                        for (Message m : messages) {
                            messagePanel.add(new JLabel("Sent by: " + m.getReceiver().getUsername() + ": " + m.getMessage()));
                        }

                        JOptionPane.showMessageDialog(null, messagePanel, "Messages", JOptionPane.PLAIN_MESSAGE);
                    
                    }
                }

                if (!found) {
                    JOptionPane.showMessageDialog(null, "Sorry, you don't have correspondence with that User yet.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user.isType().equals(userType.SELLER)) {
                    cardLayout.show(cardPanel, "SellerView");
                } else {
                    cardLayout.show(cardPanel, "BuyerView");
                }
            }
        });

        return getAll;
    }

     private JPanel createManageMessages() {
        JPanel manageMessages = new JPanel();
        manageMessages.setLayout(new GridLayout(3, 2));

        JButton editMessageButton = new JButton("Edit Message");
        editMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "EditMessage");
            }
        });
        manageMessages.add(editMessageButton);

        JButton deleteMessageButton = new JButton("Delete Message");
        deleteMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "DeleteMessage");
            }
        });
        manageMessages.add(deleteMessageButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user.isType().equals(userType.SELLER)) {
                    cardLayout.show(cardPanel, "SellerView");
                } else {
                    cardLayout.show(cardPanel, "BuyerView");
                }
            }
        });
        manageMessages.add(backButton);

        return manageMessages;
    }
    
    private JPanel browseStores() {
        JPanel browseStores = new JPanel();
        
        browseStores.setLayout(new GridLayout(4, 2));
        browseStores.add(new JLabel("Enter Message Recipient From Stores"));
        JTextField createMessageRecipient = new JTextField();
        browseStores.add(createMessageRecipient);
        browseStores.add(new JLabel("Enter Message"));
        JTextField createMessageInfo = new JTextField();
        browseStores.add(createMessageInfo);
        JButton backButton = new JButton("Back");
        JButton viewStores = new JButton("View Stores to Message Owners");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "BuyerView");
            }
        });
        browseStores.add(backButton);
        JButton sendMessageButton = new JButton("Send Message");
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String recipient = createMessageRecipient.getText();
                String content = createMessageInfo.getText();
                Main.sendMessage(recipient, user, content);
            }
        });

        viewStores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Store> stores = db.getAllStores();
                JPanel messagePanel = new JPanel();
                messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
                if (stores.isEmpty()) {
                    messagePanel.add(new JLabel("No Available Stores"));
                } else {
                for (Store s : stores) {
                    messagePanel.add(new JLabel("Store owner: " + s.getOwner().getUsername() + " | Store name: " + s.getName()));
                    }
                }
                JOptionPane.showMessageDialog(null, messagePanel, "Stores", JOptionPane.PLAIN_MESSAGE);
            }
        });
        
        browseStores.add(viewStores);
        browseStores.add(sendMessageButton);
        
        return browseStores;
    }


    private JPanel createDeleteMessage() {
        JPanel deleteMessage = new JPanel();

        

        return deleteMessage;
    }

    private JPanel createEditMessagePanel() {

        JPanel editMessagePanel = new JPanel();
        
        return editMessagePanel;
    }

    public JPanel createSendMessage() {
        JPanel createMessageCard = new JPanel();
        createMessageCard.setLayout(new GridLayout(3, 2));
        createMessageCard.add(new JLabel("Enter Message Recipient"));
        createMessageRecipient = new JTextField();
        createMessageCard.add(createMessageRecipient);
        createMessageCard.add(new JLabel("Enter Message"));
        JTextField createMessageInfo = new JTextField();
        createMessageCard.add(createMessageInfo);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user.isType().equals(userType.SELLER)) {
                    cardLayout.show(cardPanel, "SellerView");
                } else {
                    cardLayout.show(cardPanel, "BuyerView");
                }
            }
        });
        createMessageCard.add(backButton);
        JButton sendMessageButton = new JButton("Send Message");
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String recipient = createMessageRecipient.getText();
                String content = createMessageInfo.getText();
                Main.sendMessage(recipient, user, content);

            }
        });
        createMessageCard.add(sendMessageButton);
        return createMessageCard;
    }

    private JPanel createNewManageAccount() {
        JPanel createManageAccount = new JPanel();
        createManageAccount.setLayout(new GridLayout(4, 1));
        JButton editUsername = new JButton("Edit Username");
        editUsername.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "EditUsername");
            }
        });
        createManageAccount.add(editUsername);
        JButton editPassword = new JButton("Edit Password");
        editPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "EditPassword");
            }
        });
        createManageAccount.add(editPassword);
        JButton deleteAccount = new JButton("Delete Account");
        deleteAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "DeleteAccount");
            }
        });
        createManageAccount.add(deleteAccount);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (user.isType().equals(userType.SELLER)) {
                    cardLayout.show(cardPanel, "SellerView");
                } else {
                    cardLayout.show(cardPanel, "BuyerView");
                }
            }
        });
        createManageAccount.add(backButton);

        return createManageAccount;
    }

    private JPanel newDeleteAccount() {
        JPanel deleteAccount = new JPanel();
        deleteAccount.setLayout(new GridLayout(3, 2));
        deleteAccount.add(new JLabel("Are you sure you want to delete your account?"));
        deleteAccount.add(new JLabel());
        JButton yesButton = new JButton("Yes");
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.deleteUser(user);
                cardLayout.show(cardPanel, "SignInCard");
            }
        });
        deleteAccount.add(yesButton);
        JButton noButton = new JButton("No");
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user.isType() == userType.SELLER) {
                    cardLayout.show(cardPanel, "SellerView");
                } else if (user.isType() == userType.CUSTOMER) {
                    cardLayout.show(cardPanel, "BuyerView");
                }
            }
        });
        deleteAccount.add(noButton);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ManageAccount");
            }
        });
        deleteAccount.add(noButton);

        return deleteAccount;
    }

    private JPanel createNewUsername() {
        JPanel createUsername = new JPanel();
        createUsername.setLayout(new GridLayout(2, 2));
        createUsername.add(new JLabel("Enter New Username"));
        JTextField createNewUsernameField = new JTextField();
        createUsername.add(createNewUsernameField);
        JButton usernameButton = new JButton("Change Username");
        usernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = createNewUsernameField.getText();
                if(db.editUsername(user, username) == true) {
                    JOptionPane.showMessageDialog(null, "Username has been changed succesfully", null, JOptionPane.INFORMATION_MESSAGE);
                } else if(username == null) {
                    JOptionPane.showMessageDialog(null, "Invalid Username", null, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Username already exists", null, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        createUsername.add(usernameButton);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ManageAccount");
            }
        });
        createUsername.add(backButton);

        return createUsername;
    }

    private JPanel createNewPassword() {
        JPanel createPassword = new JPanel();
        createPassword.setLayout(new GridLayout(2, 2));
        createPassword.add(new JLabel("Enter New Password"));
        JTextField createNewPasswordField = new JTextField();
        createPassword.add(createNewPasswordField);
        JButton passwordButton = new JButton("Change Password");
        passwordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPassword = createNewPasswordField.getText();
                if (!newPassword.isEmpty()) {
                    user.setPassword(newPassword);
                    db.saveUser(user);
                    JOptionPane.showMessageDialog(null, "Password has been changed succesfully", null, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a new password", null, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        createPassword.add(passwordButton);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ManageAccount");
            }
        });
        createPassword.add(backButton);

        return createPassword;
    }

    private JPanel newBlockUser() {
        JPanel blockUsers = new JPanel();
        blockUsers.setLayout(new GridLayout(3, 2));
        blockUsers.add(new JLabel("Enter the username you would like to block"));
        JTextField createNewBlockUser = new JTextField();
        blockUsers.add(createNewBlockUser);
        JButton blockButton = new JButton("Block Person");
        blockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newuser = createNewBlockUser.getText();
                user.blockUser(db.getUser(newuser));
                db.saveUser(user);
                JOptionPane.showMessageDialog(null, createNewBlockUser.getText() + " Has Been Successfully Blocked", "Greetings", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        blockUsers.add(blockButton);
        JButton unblockButton = new JButton("Unblock Person");
        unblockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUser = createNewBlockUser.getText();
                User newUser2 = db.getUser(newUser);
                List<String> blockedUsers1 = newUser2.getBlockedUsers();
                if (blockedUsers1.contains(user.getUsername())) {
                    JOptionPane.showMessageDialog(null, " You cannot unblock " + newUser, "Greetings", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    user.unblockUser(db.getUser(newUser));
                    db.saveUser(user);
                    JOptionPane.showMessageDialog(null, createNewBlockUser.getText() + " Has Been Successfully Unblocked", "Greetings", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        blockUsers.add(unblockButton);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user.isType().equals(userType.SELLER)) {
                    cardLayout.show(cardPanel, "SellerView");
                } else {
                    cardLayout.show(cardPanel, "BuyerView");
                }
            }
        });
        blockUsers.add(backButton);

        return blockUsers;
    }

    private JPanel newInvisibleUser() {
        JPanel invisibleUser = new JPanel();
        invisibleUser.setLayout(new GridLayout(3,2));
        invisibleUser.add(new JLabel("Enter the username you would like to be invisble to"));
        JTextField createNewInvisibleUser = new JTextField();
        invisibleUser.add(createNewInvisibleUser);
        JButton invisibleButton = new JButton("Become Invisible");
        invisibleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newuser = createNewInvisibleUser.getText();
                user.becomeInvisible(db.getUser(newuser));
                db.saveUser(user);
                JOptionPane.showMessageDialog(null, "You have become invisible", null, JOptionPane.INFORMATION_MESSAGE);
            }
        });
        invisibleUser.add(invisibleButton);
        JButton visibleButton = new JButton("Become Visible");
        visibleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newuser = createNewInvisibleUser.getText();
                user.becomeUninvisible(db.getUser(newuser));
                db.saveUser(user);
                JOptionPane.showMessageDialog(null, "You have become visible", null , JOptionPane.INFORMATION_MESSAGE);
            }
        });
        invisibleUser.add(visibleButton);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user.isType().equals(userType.SELLER)) {
                    cardLayout.show(cardPanel, "SellerView");
                } else {
                    cardLayout.show(cardPanel, "BuyerView");
                }
            }
        });
        invisibleUser.add(backButton);

        return invisibleUser;
    }

    private JPanel newGetAll() {
        JPanel getAll = new JPanel();
        getAll.setLayout(new GridLayout(2, 2));
        getAll.add(new JLabel("Which User's messages would you like to read?"));
        JTextField createGetAllUser = new JTextField();
        getAll.add(createGetAllUser);
        JButton getAllButton = new JButton("Search for Messages");
        getAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "You have become invisible", "Greetings", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        getAll.add(getAllButton);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user.isType() == userType.SELLER) {
                    cardLayout.show(cardPanel, "SellerView");
                } else if (user.isType() == userType.CUSTOMER) {
                    cardLayout.show(cardPanel, "BuyerView");
                }
            }
        });
        getAll.add(backButton);

        return getAll;
    }

    public JPanel newManageStores() {
        JPanel manageStores = new JPanel();
        manageStores.setLayout(new GridLayout(4, 1));
        JButton addButton = new JButton("Add Store");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "AddStore");
            }
        });
        manageStores.add(addButton);
        JButton editButton = new JButton("Edit Store");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "EditStore");
            }
        });
        manageStores.add(editButton);
        JButton deleteButton = new JButton("Delete Store");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                    cardLayout.show(cardPanel, "DeleteStore");
                }
        });
        manageStores.add(deleteButton);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user.isType() == userType.SELLER) {
                    cardLayout.show(cardPanel, "SellerView");
                } else if (user.isType() == userType.CUSTOMER) {
                    cardLayout.show(cardPanel, "BuyerView");
                }
            }
        });
        manageStores.add(backButton);

        return manageStores;
    }
    public JPanel createAddStore() {
        JPanel createStore = new JPanel();
        createStore.setLayout(new GridLayout(2, 2));
        createStore.add(new JLabel("Enter New Store"));
        JTextField createNewStoreField = new JTextField();
        createStore.add(createNewStoreField);
        JButton storeButton = new JButton("Create New Store");
        storeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String store1 = createNewStoreField.getText();
                if (store1.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Store name cannot be empty", null, JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                store = Store.createStore(store1, user);
                output.debugPrint(store.toString());
                if(db.saveStore(store) != null) {
                    JOptionPane.showMessageDialog(null, "Store added successfully", null, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Could not add store", null, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        createStore.add(storeButton);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ManageStores");
            }
        });
        createStore.add(backButton);
        return createStore;
    }
    public JPanel createEditStore() {
        JPanel createNewStore = new JPanel();
        createNewStore.setLayout(new GridLayout(3, 2));
        createNewStore.add(new JLabel("Enter the name of the store you would like to change:"));
        JTextField createOldStoreField = new JTextField();
        createNewStore.add(createOldStoreField);
        createNewStore.add(new JLabel("Enter the new name for the store:"));
        JTextField createNewStoreField = new JTextField();
        createNewStore.add(createNewStoreField);
        JButton storeButton = new JButton("Change Store Name");
        storeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String store3 = createOldStoreField.getText();
                String store2 = createNewStoreField.getText();
                if (db.getStore(store3) == null) {
                    JOptionPane.showMessageDialog(null, "There is no store named " + store3, null, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    if (db.editStore(store2, store3) == true) {
                        JOptionPane.showMessageDialog(null, "Store successfully changed.", null, JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Store cannot be changed.", null, JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        createNewStore.add(storeButton);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ManageStores");
            }
        });
        createNewStore.add(backButton);
        return createNewStore;
    }
    public JPanel createDeleteStore() {
        JPanel deleteStore = new JPanel();
        deleteStore.setLayout(new GridLayout(2, 2));
        deleteStore.add(new JLabel("Enter the name of the store you would like to delete:"));
        JTextField createNewStoreField = new JTextField();
        deleteStore.add(createNewStoreField);
        JButton deletebutton = new JButton("Delete Store");
        deletebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String store2 = createNewStoreField.getText();
                if(db.removeStore(store2) == true) {
                    JOptionPane.showMessageDialog(null, "Store successfully deleted.", null, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Store could not be deleted, try again.", null, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        deleteStore.add(deletebutton);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ManageStores");
            }
        });
        deleteStore.add(backButton);
        return deleteStore;
    }
    public JPanel createNewBuyerView() {
        JPanel createBuyerView = new JPanel();
        createBuyerView.setLayout(new GridLayout(8, 1));

        JButton createReadAll = new JButton("Read Messages");
        createReadAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ReadAll");
            }
        });
        createBuyerView.add(createReadAll);
        JButton createSendMessage = new JButton("Send Message");
        createSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "SendMessage");
            }
        });
        createBuyerView.add(createSendMessage);
        JButton createManageMessages = new JButton("Manage Messages");
        createManageMessages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ManageMessages");
            }
        });
        createBuyerView.add(createManageMessages);
        JButton createManageAccount = new JButton("Manage Account");
        createManageAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ManageAccount");
            }
        });
        createBuyerView.add(createManageAccount);
        JButton createBrowseStores = new JButton("Browse Stores");
        createBrowseStores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "BrowseStores");
            }
        });
        createBuyerView.add(createBrowseStores);
        JButton createBlockUser = new JButton("Block User");
        createBlockUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "BlockUser");
            }
        });
        createBuyerView.add(createBlockUser);
        JButton createBecomeInvisible = new JButton("Become Invisible");
        createBecomeInvisible.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "InvisibleUser");
            }
        });
        createBuyerView.add(createBecomeInvisible);
        JButton logOut = new JButton("Log Out");
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "SignInCard");
            }
        });
        createBuyerView.add(logOut);
        return createBuyerView;
    }
}
