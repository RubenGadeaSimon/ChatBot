package com.example.chatbot.business;

public interface ApiService {
    public String customerRequestMessage();
    public void requestFromTwilio(String messageReceived);
}
