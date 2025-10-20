package it.uninafoodlab.dao;

import it.uninafoodlab.model.Adesione;
import it.uninafoodlab.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdesioneDAO {

    public boolean insertAdesione(Adesione adesione) {
        String sql = "INSERT INTO Adesione (username_utente, cod_fisc_chef, titolo_corso, titolo_sessione, data_adesione, confermata) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, adesione.getUsernameUtente());
            ps.setString(2, adesione.getCodFiscChef());
            ps.setString(3, adesione.getTitoloCorso());
            ps.setString(4, adesione.getTitoloSessione());
            if (adesione.getDataAdesione() != null) {
                ps.setTimestamp(5, adesione.getDataAdesione());
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
             if (adesione.getConfermata() != null) {
                ps.setBoolean(6, adesione.getConfermata());
            } else {
                 ps.setNull(6, Types.BOOLEAN);
            }


            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("? Errore durante l'inserimento dell'adesione!");
            e.printStackTrace();
            return false;
        }
    }

    public Adesione getAdesione(String usernameUtente, String codFiscChef, String titoloCorso, String titoloSessione) {
        String sql = "SELECT * FROM Adesione WHERE username_utente = ? AND cod_fisc_chef = ? AND titolo_corso = ? AND titolo_sessione = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usernameUtente); ps.setString(2, codFiscChef); ps.setString(3, titoloCorso); ps.setString(4, titoloSessione);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { return mapRowToAdesione(rs); } // Usa helper
            }
        } catch (SQLException e) { System.err.println("? Errore durante la lettura dell'adesione!"); e.printStackTrace(); }
        return null;
    }

    public boolean updateConferma(String usernameUtente, String codFiscChef, String titoloCorso, String titoloSessione, boolean confermata) {
        String sql = "UPDATE Adesione SET confermata = ? WHERE username_utente = ? AND cod_fisc_chef = ? AND titolo_corso = ? AND titolo_sessione = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, confermata); ps.setString(2, usernameUtente); ps.setString(3, codFiscChef); ps.setString(4, titoloCorso); ps.setString(5, titoloSessione);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("? Errore durante l'aggiornamento della conferma!"); e.printStackTrace(); return false; }
    }

    public boolean deleteAdesione(String usernameUtente, String codFiscChef, String titoloCorso, String titoloSessione) {
        String sql = "DELETE FROM Adesione WHERE username_utente = ? AND cod_fisc_chef = ? AND titolo_corso = ? AND titolo_sessione = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usernameUtente); ps.setString(2, codFiscChef); ps.setString(3, titoloCorso); ps.setString(4, titoloSessione);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println("? Errore durante la cancellazione dell'adesione!"); e.printStackTrace(); return false; }
    }

    public List<Adesione> getAllAdesioniByUtente(String usernameUtente) {
        List<Adesione> adesioni = new ArrayList<>();
        String sql = "SELECT * FROM Adesione WHERE username_utente = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usernameUtente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) { adesioni.add(mapRowToAdesione(rs)); } // Usa helper
            }
        } catch (SQLException e) { System.err.println("? Errore durante il recupero delle adesioni dell'utente!"); e.printStackTrace(); }
        return adesioni;
    }

    /**
     * Conta il numero di utenti che hanno confermato la partecipazione
     * (confermata = TRUE) per una specifica sessione in presenza.
     * @return Il numero di partecipanti confermati.
     */
    public int countPartecipantiConfermati(String codFiscChef, String titoloCorso, String titoloSessione) {
        String sql = "SELECT COUNT(*) FROM Adesione " +
                     "WHERE cod_fisc_chef = ? AND titolo_corso = ? AND titolo_sessione = ? AND confermata = TRUE";
        int count = 0;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codFiscChef);
            ps.setString(2, titoloCorso);
            ps.setString(3, titoloSessione);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("? Errore durante il conteggio dei partecipanti confermati!");
            e.printStackTrace();
        }
        return count;
    }

    private Adesione mapRowToAdesione(ResultSet rs) throws SQLException {
         Timestamp ts = rs.getTimestamp("data_adesione");
         Boolean conf = rs.getBoolean("confermata");
         if (rs.wasNull()) { // Controlla se il boolean era NULL nel DB
             conf = null;
         }
         return new Adesione(
                rs.getString("username_utente"),
                rs.getString("cod_fisc_chef"),
                rs.getString("titolo_corso"),
                rs.getString("titolo_sessione"),
                ts,
                conf
            );
    }
}