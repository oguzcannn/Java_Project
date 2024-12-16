package org.example.Frontend;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import org.example.Backend.ChatService;
import org.example.Backend.User;
import org.example.Backend.UserService;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainPage extends JFrame{
    private JButton settingsButton;
    private JList<String> FriendList;
    private JButton addFriendButton;
    private JPanel MainPagePanel;
    private JTextField friendTextField;
    private JButton buttonRemoveFriend;
    private JButton adminPanelButton;
    private List<String> friends;

    public MainPage (String userName){
        add(MainPagePanel);
        setSize(400,500);
        setTitle("N'aber");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }

        UserService userService = new UserService();

        friends = new CopyOnWriteArrayList<>(userService.getFriendList(userName));


        DefaultListModel<String> listModel = new DefaultListModel<>();


        for (String friend : friends) {
            listModel.addElement(friend);
        }


        FriendList.setModel(listModel);

        // timer ile arkadaş listesni güncelliyoruz.
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // arkadaş listesini çekiyoz
                List<String> updatedFriends = userService.getFriendList(userName);

                // yenileri ekliyozz
                for (String friend : updatedFriends) {
                    if (!listModel.contains(friend)) {
                        listModel.addElement(friend);
                    }
                }

                // silinenleri çıkar
                for (String friend : friends) {
                    if (!updatedFriends.contains(friend)) {
                        listModel.removeElement(friend);
                    }
                }

                // en son güncelle
                friends.clear();
                friends.addAll(updatedFriends);
            }
        });


        timer.start();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.stop();
            }
        });
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Settings settings = new Settings(userName);
                settings.setVisible(true);

            }
        });




        addFriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String friend;
                friend = friendTextField.getText();
                UserService userService = new UserService();
                if(!userService.isUsernameExists(friend)){
                    JOptionPane.showMessageDialog(null,"Incorrect username.","error",JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                if (!listModel.contains(friend)){
                    listModel.addElement(friend);
                    FriendList.setModel(listModel);
                }

                List<String> participants = new ArrayList<>();
                participants.add(userName);
                participants.add(friend);


                ChatService chatService = new ChatService();
                String chatId = chatService.createChat(participants);
                if (chatId != null) {
                    System.out.println("The chat has been successfully created.");
                }

                userService.addFriend(userName,friend);
                userService.addFriend(friend,userName);
                friendTextField.setText("");
            }
        });
        FriendList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int index = FriendList.locationToIndex(e.getPoint());
                // tıklanan index geçerli mi kontrol et
                if (index >= 0) {
                    // tıklanan ismi al
                    String selectedFriend = FriendList.getModel().getElementAt(index);
                    ChatService chatService = new ChatService();

                    String chatId = chatService.findChatIdByParticipants(userName, selectedFriend);



                    Chat chat = new Chat(selectedFriend, userName, chatId);
                    chat.setVisible(true);

                }
            }
        });
        if (!userName.equals("admin")) {
            adminPanelButton.setVisible(false);

        }

        adminPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminPanel adminPanel = new AdminPanel();
                adminPanel.setVisible(true);
            }
        });
        buttonRemoveFriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = new User(userName, "", "", "");
                user.removeFriend(friendTextField.getText());
            }
        });
    }
}
