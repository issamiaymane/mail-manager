package com.java.classification.strategy;

import com.java.classification.model.ClassificationRule;
import com.java.classification.model.EmailCategory;
import java.util.List;

public class ContentClassificationStrategy {
private final List<ClassificationRule> contentRules;
    
    public ContentClassificationStrategy(List<ClassificationRule> contentRules) {
        this.contentRules = contentRules;
    }
    
    public EmailCategory classify(String subject, String body) {
        String content = (subject + " " + body).toLowerCase();
        
        for (ClassificationRule rule : contentRules) {
            if (content.contains(rule.getPattern().toLowerCase())) {
                return rule.getCategory();
            }
        }
        return EmailCategory.OTHER;
    }

}
