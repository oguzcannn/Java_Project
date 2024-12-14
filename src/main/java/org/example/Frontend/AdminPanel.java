package org.example.Frontend;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

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
            if (selectedUser != null && !newUserName.isEmpty()) {
                userService.changeUserName(selectedUser, newUserName);
                // Kullanıcı adı değiştiğinde listeyi güncelle
                listModel.set(userList.getSelectedIndex(), newUserName);
                JOptionPane.showMessageDialog(null, "Kullanıcı adı başarıyla değiştirildi.");
            } else {
                JOptionPane.showMessageDialog(null, "Geçerli bir kullanıcı adı girin.");
            }
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
                admin.deleteUser(selectedUser);

            }
        });
    }
}
