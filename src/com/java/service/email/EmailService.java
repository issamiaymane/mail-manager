package com.java.service.email;

import com.java.model.email.EmailMessage;
import java.util.List;

public class EmailService {
    private final EmailSender sender;
    private final EmailReceiver receiver;
    private List<EmailMessage> messages;
    private final String username;

    public EmailService(String username, String password) {
        this.username = username;
        this.sender = new EmailSender(username, password);
        this.receiver = new EmailReceiver(username, password);
    }

    public void sendEmail(String to, String subject, String body, List<String> attachments) throws Exception {
        sender.send(to, subject, body, attachments);
    }

    public List<EmailMessage> receiveEmails() throws Exception {
        this.messages = receiver.receive();
        return messages;
    }

    public EmailMessage getMessageAt(int index) {
        if (messages == null || index < 0 || index >= messages.size()) {
            throw new IndexOutOfBoundsException("Invalid message index");
        }
        return messages.get(index);
    }
    
    public String getUsername() {
        return this.username;
    }
}