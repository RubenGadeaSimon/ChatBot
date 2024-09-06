package com.example.chatbot.controllers;

import com.example.chatbot.business.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/gpt")
public class ApiController {

    @Autowired
    ApiService apiService;

    @PostMapping("/customer-request")
    public ResponseEntity<?> handleCustomerRequest(@RequestBody String message) {
        message = apiService.customerRequestMessage();
        apiService.requestFromTwilio(message);
        return ResponseEntity.status(200).body("El mensaje es: " + message);
        //llamada a servicio
        //llamar a metodo con post a gpt
        //devuelve al svc el contenido de la respuesta
        //el servicio devuelve aqui el mensaje y se devuelve en responseentity
    }

    @GetMapping("/holamundo")
    public ResponseEntity<?> holamundo(@RequestBody String message) {
        return ResponseEntity.status(200).body("Hola mundo");
    }

}
