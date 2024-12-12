package org.example.Frontend;
import  org.example.Backend.UserService;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings extends JFrame{
    private JLabel usernameLabel;
    private JTextField currentpassText;
    private JTextField newpassText;
    private JTextField newpassagainText;
    private JPanel settingsPanel;
    private JButton changePasswordButton;
    private JButton deleteAccountButton;
    public Settings(String userName){
        add(settingsPanel);
        setSize(400,400);
        setTitle("Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        usernameLabel.setText(userName);
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentPass = currentpassText.getText();
                String newPass = newpassText.getText();
                String newPassAgain = newpassagainText.getText();

                // Kontrolleri yapalım
                if (currentPass.isEmpty() || newPass.isEmpty() || newPassAgain.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Lütfen tüm alanları doldurun.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!newPass.equals(newPassAgain)) {
                    JOptionPane.showMessageDialog(null, "Yeni şifreler uyuşmuyor.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                UserService userService = new UserService();
                boolean isOldPasswordCorrect = userService.loginUser(userName, currentPass);
                if (!isOldPasswordCorrect) {
                    JOptionPane.showMessageDialog(null, "Eski şifre yanlış.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                userService.changePassword(userName, newPass);
                JOptionPane.showMessageDialog(null, "Şifreniz başarıyla değiştirildi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);

            }
        });
        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentPass = currentpassText.getText();
                UserService userService = new UserService();
                boolean isOldPasswordCorrect = userService.loginUser(userName, currentPass);
                if (!isOldPasswordCorrect) {
                    JOptionPane.showMessageDialog(null, "Eski şifre yanlış.", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int option = JOptionPane.showConfirmDialog(null, "Hesabınızı silmek istediğinizden emin misiniz?", "Hesap Silme", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // Kullanıcıyı silelim

                    boolean success = userService.deleteUser(userName);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Hesabınız başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                        dispose();  // JFrame'i kapatalım
                        Login login = new Login();
                        login.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Hesap silinirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
}
