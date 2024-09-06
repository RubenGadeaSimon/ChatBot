package com.example.chatbot.business.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.chatbot.business.ApiService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpRequest.BodyPublishers;

@Service
public class ApiServiceImpl implements ApiService{
    // Find your Account Sid and Token at twilio.com/console
    @Value("${app.twilio.account.ID}")
    private String ACCOUNT_SID;

    @Value("${app.twilio.authtoken}")
    private String AUTH_TOKEN;

    public String customerRequestMessage2(){
        return "String devuelto desde el servicio";
    }

    public String customerRequestMessage(String message) throws Exception {
        // URL del servicio REST
        String url = "http://localhost:11434/api/generate";

        // Cuerpo de la petición en formato JSON
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("model", "llama3.1:latest");
        jsonObject.put("prompt", message);
        jsonObject.put("system", "");
        jsonObject.put("stream", false);

        // Convertir el objeto JSON a cadena
        String requestBody = jsonObject.toString();

        // Crear cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Construir la petición HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))                        // URI del servicio
                .header("Content-Type", "application/x-www-form-urlencoded")  // Cabecera indicando que el cuerpo es JSON
                .POST(BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))  // Método POST con el cuerpo de la petición
                .build();

        // Enviar la petición y recibir la respuesta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parsear la respuesta JSON utilizando la clase JSONObject de org.json
        JSONObject jsonResponse = new JSONObject(response.body());

        // Extraer el valor del campo "response" del JSON
        String responseMessage = jsonResponse.getString("response");

        // Retornar el mensaje de la respuesta
        return responseMessage;
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
