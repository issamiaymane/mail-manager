package com.java.model.email;

import java.io.File;

public class EmailAttachment {
    private String name;
    private File file;
    private String mimeType;

    public EmailAttachment(String name, File file, String mimeType) {
        this.name = name;
        this.file = file;
        this.mimeType = mimeType;
    }

    // Getters
    public String getName() { return name; }
    public File getFile() { return file; }
    public String getMimeType() { return mimeType; }
}