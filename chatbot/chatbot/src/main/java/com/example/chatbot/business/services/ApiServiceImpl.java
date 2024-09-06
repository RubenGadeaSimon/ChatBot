package com.example.chatbot.business.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.chatbot.business.ApiService;
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.math.BigDecimal;

@Service
public class ApiServiceImpl implements ApiService{
    // Find your Account Sid and Token at twilio.com/console
    @Value("${app.twilio.account.ID}")
    private String ACCOUNT_SID;

    @Value("${app.twilio.authtoken}")
    private String AUTH_TOKEN;

    public String customerRequestMessage(){
        return "String devuelto desde el servicio";
    }

    public void requestFromTwilio(String messageReceived) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("whatsapp:+34685571010"),
                        new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
                        messageReceived)
                        .create();
        System.out.println(message.getSid());
    }
}
