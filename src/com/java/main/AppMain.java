package com.java.main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.java.view.dialogs.LoginDialog;

/**
 * Main entry point for the ENSAM Mail Manager application
 */
public class AppMain {
    /**
     * Application entry point
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set system look and feel for consistent UI
        setSystemLookAndFeel();
        
        // Show the login dialog to start the application
        SwingUtilities.invokeLater(() -> {
            LoginDialog.show();
        });
    }

    /**
     * Attempts to set the system's native look and feel
     */
    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Warning: Could not set system look and feel");
            try {
                // Fallback to Nimbus if system LAF fails
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception ex) {
                System.err.println("Error: Could not set any look and feel");
            }
        }
    }
}