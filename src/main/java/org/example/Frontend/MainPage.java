package org.example.Frontend;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import org.example.Backend.ChatService;
import org.example.Backend.UserService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MainPage extends JFrame{
    private JButton settingsButton;
    private JList<String> FriendList;
    private JButton addFriendButton;
    private JPanel MainPagePanel;
    private JTextField friendTextField;

    public MainPage (String userName){
        add(MainPagePanel);
        setSize(400,500);
        setTitle("N'aber");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // UserService örneği oluşturuluyor
        UserService userService = new UserService();

        // Arkadaş listesi alınıyor
        List<String> friends = userService.getFriendList(userName);

        // List modelini oluşturuyoruz
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Arkadaşları liste modeline ekliyoruz
        for (String friend : friends) {
            listModel.addElement(friend);
        }

        // FriendList'e DefaultListModel set ediliyor
        FriendList.setModel(listModel);


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
                    JOptionPane.showMessageDialog(null,"Yanlış kullanıcı adı","Hata",JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                listModel.addElement(friend);
                FriendList.setModel(listModel);

                List<String> participants = new ArrayList<>();
                participants.add(userName);
                participants.add(friend);

                // ChatService kullanarak sohbet oluşturuluyor
                ChatService chatService = new ChatService();
                String chatId = chatService.createChat(participants);
                if (chatId != null) {
                    System.out.println("Sohbet başarıyla oluşturuldu");
                }

                userService.addFriend(userName,friend);
                userService.addFriend(friend,userName);
                friendTextField.setText("");
            }
        });
        FriendList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Tıklanan index'i alıyoruz
                int index = FriendList.locationToIndex(e.getPoint());
                // Eğer tıklama geçerli bir index'e denk geliyorsa
                if (index >= 0) {
                    // Tıklanan arkadaşın ismini alıyoruz
                    String selectedFriend = FriendList.getModel().getElementAt(index);
                    ChatService chatService = new ChatService();
                    // ChatService kullanarak bu iki kullanıcı arasındaki sohbeti buluyoruz
                    String chatId = chatService.findChatIdByParticipants(userName, selectedFriend);

                    // Eğer sohbet zaten varsa, sohbeti açıyoruz

                    Chat chat = new Chat(selectedFriend, userName, chatId);
                    chat.setVisible(true);

                }
            }
        });
    }
}
