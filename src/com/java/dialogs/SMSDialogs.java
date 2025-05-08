package com.java.dialogs;

import javax.swing.*;

import com.java.notification.SMSNotifier;

import java.awt.*;

public class SMSDialogs extends JDialog {
    private final JTextField phoneField = new JTextField("+212", 20);
    private final JTextArea messageArea = new JTextArea("Ceci est un test SMS", 3, 20);
    private final JTextArea statusArea = new JTextArea(5, 30);
    private final SMSNotifier smsNotifier = new SMSNotifier();

    public SMSDialogs(JFrame parent) {
        super(parent, "Envoyer un SMS", true);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("Numéro de téléphone :"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Message :"));
        inputPanel.add(new JScrollPane(messageArea));

        JButton sendBtn = new JButton("Envoyer");
        sendBtn.addActionListener(e -> {
            String phone = phoneField.getText().trim();
            String message = messageArea.getText().trim();
            String result = smsNotifier.sendCustomSms(phone, message);
            statusArea.setText(result);
        });

        JPanel southPanel = new JPanel();
        southPanel.add(sendBtn);

        add(inputPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        add(new JScrollPane(statusArea), BorderLayout.EAST);

        pack();
        setLocationRelativeTo(parent);
    }
}
