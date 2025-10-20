package it.uninafoodlab.dao;

import it.uninafoodlab.model.ProgrammaSessione;
import it.uninafoodlab.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProgrammaSessioneDAO {

    public boolean aggiungiProgrammaSessione(ProgrammaSessione ps) {
        String sql = "INSERT INTO ProgrammaSessione " +
                     "(cod_fisc_chef, titolo_ricetta, data_creazione_ricetta, titolo_corso, titolo_sessione, porzioni_per_partec) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ps.getCodFiscChef());
            pstmt.setString(2, ps.getTitoloRicetta());
            pstmt.setTimestamp(3, Timestamp.valueOf(ps.getDataCreazioneRicetta()));
            pstmt.setString(4, ps.getTitoloCorso());
            pstmt.setString(5, ps.getTitoloSessione());
            pstmt.setDouble(6, ps.getPorzioniPerPartecipante());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Errore nell'inserimento del ProgrammaSessione:");
            e.printStackTrace();
            return false;
        }
    }

    public ProgrammaSessione getProgrammaSessione(String codFiscChef, String titoloRicetta,
                                                  LocalDateTime dataCreazioneRicetta, String titoloCorso,
                                                  String titoloSessione) {
        String sql = "SELECT * FROM ProgrammaSessione " +
                     "WHERE cod_fisc_chef = ? AND titolo_ricetta = ? AND data_creazione_ricetta = ? " +
                     "AND titolo_corso = ? AND titolo_sessione = ?";

        ProgrammaSessione ps = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codFiscChef);
            pstmt.setString(2, titoloRicetta);
            pstmt.setTimestamp(3, Timestamp.valueOf(dataCreazioneRicetta));
            pstmt.setString(4, titoloCorso);
            pstmt.setString(5, titoloSessione);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ps = mapRowToProgrammaSessione(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }

    /**
     * Recupera tutte le ricette programmate per una specifica sessione.
     */
    public List<ProgrammaSessione> getProgrammaBySessione(String codFiscChef, String titoloCorso, String titoloSessione) {
        List<ProgrammaSessione> programma = new ArrayList<>();
        String sql = "SELECT * FROM ProgrammaSessione " +
                     "WHERE cod_fisc_chef = ? AND titolo_corso = ? AND titolo_sessione = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codFiscChef);
            pstmt.setString(2, titoloCorso);
            pstmt.setString(3, titoloSessione);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                programma.add(mapRowToProgrammaSessione(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return programma;
    }


    public boolean eliminaProgrammaSessione(String codFiscChef, String titoloRicetta,
                                            LocalDateTime dataCreazioneRicetta, String titoloCorso,
                                            String titoloSessione) {
        String sql = "DELETE FROM ProgrammaSessione WHERE cod_fisc_chef = ? AND titolo_ricetta = ? " +
                     "AND data_creazione_ricetta = ? AND titolo_corso = ? AND titolo_sessione = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codFiscChef);
            pstmt.setString(2, titoloRicetta);
            pstmt.setTimestamp(3, Timestamp.valueOf(dataCreazioneRicetta));
            pstmt.setString(4, titoloCorso);
            pstmt.setString(5, titoloSessione);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private ProgrammaSessione mapRowToProgrammaSessione(ResultSet rs) throws SQLException {
         return new ProgrammaSessione(
                rs.getString("cod_fisc_chef"),
                rs.getString("titolo_ricetta"),
                rs.getTimestamp("data_creazione_ricetta").toLocalDateTime(),
                rs.getString("titolo_corso"),
                rs.getString("titolo_sessione"),
                rs.getDouble("porzioni_per_partec")
         );
    }
}