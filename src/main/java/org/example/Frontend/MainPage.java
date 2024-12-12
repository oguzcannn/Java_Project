package org.example.Frontend;

import javax.swing.*;
import java.awt.event.*;

public class MainPage extends JFrame{
    private JButton settingsButton;
    private JTextArea FriendList;
    private JButton addFriendButton;
    private JPanel MainPagePanel;

    public MainPage (){
        add(MainPagePanel);
        setSize(400,500);
        setTitle("N'aber");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        FriendList.setEditable(false);

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        addFriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        FriendList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
    }
}
