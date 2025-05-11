package com.java.notification;

public class SMSNotifier {

    public String sendTestSms(String phoneNumber) {
        String message = "Ceci est un test de notification SMS depuis MailManager.";
        return SmsSender.sendSms(phoneNumber, message);
    }

    public String sendCustomSms(String phoneNumber, String messageBody) {
        return SmsSender.sendSms(phoneNumber, messageBody);
    }
}

