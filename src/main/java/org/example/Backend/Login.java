package org.example.Backend;

import org.example.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Login extends JFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JButton loginButton;
    private JPanel loginPanel;
    private JButton signUpButton;

    public Login(){
        add(loginPanel);
        setSize(400, 200);
        setTitle("Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName, userPassword;
                userName = textField1.getText();
                userPassword = textField2.getText();
                UserService userService = new UserService();
                if (userService.loginUser(userName, userPassword)){
                    JOptionPane.showMessageDialog(null, "kullanıcı bulundıu", "hata", JOptionPane.PLAIN_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "kullanıcı bulunamadı", "hata", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                dispose();
                SwingUtilities.invokeLater(()->{
                    GUI singup = new GUI();
                    singup.setVisible(true);
                });
            }
        });
    }
}
