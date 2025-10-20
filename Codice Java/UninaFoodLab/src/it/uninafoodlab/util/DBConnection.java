package it.uninafoodlab.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Parametri di connessione
    private static final String URL = "jdbc:postgresql://localhost:5432/UninaFoodLab";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    // Carichiamo il driver una sola volta quando la classe viene caricata
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver PostgreSQL non trovato!");
            e.printStackTrace();
        }
    }

    // Costruttore privato per evitare istanziazioni
    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        System.out.println("Nuova connessione al database richiesta...");
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Connessione stabilita!");
        return conn;
    }
    
}