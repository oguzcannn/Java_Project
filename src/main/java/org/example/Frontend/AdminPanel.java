package org.example.Frontend;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.example.Backend.Admin;
import org.example.Backend.UserService;

public class AdminPanel  extends  JFrame{
    private JList<String> userList;
    private JLabel SelectedUser;
    private JTextField userNameTextField;
    private JButton userNameChangeBtn;
    private JTextField userPasswordTextField;
    private JButton userPasswordChangeBtn;
    private JButton deleteUserBtn;
    private JPanel adminPanel;

    public AdminPanel(){
        add(adminPanel);
        setSize(500, 500);
        setTitle("Admin Panel");

        // UserService örneği oluşturuluyor
        UserService userService = new UserService();

        // Veritabanındaki tüm kullanıcıları alıyoruz
        List<String> allUsers = userService.getAllUsers();

        // List modelini oluşturuyoruz
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Kullanıcıları liste modeline ekliyoruz
        for (String user : allUsers) {
            listModel.addElement(user);
        }

        // userList'e DefaultListModel set ediliyor
        userList.setModel(listModel);

        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Yeni kullanıcı listesini alıyoruz
                List<String> updatedUsers = userService.getAllUsers();

                // Yeni kullanıcıları ekliyoruz
                for (String user : updatedUsers) {
                    if (!listModel.contains(user)) {
                        listModel.addElement(user);
                    }
                }

                // Silinen kullanıcıları listeden çıkarıyoruz
                for (int i = 0; i < listModel.size(); i++) {
                    String user = listModel.get(i);
                    if (!updatedUsers.contains(user)) {
                        listModel.removeElement(user);
                    }
                }
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

        // Kullanıcı seçildiğinde yapılacak işlemleri tanımlayabilirsiniz
        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedUser = userList.getSelectedValue();
                SelectedUser.setText("Seçilen Kullanıcı: " + selectedUser);
            }
        });

        // Kullanıcı adı değiştirme işlemi
        userNameChangeBtn.addActionListener(e -> {
            String newUserName = userNameTextField.getText();
            String selectedUser = userList.getSelectedValue();
            userService.changeUserName(selectedUser, newUserName);
        });

        // Kullanıcı şifresi değiştirme işlemi
        userPasswordChangeBtn.addActionListener(e -> {
            String newPassword = userPasswordTextField.getText();
            String selectedUser = userList.getSelectedValue();
            if (selectedUser != null && !newPassword.isEmpty()) {
                userService.changePassword(selectedUser, newPassword);
                JOptionPane.showMessageDialog(null, "Şifre başarıyla değiştirildi.");
            } else {
                JOptionPane.showMessageDialog(null, "Geçerli bir şifre girin.");
            }
        });


        deleteUserBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = userList.getSelectedValue();
                Admin admin = new Admin("admin", "admin", "admin123", "admin");
                admin.removeFriend(selectedUser);

            }
        });
    }
}
