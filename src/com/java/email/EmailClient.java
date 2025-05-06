package com.java.email;

import javax.mail.*;
import java.util.Properties;

public class EmailClient {
    private final String username;
    private final String password;
    private Session smtpSession;
    private Session imapSession;
    private Store store;

    public EmailClient(String username, String password) throws MessagingException {
        this.username = username;
        this.password = password;
        initSmtpSession();
        initImapSession();
    }

    private void initSmtpSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        smtpSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private void initImapSession() {
        Properties props = new Properties();
        props.put("mail.imap.host", "imap.gmail.com");
        props.put("mail.imap.port", "993");
        props.put("mail.imap.ssl.enable", "true");

        imapSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public Session getSmtpSession() {
        return smtpSession;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Retourne un Store IMAP connecté (réutilisé si déjà ouvert).
     */
    public Store getStore() throws MessagingException {
        if (store == null || !store.isConnected()) {
            store = imapSession.getStore("imap");
            store.connect(username, password);
        }
        return store;
    }

    public void close() {
        try {
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
