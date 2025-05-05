package com.java.view.panels;

import com.java.controller.EmailController;
import com.java.model.email.EmailMessage;
import com.java.view.components.EmailTable;
import com.java.view.components.EmailViewer;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CenterPanel extends JPanel {
    private final EmailTable emailTable;
    private final EmailViewer emailViewer;
    private final EmailController controller;

    public CenterPanel(EmailController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // Create components
        emailTable = new EmailTable();
        emailViewer = new EmailViewer();

        // Set up split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(new JScrollPane(emailTable));
        splitPane.setBottomComponent(emailViewer);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);

        // Add toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshEmails());
        toolBar.add(refreshButton);
        
        add(toolBar, BorderLayout.NORTH);

        // Add selection listener
        emailTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = emailTable.getSelectedRow();
                if (selectedRow >= 0) {
                    try {
                        EmailMessage email = controller.getEmail(selectedRow);
                        emailViewer.displayEmail(email);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                            "Error loading email: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public void refreshEmails() {
        try {
            List<EmailMessage> emails = controller.receiveEmails();
            emailTable.refresh(emails);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading emails: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}