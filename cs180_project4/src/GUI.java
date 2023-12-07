import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends Main {
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

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
        cardPanel.add(invisibleUser, "InvisibleUser");
        cardPanel.add(getAll, "GetMessages");
        cardPanel.add(signInCard, "SignInCard");
        cardPanel.add(createAccountCard, "CreateAccountCard");
        cardPanel.add(buyerView, "BuyerView");

        frame.add(cardPanel);
        cardLayout.show(cardPanel, "SignInCard");
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

    private JPanel createSignInCard() {
        JPanel signInCard = new JPanel();
        signInCard.setLayout(new GridLayout(4, 2));

        JButton signInButton = new JButton("Sign In");
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "SignInCard");
            }
        });
        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "CreateAccountCard");
            }
        });

        signInCard.add(createAccountButton);
        signInCard.add(signInButton);
        signInCard.add(new JLabel("Username:"));
        JTextField signInUsernameField = new JTextField();
        signInCard.add(signInUsernameField);

        signInCard.add(new JLabel("Password:"));
        JPasswordField signInPasswordField = new JPasswordField();
        signInCard.add(signInPasswordField);

        JButton signInAction = new JButton("Sign In");
        signInAction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String username = signInUsernameField.getText();
                    User user = db.getUser(username);
                    if (user == null) {
                        JOptionPane.showMessageDialog(null, "Error, no user with username " + username + " found. Please try again", null, JOptionPane.INFORMATION_MESSAGE);
                    } else {
                            String password = signInPasswordField.getText();
                            if (user.getPassword().equals(password)) {
                               cardLayout.show(cardPanel, "SellerView");
                            } else {
                                JOptionPane.showMessageDialog(null, "Error, invalid password", null, JOptionPane.INFORMATION_MESSAGE);
                            }

                    }
            }
        });
    signInCard.add(signInAction);
            return signInCard;
}

    private JPanel createCreateAccountCard() {
        JPanel createAccountCard = new JPanel();
        createAccountCard.setLayout(new GridLayout(6, 2));

        JButton signInButton = new JButton("Sign In");
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "SignInCard");
            }
        });

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "CreateAccountCard");
            }
        });

        createAccountCard.add(createAccountButton);
        createAccountCard.add(signInButton);
        createAccountCard.add(new JLabel("Username:"));
        JTextField createAccountUsernameField = new JTextField();
        createAccountCard.add(createAccountUsernameField);

        createAccountCard.add(new JLabel("Password:"));
        JPasswordField createAccountPasswordField = new JPasswordField();
        createAccountCard.add(createAccountPasswordField);

        createAccountCard.add(new JLabel("Email:"));
        JTextField createAccountEmailField = new JTextField();
        createAccountCard.add(createAccountEmailField);

        createAccountCard.add(new JLabel("Are you a Buyer or Seller?"));
        JTextField createAccountQuestionField = new JTextField();
        createAccountCard.add(createAccountQuestionField);

        JButton createAccountAction = new JButton("Create Account");
        createAccountAction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = createAccountUsernameField.getText();
                String newPassword = createAccountPasswordField.getText();
                String email = createAccountEmailField.getText();
                if (createAccountQuestionField.getText().equalsIgnoreCase("seller")) {
                    User newUser = User.createUser(userType.SELLER, newUsername, newPassword, email);
                    cardLayout.show(cardPanel, "SellerView");
                } else if (createAccountQuestionField.getText().equalsIgnoreCase("buyer")) {
                    User newUser = User.createUser(userType.CUSTOMER, newUsername, newPassword, email);
                    cardLayout.show(cardPanel, "BuyerView");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid input", null, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        createAccountCard.add(createAccountAction);

        return createAccountCard;
    }

    private JPanel createNewSellerView() {
        JPanel createSellerView = new JPanel();
        createSellerView.setLayout(new GridLayout(8, 1));

        JButton createReadAll = new JButton("Read All Messages");
        createReadAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "GetMessages");
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
                cardLayout.show(cardPanel, "SignInCard");
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
                cardLayout.show(cardPanel, "SellerView");
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
                cardLayout.show(cardPanel, "SellerView");
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
                cardLayout.show(cardPanel, "SellerView");
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
        JPanel blockUser = new JPanel();
        blockUser.setLayout(new GridLayout(2, 2));
        blockUser.add(new JLabel("Enter the username you would like to block"));
        JTextField createNewBlockUser = new JTextField();

        blockUser.add(createNewBlockUser);
        JButton blockButton = new JButton("Block Person");
        blockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, createNewBlockUser.getText() + " Has Been Successfully Blocked", "Greetings", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        blockUser.add(blockButton);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "SellerView");
            }
        });
        blockUser.add(backButton);

        return blockUser;
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



