package com.java.mailinglist;

import java.util.ArrayList;
import java.util.List;

public class Mailinglist {

    private String listName;
    private List<String> emails;

    public Mailinglist(String listName) {
        this.listName = listName;
        this.emails = new ArrayList<>();
    }

    // Get the name of the mailing list
    public String getListName() {
        return listName;
    }

    // Add an email to the mailing list
    public void addEmail(String email) {
        if (!emails.contains(email)) {
            emails.add(email);
            System.out.println("Email added to the list: " + email);
        } else {
            System.out.println("Email already exists in the list.");
        }
    }

    // Remove an email from the mailing list
    public void removeEmail(String email) {
        if (emails.contains(email)) {
            emails.remove(email);
            System.out.println("Email removed from the list: " + email);
        } else {
            System.out.println("Email not found in the list.");
        }
    }

    // Get all emails in the mailing list
    public List<String> getEmails() {
        return emails;
    }

    // Display the mailing list
    public void showMailingList() {
        System.out.println("Mailing List: " + listName);
        for (String email : emails) {
            System.out.println(email);
        }
    }
}
