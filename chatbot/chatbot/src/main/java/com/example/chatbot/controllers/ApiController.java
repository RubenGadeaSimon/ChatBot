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
    public ResponseEntity<?> handleCustomerRequest(@RequestParam Map<String, String> params) {
        try {
            // Mostrar el JSON en la consola
            //System.out.println("Received parameters: " + params);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("Key: " + key + ", Value: " + value + ", Type of Value: " + value.getClass().getName());
            }

            // Extraer el mensaje del parámetro
            String message = params.get("Body"); // Cambia "prompt" por el nombre del campo que esperas
            System.out.println("Extracted message: " + message);

            // Suponiendo que apiService.customerRequestMessage() y apiService.requestFromTwilio() están definidos en tu servicio
            String messageFromAI = apiService.customerRequestMessage(message);
            apiService.requestFromTwilio(messageFromAI);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Excepcion: " + e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
        return ResponseEntity.ok("El mensaje es: " + params);
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
