package com.java.classification.model;

public class ClassificationRule {
	private String id;
    private String pattern;
    private EmailCategory category;
    private RuleType type;
    
    public enum RuleType {
        SENDER, CONTENT
    }
    
    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPattern() { return pattern; }
    public void setPattern(String pattern) { this.pattern = pattern; }
    public EmailCategory getCategory() { return category; }
    public void setCategory(EmailCategory category) { this.category = category; }
    public RuleType getType() { return type; }
    public void setType(RuleType type) { this.type = type; }

}
