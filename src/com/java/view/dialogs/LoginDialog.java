package com.java.view.dialogs;

import com.java.controller.EmailController;
import com.java.view.panels.MainFrame;

import javax.swing.*;
import java.awt.*;

public class LoginDialog {
    public static void show() {
        JFrame frame = new JFrame("Email Client Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(passwordField, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, 
                    "Please enter both email and password", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                EmailController controller = new EmailController(email, password);
                frame.dispose();
                new MainFrame(controller).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Login failed: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}