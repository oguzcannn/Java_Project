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
        // UserService örneği oluşturuluyor
        UserService userService = new UserService();

        // Arkadaş listesini sınıfın üyesine yükleyin.
        friends = new CopyOnWriteArrayList<>(userService.getFriendList(userName));

        // List modelini oluşturuyoruz
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Arkadaşları liste modeline ekliyoruz
        for (String friend : friends) {
            listModel.addElement(friend);
        }

        // FriendList'e DefaultListModel set ediliyor
        FriendList.setModel(listModel);

        // Timer içinde listeyi güncelleyin
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Yeni arkadaş listesini alıyoruz
                List<String> updatedFriends = userService.getFriendList(userName);

                // Yeni arkadaşları ekliyoruz
                for (String friend : updatedFriends) {
                    if (!listModel.contains(friend)) {
                        listModel.addElement(friend);
                    }
                }

                // Silinen arkadaşları listeden çıkarıyoruz
                for (String friend : friends) {
                    if (!updatedFriends.contains(friend)) {
                        listModel.removeElement(friend);
                    }
                }

                // Arkadaş listesini güncelliyoruz
                friends.clear();
                friends.addAll(updatedFriends);
            }
        });


        // Timer başlatılıyor
        timer.start();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.stop(); // Timer'ı durdur
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
                    JOptionPane.showMessageDialog(null,"Yanlış kullanıcı adı","Hata",JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                if (!listModel.contains(friend)){
                    listModel.addElement(friend);
                    FriendList.setModel(listModel);
                }

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
