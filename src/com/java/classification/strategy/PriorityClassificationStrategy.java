package com.java.classification.strategy;

import com.java.classification.model.EmailCategory;

public class PriorityClassificationStrategy {
    private static final String[] PRIORITY_KEYWORDS = {"urgent", "important", "asap"};
    
    public EmailCategory classify(String subject, String body) {
        String content = (subject + " " + body).toLowerCase();
        
        for (String keyword : PRIORITY_KEYWORDS) {
            if (content.contains(keyword)) {
                return EmailCategory.IMPORTANT;
            }
        }
        return null; // Null signifie pas de priorité détectée
    }

}
