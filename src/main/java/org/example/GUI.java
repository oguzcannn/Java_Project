package org.example;
import org.example.Backend.User;
import org.example.Backend.UserService;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI extends JFrame{
    private JTextField textField1;
    private JTextField textField2;
    private JButton kaydolButton;
    private JPanel Panel1;
    private JLabel mailLabel;
    private JLabel passwordLabel;
    private JLabel usernumberLabel;
    private JLabel usernameLabel;
    private JTextField textField3;
    private JTextField textField4;

    public GUI(){
        add(Panel1);
        setSize(400,200);
        setTitle("Kullanıcı kaydı");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        kaydolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username, usernumber, password, mail;
                username = textField1.getText();
                usernumber = textField2.getText();
                password = textField3.getText();
                mail = textField4.getText();

                User user = new User(username,usernumber,password,mail);
                UserService userService = new UserService();
                userService.registerUser(user);
                System.out.println("Kullanıcı eklendi.");
            }
        });
    }
}