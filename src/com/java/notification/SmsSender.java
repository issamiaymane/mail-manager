package com.java.notification;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {

    private static final String ACCOUNT_SID = "ACaf8b2469a649844dc1f72a49f23fcdc4";
    private static final String AUTH_TOKEN = "158d1b09b09a041b39544f00a74732c95";
    private static final String TWILIO_PHONE_NUMBER = "+15076586441";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static String sendSms(String to, String messageBody) {
        try {
            Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                messageBody
            ).create();

            return "SMS envoyé avec succès ! SID: " + message.getSid();
        } catch (Exception e) {
            e.printStackTrace(); // Pour debug
            return "✗ Erreur SMS: " + e.getMessage();
        }
    }
}

