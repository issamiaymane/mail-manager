package com.java.service.email;

import com.java.model.email.EmailMessage;
import javax.mail.*;
import java.util.*;
import java.util.stream.Collectors;

public class EmailReceiver {
    private final String username;
    private final String password;
    private Store store;

    public EmailReceiver(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public List<EmailMessage> receive() throws Exception {
    Properties imapProps = new Properties();
    imapProps.put("mail.imap.host", "imap.gmail.com");
    imapProps.put("mail.imap.port", "993");
    imapProps.put("mail.imap.ssl.enable", "true");
    imapProps.put("mail.imap.connectiontimeout", "10000");
    imapProps.put("mail.imap.timeout", "10000");

    Session imapSession = Session.getInstance(imapProps, null);
    Store store = imapSession.getStore("imaps"); // Changed to imaps
    store.connect("imap.gmail.com", username, password);

    try {
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();
        Arrays.sort(messages, (m1, m2) -> {
            try {
                return m2.getSentDate().compareTo(m1.getSentDate());
            } catch (MessagingException e) {
                return 0;
            }
        });

        return Arrays.stream(messages)
                   .map(message -> {
                       try {
                           return new EmailMessage(message);
                       } catch (Exception e) {
                           return null;
                       }
                   })
                   .filter(Objects::nonNull)
                   .collect(Collectors.toList());
    } finally {
        try {
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

    public void close() throws MessagingException {
        if (store != null && store.isConnected()) {
            store.close();
        }
    }
}