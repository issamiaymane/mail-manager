package com.java.utils;

import com.java.model.email.EmailAttachment;
import javax.mail.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmailUtils {
    public static List<EmailAttachment> getAttachments(Message message) throws Exception {
        List<EmailAttachment> attachments = new ArrayList<>();
        Object content = message.getContent();
        
        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    attachments.add(processAttachment(part));
                }
            }
        }
        return attachments;
    }

    private static EmailAttachment processAttachment(BodyPart part) throws Exception {
        InputStream is = part.getInputStream();
        File file = File.createTempFile("email_attach_", part.getFileName());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buf = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buf)) != -1) {
                fos.write(buf, 0, bytesRead);
            }
        }
        return new EmailAttachment(part.getFileName(), file, part.getContentType());
    }
}