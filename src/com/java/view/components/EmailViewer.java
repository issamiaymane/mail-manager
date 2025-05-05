package com.java.view.components;

import com.java.model.email.EmailAttachment;
import com.java.model.email.EmailMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

public class EmailViewer extends JPanel {
    private final JTextArea contentArea;
    private final JPanel attachmentsPanel;

    public EmailViewer() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(new JScrollPane(contentArea), BorderLayout.CENTER);

        attachmentsPanel = new JPanel();
        attachmentsPanel.setLayout(new BoxLayout(attachmentsPanel, BoxLayout.Y_AXIS));
        attachmentsPanel.setBorder(BorderFactory.createTitledBorder("Attachments"));
        add(attachmentsPanel, BorderLayout.SOUTH);
    }

    public void displayEmail(EmailMessage email) {
        contentArea.setText(email.getContent());
        attachmentsPanel.removeAll();

        List<EmailAttachment> attachments = email.getAttachments();
        if (!attachments.isEmpty()) {
            for (EmailAttachment attachment : attachments) {
                JButton attachmentButton = new JButton(attachment.getName());
                attachmentButton.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().open(attachment.getFile());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this,
                            "Could not open attachment: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                attachmentsPanel.add(attachmentButton);
            }
        }
        attachmentsPanel.revalidate();
        attachmentsPanel.repaint();
    }
}