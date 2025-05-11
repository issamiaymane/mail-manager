package com.java.classification.strategy;

import com.java.classification.model.ClassificationRule;
import com.java.classification.model.EmailCategory;
import java.util.List;


public class SenderClassificationStrategy {
    private final List<ClassificationRule> senderRules;
    
    public SenderClassificationStrategy(List<ClassificationRule> senderRules) {
        this.senderRules = senderRules;
    }
    
    public EmailCategory classify(String sender) {
        for (ClassificationRule rule : senderRules) {
            if (sender.contains(rule.getPattern())) {
                return rule.getCategory();
            }
        }
        return EmailCategory.OTHER;
    }

}
