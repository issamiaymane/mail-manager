package com.java.classification.model;



public enum EmailCategory {
    WORK("Travail"),
    PERSONAL("Personnel"),
    NEWSLETTER("Newsletter"),
    IMPORTANT("Important"),
    SPAM("Spam"),
    OTHER("Autres");
    
    private final String displayName;
    
    EmailCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
