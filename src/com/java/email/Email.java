package com.java.email;


import com.java.classification.model.EmailCategory;
import java.util.List;

/**
 * Classe représentant un email avec ses propriétés principales
 */
public class Email {
    private String id;
    private String from;          // Expéditeur
    private List<String> to;      // Destinataires
    private String subject;       // Sujet
    private String body;          // Corps du message
    private EmailCategory category; // Catégorie (déterminée par le classificateur)
    private List<String> attachments; // Liste des pièces jointes

    // Constructeurs
    public Email() {}

    public Email(String from, String subject, String body) {
        this.from = from;
        this.subject = subject;
        this.body = body;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public List<String> getTo() { return to; }
    public void setTo(List<String> to) { this.to = to; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public EmailCategory getCategory() { return category; }
    public void setCategory(EmailCategory category) { this.category = category; }

    public List<String> getAttachments() { return attachments; }
    public void setAttachments(List<String> attachments) { this.attachments = attachments; }
}