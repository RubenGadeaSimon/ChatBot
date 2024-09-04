package com.example.chatbot.business.services;

import org.springframework.stereotype.Service;
import com.example.chatbot.business.ApiService;

@Service
public class ApiServiceImpl implements ApiService{
    public String customerRequestMessage(){
        return "String devuelto desde el servicio";
    }
}
