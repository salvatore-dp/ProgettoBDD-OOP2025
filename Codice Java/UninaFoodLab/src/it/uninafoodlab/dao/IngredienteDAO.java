package it.uninafoodlab.dao;

import it.uninafoodlab.model.Ingrediente;
import it.uninafoodlab.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IngredienteDAO {

    // Inserimento
    public boolean insertIngrediente(Ingrediente ingrediente) {
        String sql = "INSERT INTO Ingrediente(nome_ingrediente, descrizione_ingrediente) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ingrediente.getNomeIngrediente());
            stmt.setString(2, ingrediente.getDescrizioneIngrediente());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Recupero per PK
    public Ingrediente getIngredienteByNome(String nomeIngrediente) {
        String sql = "SELECT * FROM Ingrediente WHERE nome_ingrediente = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeIngrediente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Ingrediente(
                        rs.getString("nome_ingrediente"),
                        rs.getString("descrizione_ingrediente")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Ritorna la lista di tutti gli ingredienti presenti nel database.
     * @return Lista di Ingrediente
     */
    public List<Ingrediente> getAllIngredienti() {
        List<Ingrediente> ingredienti = new ArrayList<>();
        String sql = "SELECT * FROM Ingrediente ORDER BY nome_ingrediente";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ingredienti.add(new Ingrediente(
                        rs.getString("nome_ingrediente"),
                        rs.getString("descrizione_ingrediente")
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura di tutti gli ingredienti!");
            e.printStackTrace();
        }
        return ingredienti;
    }

    // Aggiornamento
    public boolean updateIngrediente(Ingrediente ingrediente) {
        String sql = "UPDATE Ingrediente SET descrizione_ingrediente = ? WHERE nome_ingrediente = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ingrediente.getDescrizioneIngrediente());
            stmt.setString(2, ingrediente.getNomeIngrediente());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cancellazione
    public boolean deleteIngrediente(String nomeIngrediente) {
        String sql = "DELETE FROM Ingrediente WHERE nome_ingrediente = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeIngrediente);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}