package org.example.Frontend;

import org.example.Backend.UserService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings extends JFrame{
    private JLabel usernameLabel;
    private JTextField currentpassText;
    private JTextField newpassText;
    private JTextField newpassagainText;
    private JPanel settingsPanel;
    private JButton changePasswordButton;
    private JButton deleteAccountButton;
    private JTextField newusername;
    private JButton changeUserNameBtn;

    public Settings(String userName){
        add(settingsPanel);
        setSize(400,400);
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
        usernameLabel.setText(userName);
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentPass = currentpassText.getText();
                String newPass = newpassText.getText();
                String newPassAgain = newpassagainText.getText();


                if (currentPass.isEmpty() || newPass.isEmpty() || newPassAgain.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all the fields.", "error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!newPass.equals(newPassAgain)) {
                    JOptionPane.showMessageDialog(null, "The new passwords do not match.", "error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                UserService userService = new UserService();
                boolean isOldPasswordCorrect = userService.loginUser(userName, currentPass);
                if (!isOldPasswordCorrect) {
                    JOptionPane.showMessageDialog(null, "The old password is incorrect.", "error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                userService.changePassword(userName, newPass);
                JOptionPane.showMessageDialog(null, "Your password has been successfully changed!", "Successful", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                SwingUtilities.invokeLater(()->{
                    Login login = new Login();
                    login.setVisible(true);
                });
            }
        });
        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentPass = currentpassText.getText();
                UserService userService = new UserService();
                boolean isOldPasswordCorrect = userService.loginUser(userName, currentPass);
                if (!isOldPasswordCorrect) {
                    JOptionPane.showMessageDialog(null, "The old password is incorrect.", "error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?", "delete account", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {


                    boolean success = userService.deleteUser(userName);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Are you sure you want to delete your account?", "Successful", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        SwingUtilities.invokeLater(()->{
                            Login login = new Login();
                            login.setVisible(true);
                        });
                    } else {
                        JOptionPane.showMessageDialog(null, "An error occurred while deleting the account.", "error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        changeUserNameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserService userService = new UserService();
                String newUsername = newusername.getText();
                if (!newUsername.isEmpty()) {
                    userService.changeUserName(userName , newUsername);
                    dispose();
                    SwingUtilities.invokeLater(()->{
                        Login login = new Login();
                        login.setVisible(true);
                    });
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid username.");
                }
            }
        });
    }
}
