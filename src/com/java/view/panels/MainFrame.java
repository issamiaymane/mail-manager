package com.java.view.panels;

import com.java.controller.EmailController;
import com.java.view.components.EmailTable;
import com.java.view.dialogs.ComposeDialog;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final EmailController controller;
    private EmailTable emailTable;

    public MainFrame(EmailController controller) {
        this.controller = controller;
        setTitle("ENSAM Mail Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(800, 600));
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(createToolBar(), BorderLayout.NORTH);
        mainPanel.add(new SidePanel(this), BorderLayout.WEST);
        mainPanel.add(new CenterPanel(controller), BorderLayout.CENTER);
        mainPanel.add(createStatusPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(70, 70, 70));
        toolBar.setPreferredSize(new Dimension(100, 60));

        JLabel title = new JLabel("  ENSAM Mail Manager");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        toolBar.add(title);

        toolBar.add(Box.createHorizontalGlue());
        JTextField searchField = new JTextField(30);
        searchField.setPreferredSize(new Dimension(300, 30));
        toolBar.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(26, 115, 232));
        searchButton.setForeground(Color.WHITE);
        toolBar.add(searchButton);

        return toolBar;
    }

    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        statusPanel.setBackground(new Color(240, 240, 240));

        JLabel statusLabel = new JLabel("Ready");
        statusPanel.add(statusLabel, BorderLayout.WEST);

        JLabel userLabel = new JLabel("Connected as: " + controller.getUsername());
        statusPanel.add(userLabel, BorderLayout.EAST);

        return statusPanel;
    }

    public void showComposeDialog() {
        new ComposeDialog(this, controller).setVisible(true);
    }

    public void refreshEmails() {
        try {
            emailTable.refresh(controller.receiveEmails());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading emails", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}