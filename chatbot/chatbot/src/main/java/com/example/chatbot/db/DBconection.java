package com.example.chatbot.db;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class DBconection {

    // Método que se conecta a la base de datos y accede a la tabla cliente
    public String connectToDatabase(String message) {
        String response="";
        // Configuración de la conexión
        String url = "jdbc:postgresql://localhost:5432/mydb";  // URL de la base de datos
        String user = "postgres";                             // Usuario de la base de datos
        String password = "";                    // Contraseña del usuario

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
                String dni = rs.getString("dni");
                String nombre = rs.getString("nombre");
                String salario = rs.getString("salario");

                System.out.println("DNI: " + dni + ", Nombre: " + nombre + ", Salario: " + salario);
                response += "DNI: " + dni + ", Nombre: " + nombre + ", Salario: " + salario + "\n";
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

