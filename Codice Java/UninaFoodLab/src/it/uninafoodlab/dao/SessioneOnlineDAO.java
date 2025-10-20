package it.uninafoodlab.dao;

import it.uninafoodlab.model.SessioneOnline;
import it.uninafoodlab.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class SessioneOnlineDAO {

    // CREATE
    public boolean insertSessione(SessioneOnline sessione) {
        String sql = "INSERT INTO SessioneOnline(cod_fisc_chef, titolo_corso, titolo_sessione, data_ora, durata, link_video) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sessione.getCodFiscChef());
            stmt.setString(2, sessione.getTitoloCorso());
            stmt.setString(3, sessione.getTitoloSessione());
            stmt.setTimestamp(4, Timestamp.valueOf(sessione.getDataOra()));
            stmt.setInt(5, sessione.getDurata());
            stmt.setString(6, sessione.getLinkVideo());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nell'inserimento della sessione online!");
            e.printStackTrace();
            return false;
        }
    }

    // READ
    public SessioneOnline getSessione(String codFiscChef, String titoloCorso, String titoloSessione) {
        String sql = "SELECT * FROM SessioneOnline WHERE cod_fisc_chef = ? AND titolo_corso = ? AND titolo_sessione = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codFiscChef);
            stmt.setString(2, titoloCorso);
            stmt.setString(3, titoloSessione);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SessioneOnline(
                        rs.getString("cod_fisc_chef"),
                        rs.getString("titolo_corso"),
                        rs.getString("titolo_sessione"),
                        rs.getTimestamp("data_ora").toLocalDateTime(),
                        rs.getInt("durata"),
                        rs.getString("link_video")
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura della sessione online!");
            e.printStackTrace();
        }
        return null;
    }

    // READ all
    public List<SessioneOnline> getAllSessioni() {
        List<SessioneOnline> sessioni = new ArrayList<>();
        String sql = "SELECT * FROM SessioneOnline";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                sessioni.add(new SessioneOnline(
                        rs.getString("cod_fisc_chef"),
                        rs.getString("titolo_corso"),
                        rs.getString("titolo_sessione"),
                        rs.getTimestamp("data_ora").toLocalDateTime(),
                        rs.getInt("durata"),
                        rs.getString("link_video")
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura di tutte le sessioni online!");
            e.printStackTrace();
        }
        return sessioni;
    }

    /**
     * Recupera tutte le sessioni online per un corso specifico.
     */
    public List<SessioneOnline> getSessioniByCorso(String codFiscChef, String titoloCorso) {
        List<SessioneOnline> sessioni = new ArrayList<>();
        String sql = "SELECT * FROM SessioneOnline WHERE cod_fisc_chef = ? AND titolo_corso = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codFiscChef);
            stmt.setString(2, titoloCorso);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sessioni.add(new SessioneOnline(
                        rs.getString("cod_fisc_chef"),
                        rs.getString("titolo_corso"),
                        rs.getString("titolo_sessione"),
                        rs.getTimestamp("data_ora").toLocalDateTime(),
                        rs.getInt("durata"),
                        rs.getString("link_video")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura delle sessioni online per il corso!");
            e.printStackTrace();
        }
        return sessioni;
    }


    // UPDATE
    public boolean updateSessione(SessioneOnline sessione) {
        String sql = "UPDATE SessioneOnline SET data_ora = ?, durata = ?, link_video = ? WHERE cod_fisc_chef = ? AND titolo_corso = ? AND titolo_sessione = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(sessione.getDataOra()));
            stmt.setInt(2, sessione.getDurata());
            stmt.setString(3, sessione.getLinkVideo());
            stmt.setString(4, sessione.getCodFiscChef());
            stmt.setString(5, sessione.getTitoloCorso());
            stmt.setString(6, sessione.getTitoloSessione());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nell'aggiornamento della sessione online!");
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteSessione(String codFiscChef, String titoloCorso, String titoloSessione) {
        String sql = "DELETE FROM SessioneOnline WHERE cod_fisc_chef = ? AND titolo_corso = ? AND titolo_sessione = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codFiscChef);
            stmt.setString(2, titoloCorso);
            stmt.setString(3, titoloSessione);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nella cancellazione della sessione online!");
            e.printStackTrace();
            return false;
        }
    }
}