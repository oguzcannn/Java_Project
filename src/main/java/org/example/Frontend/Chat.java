package org.example.Frontend;

import org.example.Backend.ChatService;
import org.example.Backend.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Chat extends JFrame {
    private JTextField messagesInput;
    private JPanel messagesPanel;
    private JButton sendButton;
    private JList<String> messagesList;

    private final ChatService chatService;
    private final String chatId;
    private final DefaultListModel<String> listModel;

    public Chat(String friend, String currentUser, String chatId) {
        add(messagesPanel);
        setSize(500, 600);
        setTitle("Chat with: " + friend);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.chatService = new ChatService();
        this.chatId = chatId;
        this.listModel = new DefaultListModel<>();
        messagesList.setModel(listModel);

        loadMessages();

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageContent = messagesInput.getText();
                if (!messageContent.isEmpty()) {
                    Message message = new Message(currentUser, friend, messageContent);
                    chatService.addMessage(chatId, message);
                    listModel.addElement("You: " + messageContent);
                    messagesInput.setText("");
                }
            }
        });
    }

    private void loadMessages() {
        List<Message> messages = chatService.getMessages(chatId);
        for (Message msg : messages) {
            String formattedMessage = (msg.getSender_id().equals(chatId) ? "You: " : "Friend: ") + msg.getMessage_content();
            listModel.addElement(formattedMessage);
        }
    }
}
