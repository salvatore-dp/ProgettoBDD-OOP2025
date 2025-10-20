package it.uninafoodlab.dao;

import it.uninafoodlab.model.Ricetta;
import it.uninafoodlab.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RicettaDAO {

    // CREATE: aggiunge una nuova ricetta
    public boolean insertRicetta(Ricetta ricetta) {
        String sql = "INSERT INTO Ricetta(cod_fisc_chef, titolo_ricetta, descrizione, difficolta, tempo_prep, porzioni, data_creazione) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ricetta.getCodFiscChef());
            stmt.setString(2, ricetta.getTitoloRicetta());
            stmt.setString(3, ricetta.getDescrizione());
            stmt.setObject(4, ricetta.getDifficolta(), java.sql.Types.OTHER); 
            stmt.setInt(5, ricetta.getTempoPrep());
            stmt.setInt(6, ricetta.getPorzioni());
            stmt.setTimestamp(7, Timestamp.valueOf(ricetta.getDataCreazione()));

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nell'inserimento della ricetta!");
            e.printStackTrace();
            return false;
        }
    }

    // READ: cerca ricetta per PK (chef + titolo + dataCreazione)
    public Ricetta getRicetta(String codFiscChef, String titoloRicetta, LocalDateTime dataCreazione) {
        String sql = "SELECT * FROM Ricetta WHERE cod_fisc_chef = ? AND titolo_ricetta = ? AND data_creazione = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codFiscChef);
            stmt.setString(2, titoloRicetta);
            stmt.setTimestamp(3, Timestamp.valueOf(dataCreazione));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Ricetta(
                        rs.getString("cod_fisc_chef"),
                        rs.getString("titolo_ricetta"),
                        rs.getString("descrizione"),
                        rs.getString("difficolta"),
                        rs.getInt("tempo_prep"),
                        rs.getInt("porzioni"),
                        rs.getTimestamp("data_creazione").toLocalDateTime()
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura della ricetta!");
            e.printStackTrace();
        }
        return null;
    }

    // READ: tutte le ricette di uno chef
    public List<Ricetta> getRicetteByChef(String codFiscChef) {
        List<Ricetta> ricette = new ArrayList<>();
        String sql = "SELECT * FROM Ricetta WHERE cod_fisc_chef = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codFiscChef);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ricette.add(new Ricetta(
                        rs.getString("cod_fisc_chef"),
                        rs.getString("titolo_ricetta"),
                        rs.getString("descrizione"),
                        rs.getString("difficolta"),
                        rs.getInt("tempo_prep"),
                        rs.getInt("porzioni"),
                        rs.getTimestamp("data_creazione").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura delle ricette dello chef!");
            e.printStackTrace();
        }

        return ricette;
    }

    // UPDATE: aggiorna dati di una ricetta
    public boolean updateRicetta(Ricetta ricetta) {
        String sql = "UPDATE Ricetta SET descrizione = ?, difficolta = ?, tempo_prep = ?, porzioni = ? " +
                     "WHERE cod_fisc_chef = ? AND titolo_ricetta = ? AND data_creazione = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ricetta.getDescrizione());
            stmt.setObject(2, ricetta.getDifficolta(), java.sql.Types.OTHER);
            
            stmt.setInt(3, ricetta.getTempoPrep());
            stmt.setInt(4, ricetta.getPorzioni());
            stmt.setString(5, ricetta.getCodFiscChef());
            stmt.setString(6, ricetta.getTitoloRicetta());
            stmt.setTimestamp(7, Timestamp.valueOf(ricetta.getDataCreazione()));

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nell'aggiornamento della ricetta!");
            e.printStackTrace();
            return false;
        }
    }

    // DELETE: elimina una ricetta
    public boolean deleteRicetta(String codFiscChef, String titoloRicetta, LocalDateTime dataCreazione) {
        String sql = "DELETE FROM Ricetta WHERE cod_fisc_chef = ? AND titolo_ricetta = ? AND data_creazione = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codFiscChef);
            stmt.setString(2, titoloRicetta);
            stmt.setTimestamp(3, Timestamp.valueOf(dataCreazione));

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nella cancellazione della ricetta!");
            e.printStackTrace();
            return false;
        }
    }
}