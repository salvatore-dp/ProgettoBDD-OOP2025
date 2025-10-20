package it.uninafoodlab.dao;

import it.uninafoodlab.model.Chef;
import it.uninafoodlab.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChefDAO {

    // CREATE: aggiunge un nuovo chef al DB
    public boolean insertChef(Chef chef) {
        String sql = "INSERT INTO Chef(codice_fiscale, nome, cognome, email, pass_hash) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, chef.getCodiceFiscale());
            stmt.setString(2, chef.getNome());
            stmt.setString(3, chef.getCognome());
            stmt.setString(4, chef.getEmail());
            stmt.setString(5, chef.getPassHash());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nell'inserimento dello chef!");
            e.printStackTrace();
            return false;
        }
    }

    // READ: cerca uno chef per codice fiscale
    public Chef getChefByCodiceFiscale(String codiceFiscale) {
        String sql = "SELECT * FROM Chef WHERE codice_fiscale = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codiceFiscale);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Chef(
                        rs.getString("codice_fiscale"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("pass_hash")
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura dello chef!");
            e.printStackTrace();
        }
        return null;
    }

    // READ: ritorna tutti gli chef
    public List<Chef> getAllChefs() {
        List<Chef> chefs = new ArrayList<>();
        String sql = "SELECT * FROM Chef";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                chefs.add(new Chef(
                        rs.getString("codice_fiscale"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("pass_hash")
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura di tutti gli chef!");
            e.printStackTrace();
        }

        return chefs;
    }

    // UPDATE: aggiorna i dati di uno chef
    public boolean updateChef(Chef chef) {
        String sql = "UPDATE Chef SET nome = ?, cognome = ?, email = ?, pass_hash = ? WHERE codice_fiscale = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, chef.getNome());
            stmt.setString(2, chef.getCognome());
            stmt.setString(3, chef.getEmail());
            stmt.setString(4, chef.getPassHash());
            stmt.setString(5, chef.getCodiceFiscale());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nell'aggiornamento dello chef!");
            e.printStackTrace();
            return false;
        }
    }

    // DELETE: elimina uno chef
    public boolean deleteChef(String codiceFiscale) {
        String sql = "DELETE FROM Chef WHERE codice_fiscale = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codiceFiscale);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nella cancellazione dello chef!");
            e.printStackTrace();
            return false;
        }
    }

    public Chef getChefByEmail(String email) {
        String sql = "SELECT * FROM Chef WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return new Chef(
                    rs.getString("codice_fiscale"),
                    rs.getString("nome"),
                    rs.getString("cognome"),
                    rs.getString("email"),
                    rs.getString("pass_hash")
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura dello chef!");
            e.printStackTrace();
        }
        return null;
    }

}