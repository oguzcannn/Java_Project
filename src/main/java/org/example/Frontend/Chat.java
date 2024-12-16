package org.example.Frontend;

import org.example.Backend.ChatService;
import org.example.Backend.Message;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class Chat extends JFrame {
    private JTextField messagesInput;
    private JPanel messagesPanel;
    private JButton sendButton;
    private JList<String> messagesList;
    private JScrollPane messagesScrollPane;
    private final DefaultListModel<String> listModel;
    private int previousMessageCount = 0;

    public Chat(String friend, String currentUser, String chatId) {
        add(messagesPanel);
        setSize(500, 600);
        setTitle("Chat with: " + friend);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }

        ChatService chatService = new ChatService();

        this.listModel = new DefaultListModel<>();
        messagesList.setModel(listModel);
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentMessageCount = chatService.getMessages(chatId).size();  // Mevcut mesaj sayısı

                if (currentMessageCount > previousMessageCount) {
                    // yeni mesaj elenirse listeye ekle
                    List<Message> newMessages = chatService.getMessages(chatId);
                    for (int i = previousMessageCount; i < currentMessageCount; i++) {
                        Message msg = newMessages.get(i);
                        String formattedMessage = (msg.getSender_id().equals(currentUser) ? "You: " : "Friend: ") + msg.getMessage_content();
                        listModel.addElement(formattedMessage);
                        scrollToBottom();
                    }
                    previousMessageCount = currentMessageCount;
                }
            }
        });

        timer.start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.stop();
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageContent = messagesInput.getText();
                if (!messageContent.isEmpty()) {
                    Message message = new Message(currentUser, friend, messageContent);
                    chatService.addMessage(chatId, message);
                    messagesInput.setText("");
                }
            }
        });

        messagesInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // entera  basınca çalışır
                    String messageContent = messagesInput.getText();
                    if (!messageContent.isEmpty()) {
                        Message message = new Message(currentUser, friend, messageContent);
                        chatService.addMessage(chatId, message);
                        messagesInput.setText("");
                        scrollToBottom();
                    }
                }
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                timer.stop();
                 }
        });
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JScrollBar verticalScrollBar = messagesScrollPane.getVerticalScrollBar();
                verticalScrollBar.setValue(verticalScrollBar.getMaximum());
            }
        });
    }
}
