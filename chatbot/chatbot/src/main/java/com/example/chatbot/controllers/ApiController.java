package com.example.chatbot.controllers;

import com.example.chatbot.business.ApiService;
import com.example.chatbot.db.DBconection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.example.chatbot.db.DBconection;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/gpt")
public class ApiController {

    @Autowired
    ApiService apiService;

    private DBconection dBconection = new DBconection();

    @PostMapping("/customer-request")
    public ResponseEntity<?> handleCustomerRequest(@RequestParam Map<String, String> params) {
        Map<String,String> map = new HashMap<>();
        String messageFromAI ="";
        String responseMessage = "";
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

            map = apiService.processMessage(message);
            // Suponiendo que apiService.customerRequestMessage() y apiService.requestFromTwilio() están definidos en tu servicio
            System.out.println("resultado del map en controller: " + map.get("resultado"));
            messageFromAI = apiService.customerRequestMessage(map.get("resultado"));
            System.out.println("mensaje ia en controller : " + messageFromAI);
            if(map.get("tipo").equals("DB")){
                System.out.println("el tipo es BD");
                responseMessage = dBconection.connectToDatabase(messageFromAI);
            } else if (map.get("tipo").equals("GIT")) {
                System.out.println("el tipo es GIT");
                responseMessage = apiService.connectToGithub(messageFromAI);
            }
            System.out.println("responsemessage en controller: " + responseMessage);
            apiService.requestFromTwilio(responseMessage);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Excepcion: " + e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
        return ResponseEntity.ok("El mensaje es: " + messageFromAI);
    }

    @GetMapping("/holamundo")
    public ResponseEntity<?> holamundo() {
        System.out.println("hola mundo");
        return ResponseEntity.status(200).body("Hola mundo");
    }

    @GetMapping("/holaollama")
    public ResponseEntity<?> holaollama() {
        System.out.println("hola ollama");
        String resp = "";
        try{
            resp = apiService.holaollama();

        }catch(Exception e){
            System.out.println("error ollama: " + e);
        }
        return ResponseEntity.status(200).body(resp);
    }

    @PostMapping("/doquery")
    public ResponseEntity<?> doquery(@RequestBody Map<String, String> params) {
        System.out.println("Query en proceso...");
        String resp = "";
        try{
            // Mostrar el JSON en la consola
            //System.out.println("Received parameters: " + params);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("Key: " + key + ", Value: " + value + ", Type of Value: " + value.getClass().getName());
            }
            // Extraer el mensaje del parámetrooo
            String message = params.get("query");

            dBconection.connectToDatabase(message);

        }catch(Exception e){
            System.out.println("error ollama: " + e);
        }
        return ResponseEntity.status(200).body(resp);
    }


}
