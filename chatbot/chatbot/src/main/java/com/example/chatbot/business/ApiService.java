package com.example.chatbot.business;

public interface ApiService {
    public String customerRequestMessage(String message) throws Exception;
    public void requestFromTwilio(String messageReceived);
}
