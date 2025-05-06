package com.java.email;



import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class MailManagerApp {
    private static EmailClient emailClient;
    private static EmailReceiverService receiver;
    private static EmailSenderService sender;

    private static JFrame mainFrame;
    private static JTable emailTable;
    private static DefaultTableModel tableModel;
    private static JLabel statusLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception ignored) {}
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
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        gbc.gridx=0; gbc.gridy=0; panel.add(emailLabel, gbc);
        gbc.gridx=1; gbc.gridy=0; panel.add(emailField, gbc);
        gbc.gridx=0; gbc.gridy=1; panel.add(passwordLabel, gbc);
        gbc.gridx=1; gbc.gridy=1; panel.add(passwordField, gbc);
        gbc.gridx=1; gbc.gridy=2; panel.add(loginButton, gbc);

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
                emailClient = new EmailClient(email, password);
                receiver    = new EmailReceiverService(emailClient);
                sender      = new EmailSenderService(emailClient);
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
        mainFrame = new JFrame("Mail Manager");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1200, 800);
        mainFrame.setMinimumSize(new Dimension(800, 600));

        JPanel mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        mainPanel.add(createToolBar(), BorderLayout.NORTH);
        mainPanel.add(createSidePanel(), BorderLayout.WEST);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        mainPanel.add(createStatusPanel(), BorderLayout.SOUTH);

        mainFrame.setContentPane(mainPanel);
        mainFrame.setVisible(true);

        // Charge la boîte reçue par défaut
        refreshFolder("INBOX");
    }

    private static JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(70,70,70));
        toolBar.setPreferredSize(new Dimension(100,60));

        JLabel title = new JLabel("  Mail Manager");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        toolBar.add(title);

        toolBar.add(Box.createHorizontalGlue());
        JTextField searchField = new JTextField(30);
        searchField.setMaximumSize(new Dimension(300,30));
        toolBar.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(26,115,232));
        searchButton.setForeground(Color.WHITE);
        toolBar.add(searchButton);

        return toolBar;
    }

    private static JPanel createSidePanel() {
        // mappe l’affichage aux vrais noms IMAP
        Map<String,String> folderMap = new LinkedHashMap<>();
        folderMap.put("Inbox",     "INBOX");
        folderMap.put("Starred",   "[Gmail]/Starred");
        folderMap.put("Important", "[Gmail]/Important");
        folderMap.put("Sent",      "[Gmail]/Sent Mail");
        folderMap.put("Drafts",    "[Gmail]/Drafts");
        folderMap.put("Trash",     "[Gmail]/Trash");

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(200, 100));
        sidePanel.setBackground(new Color(240, 240, 240));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10,5,10,5));

        JButton compose = new JButton("Compose");
        compose.setAlignmentX(Component.CENTER_ALIGNMENT);
        compose.setBackground(new Color(26,115,232));
        compose.setForeground(Color.WHITE);
        compose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        compose.setMaximumSize(new Dimension(180,40));
        compose.addActionListener(e -> showComposeDialog());
        sidePanel.add(compose);  // <— ici, on ajoute bien "compose"

        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // pour chaque entrée du map, on crée un bouton
        for (Map.Entry<String,String> entry : folderMap.entrySet()) {
            String displayName = entry.getKey();
            String imapName     = entry.getValue();

            JButton folderBtn = new JButton(displayName);
            folderBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            folderBtn.setBackground(new Color(240,240,240));
            folderBtn.setBorder(BorderFactory.createEmptyBorder(8,15,8,15));
            folderBtn.setMaximumSize(new Dimension(180,30));
            folderBtn.addActionListener(evt -> refreshFolder(imapName));
            sidePanel.add(folderBtn);
        }

        sidePanel.add(Box.createVerticalGlue());
        return sidePanel;
    }


    private static JPanel createCenterPanel() {
        JPanel center = new JPanel(new BorderLayout());

        String[] cols = {"From","Subject","Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;  // <-- ajouté

            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        emailTable = new JTable(tableModel);
        emailTable.setRowHeight(30);
        emailTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        emailTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showSelectedEmailDialog();
                }
            }
        });

        JScrollPane tablePane = new JScrollPane(emailTable);
        center.add(tablePane, BorderLayout.CENTER);
        return center;
    }


    private static void showSelectedEmailDialog() {
        int idx = emailTable.getSelectedRow();
        if (idx < 0) return;

        try {
            Message msg = receiver.getMessage(idx);

            // Création du dialog modal
            JDialog dlg = new JDialog(mainFrame, "Email Details", true);
            dlg.setSize(800, 600);
            dlg.setLocationRelativeTo(mainFrame);

            // Panel principal
            JPanel panel = new JPanel(new BorderLayout(10,10));
            panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

            // Haut : expéditeur + sujet
            JPanel top = new JPanel(new GridLayout(2,1));
            top.add(new JLabel("From: " + InternetAddress.toString(msg.getFrom())));
            top.add(new JLabel("Subject: " + msg.getSubject()));
            panel.add(top, BorderLayout.NORTH);

            // Centre : corps du message
            JTextArea contentArea = new JTextArea(EmailReceiverService.extractText(msg));
            contentArea.setEditable(false);
            contentArea.setLineWrap(true);
            contentArea.setWrapStyleWord(true);
            panel.add(new JScrollPane(contentArea), BorderLayout.CENTER);

            // Sud : pièces-jointes (s'il y en a)
            List<File> atts = EmailReceiverService.extractAttachments(msg);
            if (!atts.isEmpty()) {
                JPanel attachP = new JPanel();
                attachP.setLayout(new BoxLayout(attachP, BoxLayout.Y_AXIS));
                attachP.setBorder(BorderFactory.createTitledBorder("Attachments"));
                for (File f : atts) {
                    JButton btn = new JButton(f.getName());
                    btn.addActionListener(ev -> {
                        try { Desktop.getDesktop().open(f); }
                        catch (Exception ignored) {}
                    });
                    attachP.add(btn);
                }
                panel.add(attachP, BorderLayout.SOUTH);
            }

            dlg.setContentPane(panel);
            dlg.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static JPanel createStatusPanel() {
        JPanel status = new JPanel(new BorderLayout());
        status.setBorder(BorderFactory.createMatteBorder(1,0,0,0,new Color(200,200,200)));
        statusLabel = new JLabel("Ready");
        status.add(statusLabel, BorderLayout.WEST);
        return status;
    }

    private static void refreshFolder(String folderName) {
        statusLabel.setText("Loading " + folderName + "...");
        tableModel.setRowCount(0);
        SwingWorker<List<Message>,Void> worker = new SwingWorker<>() {
            @Override protected List<Message> doInBackground() throws Exception {
                return receiver.receiveEmails(folderName);
            }
            @Override protected void done() {
                try {
                    List<Message> msgs = get();
                    for (Message m : msgs) {
                        String from = InternetAddress.toString(m.getFrom());
                        String subj = m.getSubject();
                        String date = m.getSentDate()!=null?m.getSentDate().toString():"";
                        tableModel.addRow(new Object[]{from, subj, date});
                    }
                    statusLabel.setText("Loaded " + msgs.size() + " messages");
                } catch (Exception ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }


    private static void showComposeDialog() {
        JDialog dlg = new JDialog(mainFrame, "New Message", true);
        dlg.setSize(800,600);
        dlg.setLocationRelativeTo(mainFrame);

        JPanel p = new JPanel(new BorderLayout(10,10));
        // Top: fields
        JPanel fields = new JPanel(new GridLayout(3,2,5,5));
        JTextField toField = new JTextField();
        JTextField ccField = new JTextField();
        JTextField subjField = new JTextField();
        fields.add(new JLabel("To:")); fields.add(toField);
        fields.add(new JLabel("Cc:")); fields.add(ccField);
        fields.add(new JLabel("Subject:")); fields.add(subjField);
        p.add(fields, BorderLayout.NORTH);

        // Center: message
        JTextArea msgArea = new JTextArea();
        p.add(new JScrollPane(msgArea), BorderLayout.CENTER);

        // South: attachments + buttons
        JPanel south = new JPanel(new BorderLayout());
        JPanel attachP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        attachP.setBorder(BorderFactory.createTitledBorder("Attachments"));
        List<File> atts = new ArrayList<>();
        JButton addAtt = new JButton("Add Attachment");
        addAtt.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(dlg)==JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                atts.add(f);
                attachP.add(new JLabel(f.getName()));
                attachP.revalidate();
            }
        });
        attachP.add(addAtt);
        south.add(attachP, BorderLayout.CENTER);

        JPanel btnP = new JPanel();
        JButton send = new JButton("Send");
        JButton cancel = new JButton("Cancel");
        btnP.add(send);
        btnP.add(cancel);
        south.add(btnP, BorderLayout.SOUTH);

        p.add(south, BorderLayout.SOUTH);

        send.addActionListener(e -> {
            List<String> to = Arrays.stream(toField.getText().split(","))
                                    .map(String::trim).filter(s->!s.isEmpty())
                                    .collect(Collectors.toList());
            List<String> cc = Arrays.stream(ccField.getText().split(","))
                                    .map(String::trim).filter(s->!s.isEmpty())
                                    .collect(Collectors.toList());
            try {
                sender.sendEmail(to, cc, subjField.getText(),
                                 msgArea.getText(), atts);
                JOptionPane.showMessageDialog(dlg,
                    "Email sent successfully", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose();
                refreshFolder("Sent");
            } catch (MessagingException ex) {
                JOptionPane.showMessageDialog(dlg,
                    "Failed to send: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancel.addActionListener(e -> dlg.dispose());

        dlg.setContentPane(p);
        dlg.setVisible(true);
    }
}
