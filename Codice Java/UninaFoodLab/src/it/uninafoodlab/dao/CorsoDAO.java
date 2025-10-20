package it.uninafoodlab.dao;

import it.uninafoodlab.model.Corso;
import it.uninafoodlab.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class CorsoDAO {

    // CREATE: aggiunge un nuovo corso
    public boolean insertCorso(Corso corso) {
        String sql = "INSERT INTO Corso(cod_fisc_chef, titolo, frequenza, data_inizio) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, corso.getCodFiscChef());
            stmt.setString(2, corso.getTitolo());
            stmt.setString(3, corso.getFrequenza());
            stmt.setDate(4, Date.valueOf(corso.getDataInizio())); // LocalDate → java.sql.Date

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nell'inserimento del corso!");
            e.printStackTrace();
            return false;
        }
    }

    // READ: cerca un corso per chef e titolo
    public Corso getCorso(String codFiscChef, String titolo) {
        String sql = "SELECT * FROM Corso WHERE cod_fisc_chef = ? AND titolo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codFiscChef);
            stmt.setString(2, titolo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Corso(
                        rs.getString("cod_fisc_chef"),
                        rs.getString("titolo"),
                        rs.getString("frequenza"),
                        rs.getDate("data_inizio").toLocalDate()
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura del corso!");
            e.printStackTrace();
        }
        return null;
    }

    // READ: ritorna tutti i corsi
    public List<Corso> getAllCorsi() {
        List<Corso> corsi = new ArrayList<>();
        String sql = "SELECT * FROM Corso";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                corsi.add(new Corso(
                        rs.getString("cod_fisc_chef"),
                        rs.getString("titolo"),
                        rs.getString("frequenza"),
                        rs.getDate("data_inizio").toLocalDate()
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura di tutti i corsi!");
            e.printStackTrace();
        }

        return corsi;
    }

    /**
     * Ritorna la lista di tutti i corsi tenuti da uno specifico chef.
     * @param codFiscChef Il codice fiscale dello chef
     * @return Lista di Corsi
     */
    public List<Corso> getCorsiByChef(String codFiscChef) {
        List<Corso> corsi = new ArrayList<>();
        String sql = "SELECT * FROM Corso WHERE cod_fisc_chef = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codFiscChef);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                corsi.add(new Corso(
                        rs.getString("cod_fisc_chef"),
                        rs.getString("titolo"),
                        rs.getString("frequenza"),
                        rs.getDate("data_inizio").toLocalDate()
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura dei corsi dello chef!");
            e.printStackTrace();
        }

        return corsi;
    }


    // UPDATE: aggiorna i dati di un corso
    public boolean updateCorso(Corso corso) {
        String sql = "UPDATE Corso SET frequenza = ?, data_inizio = ? WHERE cod_fisc_chef = ? AND titolo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, corso.getFrequenza());
            stmt.setDate(2, Date.valueOf(corso.getDataInizio()));
            stmt.setString(3, corso.getCodFiscChef());
            stmt.setString(4, corso.getTitolo());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nell'aggiornamento del corso!");
            e.printStackTrace();
            return false;
        }
    }

    // DELETE: elimina un corso
    public boolean deleteCorso(String codFiscChef, String titolo) {
        String sql = "DELETE FROM Corso WHERE cod_fisc_chef = ? AND titolo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codFiscChef);
            stmt.setString(2, titolo);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nella cancellazione del corso!");
            e.printStackTrace();
            return false;
        }
    }
}