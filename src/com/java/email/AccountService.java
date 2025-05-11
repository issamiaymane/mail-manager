package com.java.email;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

public class AccountService {
    private List<Account> accounts = new ArrayList<>();

    // Ajouter un compte
    public void addAccount(String email, String password, String imapServer, String smtpServer, int imapPort, int smtpPort) {
        Account account = new Account(email, password, imapServer, smtpServer, imapPort, smtpPort);
        accounts.add(account);
    }

    // Supprimer un compte
    public void removeAccount(String email) {
        accounts.removeIf(account -> account.getEmail().equals(email));
    }

    // Mettre à jour un compte (par exemple, pour changer le mot de passe ou les serveurs)
    public void updateAccount(String email, String newPassword, String newImapServer, String newSmtpServer, int newImapPort, int newSmtpPort) {
        for (Account account : accounts) {
            if (account.getEmail().equals(email)) {
                account.setPassword(newPassword);
                account.setImapServer(newImapServer);
                account.setSmtpServer(newSmtpServer);
                account.setImapPort(newImapPort);
                account.setSmtpPort(newSmtpPort);
                break;
            }
        }
    }

    // Obtenir tous les comptes
    public List<Account> getAllAccounts() {
        return accounts;
    }

    public void connectToAllAccounts() {
        for (Account account : accounts) {
            try {
                EmailClient emailClient = new EmailClient(account.getEmail(), account.getPassword());
                emailClient.getSmtpSession();  // Connexion SMTP pour envoyer des emails
                emailClient.getStore();        // Connexion IMAP pour recevoir des emails
            } catch (MessagingException e) {
                e.printStackTrace();  // Gérer l'exception, par exemple en affichant l'erreur dans la console
            }
        }
    }


    // Se connecter à IMAP et SMTP pour un seul compte
    public EmailClient connectToAccount(String email) {
        for (Account account : accounts) {
            if (account.getEmail().equals(email)) {
                try {
                    return new EmailClient(account.getEmail(), account.getPassword());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}

