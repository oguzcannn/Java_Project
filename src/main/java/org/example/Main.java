package org.example;


import org.example.Backend.Login;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }

        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });

    }
}
