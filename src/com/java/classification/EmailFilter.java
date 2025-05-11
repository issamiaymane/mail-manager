package com.java.classification;

import com.java.classification.model.EmailCategory;
import com.java.email.Email;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtre les emails par catégorie
 */

public class EmailFilter {
    public List<Email> filterByCategory(List<Email> emails, EmailCategory category) {
        return emails.stream()
                   .filter(email -> email.getCategory() == category)
                   .collect(Collectors.toList());
    }
    
    public List<Email> filterImportantEmails(List<Email> emails) {
        return filterByCategory(emails, EmailCategory.IMPORTANT);
    }

}
