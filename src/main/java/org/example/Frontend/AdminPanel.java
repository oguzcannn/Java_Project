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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
        UserService userService = new UserService();

        List<String> allUsers = userService.getAllUsers();

        DefaultListModel<String> listModel = new DefaultListModel<>();

        //  listeye  ekleme
        for (String user : allUsers) {
            listModel.addElement(user);
        }

        userList.setModel(listModel);

        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> updatedUsers = userService.getAllUsers();

                // listeye yeni kullanıcıları ekliyor
                for (String user : updatedUsers) {
                    if (!listModel.contains(user)) {
                        listModel.addElement(user);
                    }
                }

                //  kullanıcıları listeden çıkarıyor
                for (int i = 0; i < listModel.size(); i++) {
                    String user = listModel.get(i);
                    if (!updatedUsers.contains(user)) {
                        listModel.removeElement(user);
                    }
                }
            }
        });


        timer.start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.stop(); // Timer'ı durdur
            }
        });


        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedUser = userList.getSelectedValue();
                SelectedUser.setText("Selected User: " + selectedUser);
            }
        });

        // username  değiştirme işlemi
        userNameChangeBtn.addActionListener(e -> {
            String newUserName = userNameTextField.getText();
            String selectedUser = userList.getSelectedValue();
            if (newUserName == null || newUserName.isEmpty() || selectedUser == null || selectedUser.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username or selected username cannot be empty.", "error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            userService.changeUserName(selectedUser, newUserName);
        });

        //  şifre değiştirme işlemi
        userPasswordChangeBtn.addActionListener(e -> {
            String newPassword = userPasswordTextField.getText();
            String selectedUser = userList.getSelectedValue();
            if (selectedUser != null && !newPassword.isEmpty()) {
                userService.changePassword(selectedUser, newPassword);
                JOptionPane.showMessageDialog(null, "The password has been successfully changed.");
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a valid password.");
            }
        });


        deleteUserBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = userList.getSelectedValue();
                Admin admin = new Admin("admin", "", "123", "");
                admin.removeFriend(selectedUser);

            }
        });
    }
}
