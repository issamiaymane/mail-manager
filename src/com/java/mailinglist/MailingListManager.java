package com.java.mailinglist;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages multiple named mailing lists.
 */
public class MailingListManager {

    private final Map<String, Mailinglist> mailingLists;

    public MailingListManager() {
        this.mailingLists = new HashMap<>();
    }

    // Create a new mailing list
    public void createMailingList(String name) {
        if (!mailingLists.containsKey(name)) {
            mailingLists.put(name, new Mailinglist(name));
            System.out.println("✅ Mailing list '" + name + "' created.");
        } else {
            System.out.println("⚠️ Mailing list '" + name + "' already exists.");
        }
    }

    // Add an email to a specific mailing list
    public void addEmailToList(String listName, String email) {
        Mailinglist list = mailingLists.get(listName);
        if (list != null) {
            list.addEmail(email);
        } else {
            System.out.println("⚠️ Mailing list '" + listName + "' not found.");
        }
    }

    // Remove an email from a specific list
    public void removeEmailFromList(String listName, String email) {
        Mailinglist list = mailingLists.get(listName);
        if (list != null) {
            list.removeEmail(email);
        } else {
            System.out.println("⚠️ Mailing list '" + listName + "' not found.");
        }
    }

    // Show all mailing lists and their contents
    public void showAllMailingLists() {
        for (Mailinglist list : mailingLists.values()) {
            list.showMailingList();
        }
    }

    // Get emails from a specific list
    public Mailinglist getMailingList(String listName) {
        return mailingLists.get(listName);
    }

    // Get all emails from a specific list (as List<String>)
    public java.util.List<String> getEmailsFromList(String listName) {
        Mailinglist list = mailingLists.get(listName);
        return (list != null) ? list.getEmails() : new java.util.ArrayList<>();
    }
}
