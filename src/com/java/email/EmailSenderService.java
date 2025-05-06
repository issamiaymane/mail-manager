package com.java.email;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import java.io.File;
import java.util.List;

public class EmailSenderService {
    private final EmailClient client;

    public EmailSenderService(EmailClient client) {
        this.client = client;
    }

    /**
     * Envoie un email à une liste de destinataires (To + Cc) avec pièces-jointes.
     *
     * @param to           liste d'adresses en TO
     * @param cc           liste d'adresses en CC (peut être null ou vide)
     * @param subject      objet du mail
     * @param body         contenu HTML/texte
     * @param attachments  fichiers à joindre (peut être null)
     */
    public void sendEmail(
            List<String> to,
            List<String> cc,
            String subject,
            String body,
            List<File> attachments
    ) throws MessagingException {
        Message message = new MimeMessage(client.getSmtpSession());
        message.setFrom(new InternetAddress(client.getUsername()));
        message.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(String.join(",", to))
        );
        if (cc != null && !cc.isEmpty()) {
            message.setRecipients(
                Message.RecipientType.CC,
                InternetAddress.parse(String.join(",", cc))
            );
        }
        message.setSubject(subject);

        Multipart multipart = new MimeMultipart();

        // Corps du message
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(body, "text/html; charset=utf-8");
        multipart.addBodyPart(textPart);

        // Pièces jointes
        if (attachments != null) {
            for (File file : attachments) {
                MimeBodyPart attachPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                attachPart.setDataHandler(new DataHandler(source));
                attachPart.setFileName(file.getName());
                multipart.addBodyPart(attachPart);
            }
        }

        message.setContent(multipart);
        Transport.send(message);
    }
}
