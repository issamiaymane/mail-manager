package com.java.controller;

import com.java.model.email.EmailMessage;
import com.java.service.email.EmailService;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class EmailController {
    private final EmailService emailService;

    public EmailController(String username, String password) {
        this.emailService = new EmailService(username, password);
    }

    public void sendEmail(String to, String subject, String body, List<String> attachments) throws Exception {
        emailService.sendEmail(to, subject, body, attachments);
    }

    public List<EmailMessage> receiveEmails() {
        try {
            return emailService.receiveEmails();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error loading emails: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>(); // Return empty list on error
        }
    }

    public EmailMessage getEmail(int index) throws Exception {
        return emailService.getMessageAt(index);
    }
    
    public String getUsername() {
        return this.emailService.getUsername();
    }
}