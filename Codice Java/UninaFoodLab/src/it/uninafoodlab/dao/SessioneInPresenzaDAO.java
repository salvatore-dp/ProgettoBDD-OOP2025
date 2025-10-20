package it.uninafoodlab.dao;

import it.uninafoodlab.model.SessioneInPresenza;
import it.uninafoodlab.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class SessioneInPresenzaDAO {

    // CREATE (Aggiornato)
    public boolean insertSessione(SessioneInPresenza sessione) {
        String sql = "INSERT INTO SessioneInPresenza(cod_fisc_chef, titolo_corso, titolo_sessione, data_ora, durata, luogo, posti_totali, posti_disponibili) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sessione.getCodFiscChef());
            stmt.setString(2, sessione.getTitoloCorso());
            stmt.setString(3, sessione.getTitoloSessione());
            stmt.setTimestamp(4, Timestamp.valueOf(sessione.getDataOra()));
            stmt.setInt(5, sessione.getDurata());
            stmt.setString(6, sessione.getLuogo());
            
            stmt.setInt(7, sessione.getPostiTotali());
            // All'inserimento, i posti disponibili sono SEMPRE uguali ai posti totali
            stmt.setInt(8, sessione.getPostiTotali()); 

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nell'inserimento della sessione in presenza!");
            e.printStackTrace();
            return false;
        }
    }

    // READ (Aggiornato)
    public SessioneInPresenza getSessione(String codFiscChef, String titoloCorso, String titoloSessione) {
        String sql = "SELECT * FROM SessioneInPresenza WHERE cod_fisc_chef = ? AND titolo_corso = ? AND titolo_sessione = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codFiscChef);
            stmt.setString(2, titoloCorso);
            stmt.setString(3, titoloSessione);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SessioneInPresenza(
                        rs.getString("cod_fisc_chef"),
                        rs.getString("titolo_corso"),
                        rs.getString("titolo_sessione"),
                        rs.getTimestamp("data_ora").toLocalDateTime(),
                        rs.getInt("durata"),
                        rs.getString("luogo"),
                        rs.getInt("posti_totali"),
                        rs.getInt("posti_disponibili")
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura della sessione in presenza!");
            e.printStackTrace();
        }
        return null;
    }

    // READ all (Aggiornato)
    public List<SessioneInPresenza> getAllSessioni() {
        List<SessioneInPresenza> sessioni = new ArrayList<>();
        String sql = "SELECT * FROM SessioneInPresenza";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                sessioni.add(new SessioneInPresenza(
                        rs.getString("cod_fisc_chef"),
                        rs.getString("titolo_corso"),
                        rs.getString("titolo_sessione"),
                        rs.getTimestamp("data_ora").toLocalDateTime(),
                        rs.getInt("durata"),
                        rs.getString("luogo"),
                        rs.getInt("posti_totali"),      // Aggiunto
                        rs.getInt("posti_disponibili")  // Aggiunto
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura di tutte le sessioni in presenza!");
            e.printStackTrace();
        }
        return sessioni;
    }

    /**
     * Recupera tutte le sessioni in presenza per un corso specifico.
     */
    public List<SessioneInPresenza> getSessioniByCorso(String codFiscChef, String titoloCorso) {
        List<SessioneInPresenza> sessioni = new ArrayList<>();
        String sql = "SELECT * FROM SessioneInPresenza WHERE cod_fisc_chef = ? AND titolo_corso = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codFiscChef);
            stmt.setString(2, titoloCorso);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sessioni.add(new SessioneInPresenza(
                        rs.getString("cod_fisc_chef"),
                        rs.getString("titolo_corso"),
                        rs.getString("titolo_sessione"),
                        rs.getTimestamp("data_ora").toLocalDateTime(),
                        rs.getInt("durata"),
                        rs.getString("luogo"),
                        rs.getInt("posti_totali"),
                        rs.getInt("posti_disponibili")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura delle sessioni in presenza per il corso!");
            e.printStackTrace();
        }
        return sessioni;
    }


    // UPDATE
    public boolean updateSessione(SessioneInPresenza sessione) {
        // Query per includere posti_totali (non disponibili, gestiti dal trigger)
        String sql = "UPDATE SessioneInPresenza SET data_ora = ?, durata = ?, luogo = ?, posti_totali = ? " + 
                     "WHERE cod_fisc_chef = ? AND titolo_corso = ? AND titolo_sessione = ?";
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(sessione.getDataOra()));
            stmt.setInt(2, sessione.getDurata());
            stmt.setString(3, sessione.getLuogo());
            stmt.setInt(4, sessione.getPostiTotali());
            
            // Chiavi primarie per il WHERE
            stmt.setString(5, sessione.getCodFiscChef());
            stmt.setString(6, sessione.getTitoloCorso());
            stmt.setString(7, sessione.getTitoloSessione());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nell'aggiornamento della sessione in presenza!");
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteSessione(String codFiscChef, String titoloCorso, String titoloSessione) {
        String sql = "DELETE FROM SessioneInPresenza WHERE cod_fisc_chef = ? AND titolo_corso = ? AND titolo_sessione = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codFiscChef);
            stmt.setString(2, titoloCorso);
            stmt.setString(3, titoloSessione);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nella cancellazione della sessione in presenza!");
            e.printStackTrace();
            return false;
        }
    }
}