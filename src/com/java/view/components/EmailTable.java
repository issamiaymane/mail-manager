package com.java.view.components;

import com.java.model.email.EmailMessage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class EmailTable extends JTable {
    private final DefaultTableModel model;

    public EmailTable() {
        super(new DefaultTableModel(new Object[]{"From", "Subject", "Date"}, 0));
        this.model = (DefaultTableModel) getModel();
        setRowHeight(30);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void refresh(List<EmailMessage> emails) {
        model.setRowCount(0);
        for (EmailMessage email : emails) {
            model.addRow(new Object[]{
                email.getFrom(),
                email.getSubject(),
                email.getSentDate()
            });
        }
    }
}