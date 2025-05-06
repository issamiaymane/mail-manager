package com.java.email;

import javax.mail.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Service de réception des emails : récupère, trie (newest first),
 * extrait texte et pièces-jointes.
 */
public class EmailReceiverService {
    private final EmailClient client;
    private List<Message> messages;

    public EmailReceiverService(EmailClient client) {
        this.client = client;
    }

    /**
     * Récupère tous les messages du dossier INBOX, triés par date décroissante.
     */
    public List<Message> receiveEmails() throws MessagingException {
        return receiveEmails("INBOX");
    }

    /**
     * Récupère les messages d’un dossier IMAP donné (triés par date décroissante).
     *
     * @param folderName nom du dossier (ex. "INBOX", "Sent", etc.)
     */
    public List<Message> receiveEmails(String folderName) throws MessagingException {
        Store store = client.getStore();
        Folder folder = store.getFolder(folderName);
        folder.open(Folder.READ_ONLY);

        Message[] msgs = folder.getMessages();

        // Tri explicite des Message[] : plus récent d'abord
        Arrays.sort(msgs, new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                try {
                    Date d1 = m1.getSentDate();
                    Date d2 = m2.getSentDate();
                    if (d1 == null && d2 == null) return 0;
                    if (d1 == null)             return 1;
                    if (d2 == null)             return -1;
                    // ordre inverse pour newest first
                    return d2.compareTo(d1);
                } catch (MessagingException e) {
                    // en cas d’erreur, on ne change pas l’ordre
                    return 0;
                }
            }
        });

        messages = Arrays.asList(msgs);
        return messages;
    }

    /**
     * Récupère un message par index dans la liste précédemment chargée.
     */
    public Message getMessage(int index) {
        if (messages == null || index < 0 || index >= messages.size()) {
            throw new IndexOutOfBoundsException("Invalid message index");
        }
        return messages.get(index);
    }

    /**
     * Extrait le texte (plain) d’un Message (ou la première partie text/plain).
     */
    public static String extractText(Message message) {
        try {
            Object content = message.getContent();
            if (content instanceof String) {
                return (String) content;
            } else if (content instanceof Multipart) {
                Multipart mp = (Multipart) content;
                for (int i = 0; i < mp.getCount(); i++) {
                    BodyPart bp = mp.getBodyPart(i);
                    if (bp.isMimeType("text/plain")) {
                        return bp.getContent().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Sauvegarde les pièces jointes dans des fichiers temporaires
     * et retourne la liste de ces fichiers.
     */
    public static List<File> extractAttachments(Message message) {
        List<File> attachments = new ArrayList<>();
        try {
            if (message.getContent() instanceof Multipart) {
                Multipart mp = (Multipart) message.getContent();
                for (int i = 0; i < mp.getCount(); i++) {
                    BodyPart bp = mp.getBodyPart(i);
                    if (Part.ATTACHMENT.equalsIgnoreCase(bp.getDisposition())) {
                        InputStream is = bp.getInputStream();
                        File tmp = File.createTempFile("attach_", bp.getFileName());
                        try (FileOutputStream fos = new FileOutputStream(tmp)) {
                            byte[] buffer = new byte[4096];
                            int len;
                            while ((len = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, len);
                            }
                        }
                        attachments.add(tmp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attachments;
    }
}
