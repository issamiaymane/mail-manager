package com.java.test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class EmailClientApp {
    private static EmailService emailService;
    private static JTable emailTable;
    private static DefaultTableModel tableModel;
    private static JTextArea emailBodyTextArea;
    private static JPanel attachmentsPanel;
    private static JFrame mainFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            showLoginDialog();
        });
    }

    private static void showLoginDialog() {
        JFrame loginFrame = new JFrame("Email Client Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 200);
        loginFrame.setLocationRelativeTo(null);
        
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
                JOptionPane.showMessageDialog(loginFrame, 
                    "Please enter both email and password", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                emailService = new EmailService("webxcelsite@gmail.com", "lrrf lcih rofh lbix");
                loginFrame.dispose();
                createAndShowGUI();
            } catch (MessagingException ex) {
                JOptionPane.showMessageDialog(loginFrame, 
                    "Login failed: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        loginFrame.add(panel);
        loginFrame.setVisible(true);
    }

    private static void createAndShowGUI() {
        mainFrame = new JFrame("ENSAM Mail Manager");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1200, 800);
        mainFrame.setMinimumSize(new Dimension(800, 600));

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components
        mainPanel.add(createToolBar(), BorderLayout.NORTH);
        mainPanel.add(createSidePanel(), BorderLayout.WEST);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        mainPanel.add(createStatusPanel(), BorderLayout.SOUTH);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
        
        // Load emails on startup
        refreshEmails();
    }

    private static JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(70, 70, 70));
        toolBar.setPreferredSize(new Dimension(100, 60));

        // App title
        JLabel title = new JLabel("  ENSAM Mail Manager");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        toolBar.add(title);

        // Search field
        toolBar.add(Box.createHorizontalGlue());
        JTextField searchField = new JTextField(30);
        searchField.setPreferredSize(new Dimension(300, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        toolBar.add(searchField);

        // Search button
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(26, 115, 232));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        toolBar.add(searchButton);

        return toolBar;
    }

    private static JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(200, 100));
        sidePanel.setBackground(new Color(240, 240, 240));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        // Compose button
        JButton composeButton = new JButton("Compose");
        composeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        composeButton.setBackground(new Color(26, 115, 232));
        composeButton.setForeground(Color.WHITE);
        composeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        composeButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 25, 8, 25)
        ));
        composeButton.setMaximumSize(new Dimension(180, 40));
        composeButton.addActionListener(e -> showComposeDialog());
        sidePanel.add(composeButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Folders
        String[] folders = {"Inbox", "Starred", "Important", "Sent", "Drafts", "Trash"};
        for (String folder : folders) {
            JButton folderButton = new JButton(folder);
            folderButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            folderButton.setHorizontalAlignment(SwingConstants.LEFT);
            folderButton.setBackground(new Color(240, 240, 240));
            folderButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            folderButton.setForeground(new Color(60, 60, 60));
            folderButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            folderButton.setMaximumSize(new Dimension(180, 30));
            sidePanel.add(folderButton);
        }

        sidePanel.add(Box.createVerticalGlue());
        return sidePanel;
    }

    private static JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // Email table model
        String[] columnNames = {"From", "Subject", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Email table
        emailTable = new JTable(tableModel);
        emailTable.setRowHeight(30);
        emailTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        emailTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedEmail();
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(emailTable);
        
        // Email view panel
        JPanel emailViewPanel = new JPanel(new BorderLayout());
        emailViewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        emailBodyTextArea = new JTextArea();
        emailBodyTextArea.setEditable(false);
        emailBodyTextArea.setLineWrap(true);
        emailBodyTextArea.setWrapStyleWord(true);
        emailBodyTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        attachmentsPanel = new JPanel();
        attachmentsPanel.setLayout(new BoxLayout(attachmentsPanel, BoxLayout.Y_AXIS));
        attachmentsPanel.setBorder(BorderFactory.createTitledBorder("Attachments"));
        
        emailViewPanel.add(new JScrollPane(emailBodyTextArea), BorderLayout.CENTER);
        emailViewPanel.add(attachmentsPanel, BorderLayout.SOUTH);
        
        // Split pane for table and email view
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        
        // Toolbar with refresh button
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshEmails());
        toolBar.add(refreshButton);
        
        centerPanel.add(toolBar, BorderLayout.NORTH);
        
        return centerPanel;
    }

    private static JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        statusPanel.setBackground(new Color(240, 240, 240));

        JLabel statusLabel = new JLabel("Ready");
        statusPanel.add(statusLabel, BorderLayout.WEST);

        JLabel userLabel = new JLabel("Connected as: " + 
            (emailService != null ? emailService.username : "Not connected"));
        statusPanel.add(userLabel, BorderLayout.EAST);

        return statusPanel;
    }

    private static void showComposeDialog() {
        JDialog composeDialog = new JDialog(mainFrame, "New Message", true);
        composeDialog.setSize(800, 600);
        composeDialog.setLocationRelativeTo(mainFrame);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Top Panel (Toolbar + Fields) ===
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Toolbar with Send and Cancel buttons
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton sendButton = new JButton("Send");
        JButton cancelButton = new JButton("Cancel");

        toolBar.add(sendButton);
        toolBar.addSeparator();
        toolBar.add(cancelButton);

        topPanel.add(toolBar);

        // Message fields panel
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // To field
        JPanel toPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toPanel.add(new JLabel("To:"));
        JTextField toField = new JTextField(50);
        toPanel.add(toField);
        fieldsPanel.add(toPanel);

        // Cc field
        JPanel ccPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ccPanel.add(new JLabel("Cc:"));
        JTextField ccField = new JTextField(50);
        ccPanel.add(ccField);
        fieldsPanel.add(ccPanel);

        // Subject field
        JPanel subjectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subjectPanel.add(new JLabel("Subject:"));
        JTextField subjectField = new JTextField(50);
        subjectPanel.add(subjectField);
        fieldsPanel.add(subjectPanel);

        topPanel.add(fieldsPanel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // === Message body ===
        JTextArea messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(messageArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // === Attachments ===
        List<String> attachments = new ArrayList<>();
        JPanel composeAttachmentsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        composeAttachmentsPanel.setBorder(BorderFactory.createTitledBorder("Attachments"));

        JButton addAttachmentButton = new JButton("Add Attachment");
        addAttachmentButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(composeDialog) == JFileChooser.APPROVE_OPTION) {
                attachments.add(fileChooser.getSelectedFile().getAbsolutePath());
                composeAttachmentsPanel.add(new JLabel(fileChooser.getSelectedFile().getName()));
                composeAttachmentsPanel.revalidate();
                composeAttachmentsPanel.repaint();
            }
        });

        composeAttachmentsPanel.add(addAttachmentButton);
        mainPanel.add(composeAttachmentsPanel, BorderLayout.SOUTH);

        // === Send & Cancel actions ===
        Map<String, Object> fields = new HashMap<>();
        fields.put("dialog", composeDialog);
        fields.put("toField", toField);
        fields.put("ccField", ccField);  // Include ccField
        fields.put("subjectField", subjectField);
        fields.put("messageArea", messageArea);
        fields.put("attachments", attachments);

        sendButton.putClientProperty("fields", fields);
        sendButton.addActionListener(e -> sendEmailAction(fields));  // Pass the fields map
        cancelButton.addActionListener(e -> composeDialog.dispose());

        // Finalize
        composeDialog.setContentPane(mainPanel);
        composeDialog.setVisible(true);
    }

    private static void sendEmailAction(Map<String, Object> fields) {
    try {
        // Extract form data
        JDialog composeDialog = (JDialog) fields.get("dialog");
        JTextField toField = (JTextField) fields.get("toField");
        JTextField ccField = (JTextField) fields.get("ccField"); // Get CC field value
        JTextField subjectField = (JTextField) fields.get("subjectField");
        JTextArea messageArea = (JTextArea) fields.get("messageArea");
        List<String> attachments = (List<String>) fields.get("attachments");

        String to = toField.getText();
        String cc = ccField.getText();  // Get CC field value
        String subject = subjectField.getText();
        String message = messageArea.getText();

        // Validate required fields
        if (to.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            JOptionPane.showMessageDialog(composeDialog, "To, Subject, and Message cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // EmailService setup
        String username = "webxcelsite@gmail.com";  // Replace with your email
        String password = "lrrf lcih rofh lbix";  // Replace with your email password
        EmailService emailService = new EmailService(username, password);

        // Create the email
        List<String> recipients = new ArrayList<>();
        recipients.add(to);
        if (!cc.isEmpty()) {
            recipients.add(cc);  // Add CC if provided
        }

        // Send the email
        emailService.sendEmail(
            String.join(",", recipients),  // Send to multiple recipients (To + Cc)
            subject,
            message,
            attachments
        );

        // Show success message
        JOptionPane.showMessageDialog(composeDialog, "Email sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Close the dialog after sending the email
        composeDialog.dispose();

    } catch (MessagingException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Failed to send email: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private static void refreshEmails() {
        if (emailService == null) return;
        
        SwingWorker<List<Message>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Message> doInBackground() throws Exception {
                return emailService.receiveEmails();
            }
            
            @Override
            protected void done() {
                try {
                    List<Message> messages = get();
                    tableModel.setRowCount(0);
                    
                    for (Message message : messages) {
                        String from = InternetAddress.toString(message.getFrom());
                        String subject = message.getSubject();
                        String date = message.getSentDate() != null 
                            ? message.getSentDate().toString() 
                            : "No date";
                        tableModel.addRow(new Object[]{from, subject, date});
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Error loading emails: " + e.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }

    private static void showSelectedEmail() {
    int selectedRow = emailTable.getSelectedRow();
    if (selectedRow == -1) return;

    Message message = emailService.getMessageAt(selectedRow);

    try {
        String subject = message.getSubject();
        Address[] fromAddresses = message.getFrom();
        String from = (fromAddresses.length > 0) ? fromAddresses[0].toString() : "Unknown";
        String content = EmailService.getTextFromMessage(message);
        List<File> attachments = EmailService.getAttachmentsFromMessage(message);

        // Create dialog
        JDialog emailDialog = new JDialog(mainFrame, "Email Details", true);
        emailDialog.setSize(800, 600);
        emailDialog.setLocationRelativeTo(mainFrame);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel fromLabel = new JLabel("From: " + from);
        JLabel subjectLabel = new JLabel("Subject: " + subject);

        JTextArea contentArea = new JTextArea(content);
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setEditable(false);
        JScrollPane contentScrollPane = new JScrollPane(contentArea);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(fromLabel);
        topPanel.add(subjectLabel);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(contentScrollPane, BorderLayout.CENTER);

        // Attachment panel
        if (!attachments.isEmpty()) {
            JPanel attachPanel = new JPanel();
            attachPanel.setLayout(new BoxLayout(attachPanel, BoxLayout.Y_AXIS));
            attachPanel.setBorder(BorderFactory.createTitledBorder("Attachments"));
            for (File file : attachments) {
                JButton fileButton = new JButton(file.getName());
                fileButton.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                attachPanel.add(fileButton);
            }
            panel.add(attachPanel, BorderLayout.SOUTH);
        }

        emailDialog.setContentPane(panel);
        emailDialog.setVisible(true);

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(mainFrame, "Failed to open email: " + ex.getMessage());
    }
}

}
