package org.example.Frontend;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Chat extends JFrame {
    private JTextField messagesInput;
    private JPanel messagesPanel;
    private JButton sendButton;
    private JList<String> messagesList;


    public Chat(String friend){
        add(messagesPanel);
        setSize(500, 600);
        setTitle(friend);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> jList = new JList<>(listModel);



        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messagesInput.getText();
                if (!message.isEmpty()){
                    listModel.addElement(message);
                    messagesList.setModel(listModel);
                    messagesInput.setText("");
                }

            }
        });
    }




}
