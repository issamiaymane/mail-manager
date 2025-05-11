package com.java.email;

public class Account {
    private String email;
    private String password;
    private String imapServer;
    private String smtpServer;
    private int imapPort;
    private int smtpPort;

    // Constructeur
    public Account(String email, String password, String imapServer, String smtpServer, int imapPort, int smtpPort) {
        this.email = email;
        this.password = password;
        this.imapServer = imapServer;
        this.smtpServer = smtpServer;
        this.imapPort = imapPort;
        this.smtpPort = smtpPort;
    }

    // Getters et Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImapServer() {
        return imapServer;
    }

    public void setImapServer(String imapServer) {
        this.imapServer = imapServer;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public int getImapPort() {
        return imapPort;
    }

    public void setImapPort(int imapPort) {
        this.imapPort = imapPort;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }
}

