package com.java.test;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.*;
import java.io.*;

public class EmailService {
    private Session session;
    private Store store;
    public String username;
    private String password;
    private List<Message> messages;

    public EmailService(String username, String password) throws MessagingException {
        this.username = username;
        this.password = password;
        configureSession();
    }

    private void configureSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Optional: timeouts
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendEmail(String to, String subject, String body, List<String> attachments)
            throws MessagingException {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(body, "text/html; charset=utf-8"); // ✅ Better MIME
            multipart.addBodyPart(textPart);

            if (attachments != null && !attachments.isEmpty()) {
                for (String filePath : attachments) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    try {
                        DataSource source = new FileDataSource(filePath);
                        attachmentPart.setDataHandler(new DataHandler(source));
                        attachmentPart.setFileName(new File(filePath).getName());
                        multipart.addBodyPart(attachmentPart);
                    } catch (Exception e) {
                        throw new MessagingException("Error attaching file: " + filePath, e);
                    }
                }
            }

            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new MessagingException("Error sending email", e);
        }
    }

    public List<Message> receiveEmails() throws MessagingException {
    messages = new ArrayList<>();

    try {
        Properties imapProps = new Properties();
        imapProps.put("mail.imap.host", "imap.gmail.com");
        imapProps.put("mail.imap.port", "993");
        imapProps.put("mail.imap.ssl.enable", "true");

        Session imapSession = Session.getInstance(imapProps,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        store = imapSession.getStore("imap");
        store.connect("imap.gmail.com", username, password);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] msgs = inbox.getMessages();
        Arrays.sort(msgs, (m1, m2) -> {
            try {
                return m2.getSentDate().compareTo(m1.getSentDate()); // 🔁 Newest first
            } catch (MessagingException e) {
                return 0;
            }
        });

        messages = Arrays.asList(msgs);
    } catch (Exception e) {
        throw new MessagingException("Error receiving emails", e);
    }

    return messages;
}


    public Message getMessageAt(int index) {
        if (messages == null || index < 0 || index >= messages.size()) {
            throw new IndexOutOfBoundsException("Invalid message index");
        }
        return this.messages.get(index);
    }

    public static String getTextFromMessage(Message message) {
        // TODO: implement properly based on content type (e.g. text/plain, text/html)
        try {
            Object content = message.getContent();
            if (content instanceof String) {
                return (String) content;
            } else if (content instanceof Multipart) {
                Multipart multipart = (Multipart) content;
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart part = multipart.getBodyPart(i);
                    if (part.isMimeType("text/plain")) {
                        return part.getContent().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<File> getAttachmentsFromMessage(Message message) {
        List<File> attachments = new ArrayList<>();
        try {
            Object content = message.getContent();
            if (content instanceof Multipart) {
                Multipart multipart = (Multipart) content;

                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart part = multipart.getBodyPart(i);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        InputStream is = part.getInputStream();
                        File file = File.createTempFile("email_attach_", part.getFileName());
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buf = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buf)) != -1) {
                            fos.write(buf, 0, bytesRead);
                        }
                        fos.close();
                        attachments.add(file);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attachments;
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

