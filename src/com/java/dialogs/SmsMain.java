package com.java.dialogs;

public class SmsMain {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            SMSDialogs dialog = new SMSDialogs(null);
            dialog.setVisible(true);
        });
    }
}

