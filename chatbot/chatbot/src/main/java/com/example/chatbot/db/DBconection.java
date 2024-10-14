package com.example.chatbot.db;
import com.example.chatbot.business.services.ApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import com.example.chatbot.business.ApiService;

public class DBconection {
    // Token de seguridad. Invalidar al acabar
    private static final String TOKEN = "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";



    // Método que se conecta a la base de datos y accede a la tabla cliente
    public String connectToDatabase(String message) {
        String response="";
        // Configuración de la conexión
        String url = "jdbc:postgresql://172.30.9.121:30625/mydb";  // URL de la base de datos
        String user = "postgres";                             // Usuario de la base de datos
        String password = "postgres";                    // Contraseña del usuario

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Establecer la conexión
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa a la base de datos PostgreSQL.");

            // Crear una consulta a la tabla cliente
            stmt = conn.createStatement();
            String query = message;
            rs = stmt.executeQuery(query);

            // Procesar los resultados de la consulta
            while (rs.next()) {
                // Iterar sobre el número de columnas en el ResultSet
                int columnCount = rs.getMetaData().getColumnCount();
                StringBuilder row = new StringBuilder();

                for (int i = 1; i <= columnCount; i++) {
                    // Obtener el valor de la columna actual
                    Object value = rs.getObject(i);
                    // Añadir el valor al StringBuilder, separado por un espacio
                    row.append(value).append(" ");
                }

                // Imprimir la fila completa
                System.out.println(row.toString().trim());  // .trim() elimina el espacio extra al final
                response += row.toString().trim() + "\n";
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar la conexión y liberar recursos
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return response;
    }



}

