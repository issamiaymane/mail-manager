package com.java.classification;

import com.java.classification.model.EmailCategory;
import com.java.classification.strategy.*;
import com.java.classification.model.ClassificationRule;
import java.util.List;

public class EmailClassifier {
    private final SenderClassificationStrategy senderStrategy;
    private final ContentClassificationStrategy contentStrategy;
    private final PriorityClassificationStrategy priorityStrategy;
    
    public EmailClassifier(List<ClassificationRule> senderRules, 
                         List<ClassificationRule> contentRules) {
        this.senderStrategy = new SenderClassificationStrategy(senderRules);
        this.contentStrategy = new ContentClassificationStrategy(contentRules);
        this.priorityStrategy = new PriorityClassificationStrategy();
}
    public EmailCategory classify(String sender, String subject, String body) {
        // 1. Vérifier d'abord la priorité
        EmailCategory priorityCat = priorityStrategy.classify(subject, body);
        if (priorityCat != null) {
            return priorityCat;
        }
        
        // 2. Vérifier l'expéditeur
        EmailCategory senderCat = senderStrategy.classify(sender);
        if (senderCat != EmailCategory.OTHER) {
            return senderCat;
        }
        
        // 3. Vérifier le contenu
        return contentStrategy.classify(subject, body);
    }
}

