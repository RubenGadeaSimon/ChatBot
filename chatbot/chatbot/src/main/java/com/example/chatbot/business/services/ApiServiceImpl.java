package com.example.chatbot.business.services;

import com.example.chatbot.db.DBconection;
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
import java.util.HashMap;
import java.util.Map;

@Service
public class ApiServiceImpl implements ApiService{
    private DBconection dBconection = new DBconection();
    // Find your Account Sid and Token at twilio.com/console
    @Value("${app.twilio.account.ID}")
    private String ACCOUNT_SID;

    @Value("${app.twilio.authtoken}")
    private String AUTH_TOKEN;

    private static final String TOKEN = "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";


    // URL del servicio REST
    //@Value("${ollama.url}")
    //private String url = "http://ollama.paco-namespace.svc.cluster.local:11434/api/generate";
    private String url = "http://localhost:11434/api/generate";
    private String urlHola= "http://ollama.paco-namespace.svc.cluster.local:11434";

    public String formatJson(String jsonResponse) {
        // Limitar el tamaño del JSON a 500 caracteres
        if (jsonResponse.length() > 500) {
            jsonResponse = jsonResponse.substring(0, 500);
        }

        // Eliminar llaves y comillas
        String formatted = jsonResponse.replace("{", "")
                .replace("}", "")
                .replace("\"", "");

        // Reemplazar comas por saltos de línea
        formatted = formatted.replace(",", "\n");

        // Añadir negrita para las palabras a la izquierda de ":" excepto en URLs
        String[] lines = formatted.split("\n");
        StringBuilder finalOutput = new StringBuilder();

        for (String line : lines) {
            // Dividir la línea en la palabra antes de ":" y el valor
            if (line.contains(":")) {
                String[] parts = line.split(":", 2);  // Dividir solo en el primer ":"
                String field = parts[0].trim();       // La palabra antes de ":"
                String value = parts.length > 1 ? parts[1].trim() : ""; // El valor después de ":"

                // Evitar aplicar negrita a las URLs
                if (value.startsWith("http")) {
                    finalOutput.append(field).append(": ").append(value).append("\n");
                } else {
                    // Formatear la palabra antes de ":" en negrita
                    finalOutput.append("*").append(field).append(":* ").append(value).append("\n");
                }
            } else {
                finalOutput.append(line).append("\n");
            }
        }

        return finalOutput.toString().trim(); // Eliminar espacios extra al final
    }




    public String customerRequestMessage2(){
        return "String devuelto desde el servicio";
    }

    public String customerRequestMessage(String message) throws Exception {
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
                .header("Content-Type", "application/json")  // Cabecera indicando que el cuerpo es JSON
                .POST(BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))  // Método POST con el cuerpo de la petición
                .build();
        // Enviar la petición y recibir la respuesta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("response: " + response.body());

        // Parsear la respuesta JSON utilizando la clase JSONObject de org.json
        JSONObject jsonResponse = new JSONObject(response.body());
        // Extraer el valor del campo "response" del JSON
        String responseMessage = jsonResponse.getString("response");
        System.out.println("Respuesta IA:" + responseMessage);

        // Retornar el mensaje de la respuesta de la IA
        return responseMessage;
    }

    public void requestFromTwilio(String messageReceived) {
        System.out.println("Entra a twilio method");
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("whatsapp:+34685571010"),
                        new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
                        messageReceived)
                        .create();
        System.out.println(message.getSid());
    }

    public String holaollama() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlHola))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response Code: " + response.statusCode());
        System.out.println(response.body());
        return response.body();
    }

    public Map<String,String> processMessage(String message){//resultado,tipo
        String result = "";
        String type = "";
        Map<String,String> map = new HashMap<>();


        if (message.startsWith("DB")) {
            result = "Quiero que contestes solo con una query sin texto adicional: " + message;
            type = "DB";
        } else if (message.startsWith("JSON")) {
            result = "Contesta solo con un JSON: " + message;
            type="JSON";
        } else if (message.startsWith("GIT")) {
            result = "Contesta a esto solo proporcionando el metodo y url necesarios(con https://) (solo eparados por una coma) para realizar la siguiente peticion a la API de github, supon que tengo ya acceso con un token autorizado. No quiero query params en la url El formato es: <METODO>,<URL>  No devuelvas nada mas que se salga del formato. : " + message;
            type = "GIT";
        }

        map.put("resultado", result);
        map.put("tipo", type);
        System.out.println("resultsfo en process: " + map.get("resultado"));
        System.out.println("tipo en process: " + map.get("tipo"));

        return map;
    }

    public String connectToGithub(String message){//metodo:<METODO>,url:<URL>
        String response="";
        String [] partes = message.split(",");
        String metodo = partes[0];
        String url = partes[1];

        System.out.println("metodo: " + metodo);
        System.out.println("url:" + url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer" + TOKEN)
                .method(metodo.toUpperCase(), HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> responsehttp = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("response: " + responsehttp.body());
            response = responsehttp.body();
        } catch (Exception e) {
            e.printStackTrace();
            response = "Ocurrió un error al conectar con GitHub.";
        }
        response = formatJson(response);
        return response;
    }
}

// Hola
