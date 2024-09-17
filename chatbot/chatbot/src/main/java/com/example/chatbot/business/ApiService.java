package com.example.chatbot.business;

import java.util.Map;

public interface ApiService {
    public String customerRequestMessage(String message) throws Exception;
    public void requestFromTwilio(String messageReceived);
    public String holaollama() throws Exception;
    public Map<String,String> processMessage(String message);
    public String connectToGithub(String message);
}
