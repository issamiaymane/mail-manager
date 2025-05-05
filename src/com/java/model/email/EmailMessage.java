package com.java.model.email;

import javax.mail.Message;
import javax.mail.Multipart;

import com.java.utils.EmailUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmailMessage {
    private String from;
    private String subject;
    private Date sentDate;
    private String content;
    private List<EmailAttachment> attachments;
    private Message originalMessage;

    public EmailMessage(Message message) throws Exception {
        this.originalMessage = message;
        this.from = message.getFrom()[0].toString();
        this.subject = message.getSubject();
        this.sentDate = message.getSentDate();
        this.content = getTextFromMessage(message);
        this.attachments = EmailUtils.getAttachments(message);
    }

    private String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                if (multipart.getBodyPart(i).isMimeType("text/plain")) {
                    return multipart.getBodyPart(i).getContent().toString();
                }
            }
        }
        return "";
    }

    // Getters
    public String getFrom() { return from; }
    public String getSubject() { return subject; }
    public Date getSentDate() { return sentDate; }
    public String getContent() { return content; }
    public List<EmailAttachment> getAttachments() { return attachments; }
}