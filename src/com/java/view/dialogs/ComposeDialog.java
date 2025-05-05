package com.java.view.dialogs;

import com.java.controller.EmailController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.File;

public class ComposeDialog extends JDialog {
    private final EmailController controller;
    private final JTextField toField;
    private final JTextField ccField;
    private final JTextField subjectField;
    private final JTextArea messageArea;
    private final List<String> attachments;
    private final JPanel attachmentsPanel;

    public ComposeDialog(JFrame parent, EmailController controller) {
        super(parent, "New Message", true);
        this.controller = controller;
        this.attachments = new ArrayList<>();
        
        setSize(800, 600);
        setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel with toolbar and fields
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton sendButton = new JButton("Send");
        JButton cancelButton = new JButton("Cancel");
        
        toolBar.add(sendButton);
        toolBar.addSeparator();
        toolBar.add(cancelButton);
        topPanel.add(toolBar);

        // Fields panel
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // To field
        JPanel toPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toPanel.add(new JLabel("To:"));
        toField = new JTextField(50);
        toPanel.add(toField);
        fieldsPanel.add(toPanel);

        // Cc field
        JPanel ccPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ccPanel.add(new JLabel("Cc:"));
        ccField = new JTextField(50);
        ccPanel.add(ccField);
        fieldsPanel.add(ccPanel);

        // Subject field
        JPanel subjectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subjectPanel.add(new JLabel("Subject:"));
        subjectField = new JTextField(50);
        subjectPanel.add(subjectField);
        fieldsPanel.add(subjectPanel);

        topPanel.add(fieldsPanel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Message body
        messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(messageArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Attachments panel
        attachmentsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        attachmentsPanel.setBorder(BorderFactory.createTitledBorder("Attachments"));

        JButton addAttachmentButton = new JButton("Add Attachment");
        addAttachmentButton.addActionListener(e -> addAttachment());
        attachmentsPanel.add(addAttachmentButton);
        mainPanel.add(attachmentsPanel, BorderLayout.SOUTH);

        // Button actions
        sendButton.addActionListener(e -> sendEmail());
        cancelButton.addActionListener(e -> dispose());

        setContentPane(mainPanel);
    }

    private void addAttachment() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            attachments.add(filePath);
            attachmentsPanel.add(new JLabel(fileChooser.getSelectedFile().getName()));
            attachmentsPanel.revalidate();
            attachmentsPanel.repaint();
        }
    }

    private void sendEmail() {
    try {
        String to = toField.getText().trim();
        String subject = subjectField.getText().trim();
        String message = messageArea.getText().trim();

        if (to.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "To, Subject, and Message cannot be empty",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add CC if provided
        String cc = ccField.getText().trim();
        if (!cc.isEmpty()) {
            to += "," + cc;
        }

        controller.sendEmail(to, subject, message, attachments);
        JOptionPane.showMessageDialog(this,
            "Email sent successfully!",
            "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
            "Failed to send email: " + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
}