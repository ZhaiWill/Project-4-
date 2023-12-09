import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI extends Main {
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    public User user;
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
        JPanel editUsername = createNewUsername();
        JPanel editPassword = createNewPassword();
        JPanel deleteAccount = newDeleteAccount();
        JPanel blockUser = newBlockUser();
        JPanel invisibleUser = newInvisibleUser();
        JPanel getAll = newGetAll();
        JPanel manageStores = newManageStores();
        JPanel createInitMenu = createInitMenu();
        JPanel signInCard = createSignInCard();
        JPanel createAccountCard = createCreateAccountCard();
        JPanel buyerView = createNewBuyerView();

        cardPanel.add(sellerView, "SellerView");
        cardPanel.add(sendMessage, "SendMessage");
        cardPanel.add(manageAccount, "ManageAccount");
        cardPanel.add(editPassword, "EditPassword");
        cardPanel.add(editUsername, "EditUsername");
        cardPanel.add(deleteAccount, "DeleteAccount");
        cardPanel.add(blockUser, "BlockUser");
        cardPanel.add(manageStores, "ManageStores");
        cardPanel.add(invisibleUser, "InvisibleUser");
        cardPanel.add(getAll, "GetMessages");
        cardPanel.add(createInitMenu, "InitMenu");
        cardPanel.add(signInCard, "SignInCard");
        cardPanel.add(createAccountCard, "CreateAccountCard");
        cardPanel.add(buyerView, "BuyerView");

        frame.add(cardPanel);
        cardLayout.show(cardPanel, "InitMenu");
        frame.setVisible(true);

    }

    private JPanel createNewBuyerView() {
        JPanel createBuyerView = new JPanel();
        createBuyerView.setLayout(new GridLayout(8, 1));

        JButton createReadAll = new JButton("Read All Messages");
        createReadAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "GetMessages");
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
                cardLayout.show(cardPanel, "SendMessage");
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
        JButton createManageMessages = new JButton("Manage Messages");
        createManageMessages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "SendMessage");
            }
        });
        createBuyerView.add(createManageMessages);
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
        signInCard.setLayout(new GridLayout(5, 2));
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
                            cardLayout.show(cardPanel, "SellerView");
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
                    if (type.equalsIgnoreCase("seller")) {
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

        JButton createReadAll = new JButton("Read All Messages");
        createReadAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ReadMessages");
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
        JButton createManageMessages = new JButton("Manage Messages");
        createManageMessages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "SendMessage");
            }
        });
        createSellerView.add(createManageMessages);
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

    private JPanel createSendMessage() {
        JPanel createMessageCard = new JPanel();
        createMessageCard.setLayout(new GridLayout(3, 2));
        createMessageCard.add(new JLabel("Enter Message Recipient"));
        JTextField createMessageRecipient = new JTextField();
        createMessageCard.add(createMessageRecipient);
        createMessageCard.add(new JLabel("Enter Message"));
        JTextField createMessageInfo = new JTextField();
        createMessageCard.add(createMessageInfo);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "SellerView");
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
                cardLayout.show(cardPanel, "SellerView");
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
                cardLayout.show(cardPanel, "SellerView");
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
        deleteAccount.add(backButton);

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
        blockUsers.setLayout(new GridLayout(2, 2));
        blockUsers.add(new JLabel("Enter the username you would like to block"));
        JTextField createNewBlockUser = new JTextField();
        blockUsers.add(createNewBlockUser);
        JButton blockButton = new JButton("Block Person");
        blockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newuser = createNewBlockUser.getText();
                user.blockUser(db.getUser(newuser));
                JOptionPane.showMessageDialog(null, createNewBlockUser.getText() + " Has Been Successfully Blocked", "Greetings", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        blockUsers.add(blockButton);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "SellerView");
            }
        });
        blockUsers.add(backButton);

        return blockUsers;
    }

    private JPanel newInvisibleUser() {
        JPanel invisibleUser = new JPanel();
        invisibleUser.setLayout(new GridLayout(2, 2));
        invisibleUser.add(new JLabel("Enter the username you would like to be invisble to"));
        JTextField createNewInvisibleUser = new JTextField();
        invisibleUser.add(createNewInvisibleUser);
        JButton invisibleButton = new JButton("Become Invisible");
        invisibleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newuser = createNewInvisibleUser.getText();
                user.becomeInvisible(db.getUser(newuser));
                JOptionPane.showMessageDialog(null, "You have become invisible", "Greetings", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        invisibleUser.add(invisibleButton);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "SellerView");
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
                cardLayout.show(cardPanel, "SellerView");
            }
        });
        getAll.add(backButton);

        return getAll;
    }

    private JPanel newManageStores() {
        JPanel manageStores = new JPanel();
        manageStores.setLayout(new GridLayout(2, 2));
        manageStores.add(new JLabel("Which User's messages would you like to read?"));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "SellerView");
            }
        });
        manageStores.add(backButton);

        return manageStores;
    }
}
