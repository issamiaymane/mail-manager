package com.java.view.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SidePanel extends JPanel {
    public SidePanel(MainFrame mainFrame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(200, 100));
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        // Compose button
        JButton composeButton = new JButton("Compose");
        composeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        composeButton.setBackground(new Color(26, 115, 232));
        composeButton.setForeground(Color.WHITE);
        composeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        composeButton.setMaximumSize(new Dimension(180, 40));
        composeButton.addActionListener(e -> mainFrame.showComposeDialog());
        add(composeButton);
        add(Box.createRigidArea(new Dimension(0, 20)));

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
            
            // Add action listener for folder selection
            folderButton.addActionListener(e -> {
                // TODO: Implement folder switching logic
                if (folder.equals("Inbox")) {
                    mainFrame.refreshEmails();
                }
            });
            
            add(folderButton);
        }

        add(Box.createVerticalGlue());
    }
}