package com.java.mailinglist;

import com.java.email.EmailSenderService;
import com.java.email.EmailClient;

import javax.mail.MessagingException;
import java.io.File;
import java.util.List;

/**
 * Sends bulk emails to all recipients in a mailing list.
 */
public class BulkEmailSender {

    private EmailSenderService emailSender;

    public BulkEmailSender(EmailClient client) {
        this.emailSender = new EmailSenderService(client);
    }

    /**
     * Envoie un email en masse à tous les destinataires de la mailing list.
     *
     * @param subject       objet du mail
     * @param body          corps du message
     * @param attachments   liste de fichiers joints (peut être null)
     * @param mailingList   le nom de la mailing list (pour retrouver la liste des destinataires)
     */
    public void sendBulkEmail(
            String subject,
            String body,
            List<File> attachments,
            MailingListManager mailingListManager,   // Pass manager to access the lists
            String listName                           // Name of the mailing list
    ) {
        Mailinglist mailingList = mailingListManager.getMailingList(listName); // Retrieve the specific list

        if (mailingList == null) {
            System.out.println("⚠️ Mailing list '" + listName + "' not found.");
            return;
        }

        List<String> recipients = mailingList.getEmails(); // Get emails from the list

        for (String to : recipients) {
            try {
                emailSender.sendEmail(
                    List.of(to),           // TO
                    null,                  // CC
                    subject,
                    body,
                    attachments
                );
                System.out.println("✅ Email sent to: " + to);
            } catch (MessagingException e) {
                System.err.println("❌ Failed to send email to: " + to);
                e.printStackTrace();
            }
        }
    }
}
