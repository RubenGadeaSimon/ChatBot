package com.example.chatbot.controllers;

import com.example.chatbot.business.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/gpt")
public class ApiController {

    @Autowired
    ApiService apiService;

    @PostMapping("/customer-request")
    public ResponseEntity<?> handleCustomerRequest(@RequestBody Map<String, Object> requestBody) {
        String message = "";
        try{
            System.out.println("Received JSON: " + requestBody);
            message = "aux";
            String messageFromAI = apiService.customerRequestMessage(message);
            apiService.requestFromTwilio(messageFromAI);

        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        return ResponseEntity.status(200).body("El mensaje es: " + message);
    }

    @GetMapping("/holamundo")
    public ResponseEntity<?> holamundo() {
        System.out.println("hola mundo");
        return ResponseEntity.status(200).body("Hola mundo");
    }

    @PostMapping("/holamundo")
    public ResponseEntity<?> holamundo2() {
        System.out.println("hola mundo2");
        return ResponseEntity.status(200).body("Hola mundo");
    }


}
