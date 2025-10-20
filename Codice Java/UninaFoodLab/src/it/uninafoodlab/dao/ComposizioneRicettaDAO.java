package it.uninafoodlab.dao;

import it.uninafoodlab.model.ComposizioneRicetta;
import it.uninafoodlab.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ComposizioneRicettaDAO {

    public boolean aggiungiComposizioneRicetta(ComposizioneRicetta c) {
        String sql = "INSERT INTO ComposizioneRicetta " +
                     "(cod_fisc_chef, titolo_ricetta, data_creazione_ricetta, nome_ingrediente, quantita_ingrediente, unita_spec_ingrediente) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, c.getCodFiscChef());
            pstmt.setString(2, c.getTitoloRicetta());
            pstmt.setTimestamp(3, Timestamp.valueOf(c.getDataCreazioneRicetta()));
            pstmt.setString(4, c.getNomeIngrediente());
            pstmt.setDouble(5, c.getQuantitaIngrediente());
            pstmt.setString(6, c.getUnitaSpecIngrediente());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento di ComposizioneRicetta:");
            e.printStackTrace();
            return false;
        }
    }

    public ComposizioneRicetta getComposizioneRicetta(String codFiscChef, String titoloRicetta,
                                                      LocalDateTime dataCreazioneRicetta, String nomeIngrediente) {
        String sql = "SELECT * FROM ComposizioneRicetta " +
                     "WHERE cod_fisc_chef = ? AND titolo_ricetta = ? AND data_creazione_ricetta = ? AND nome_ingrediente = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codFiscChef);
            pstmt.setString(2, titoloRicetta);
            pstmt.setTimestamp(3, Timestamp.valueOf(dataCreazioneRicetta));
            pstmt.setString(4, nomeIngrediente);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapRowToComposizioneRicetta(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Recupera tutti gli ingredienti che compongono una specifica ricetta.
     * @return Lista di ComposizioneRicetta
     */
    public List<ComposizioneRicetta> getComposizioniByRicetta(String codFiscChef, String titoloRicetta, LocalDateTime dataCreazioneRicetta) {
        List<ComposizioneRicetta> composizioni = new ArrayList<>();
        String sql = "SELECT * FROM ComposizioneRicetta " +
                     "WHERE cod_fisc_chef = ? AND titolo_ricetta = ? AND data_creazione_ricetta = ?";
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codFiscChef);
            pstmt.setString(2, titoloRicetta);
            pstmt.setTimestamp(3, Timestamp.valueOf(dataCreazioneRicetta));
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                composizioni.add(mapRowToComposizioneRicetta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return composizioni;
    }
    
    /**
     * Aggiorna la quantità e l'unità di un ingrediente in una ricetta.
     * @param c L'oggetto ComposizioneRicetta con i dati aggiornati
     * @return true se l'aggiornamento ha successo
     */
    public boolean updateComposizioneRicetta(ComposizioneRicetta c) {
        String sql = "UPDATE ComposizioneRicetta " +
                     "SET quantita_ingrediente = ?, unita_spec_ingrediente = ? " +
                     "WHERE cod_fisc_chef = ? AND titolo_ricetta = ? AND data_creazione_ricetta = ? AND nome_ingrediente = ?";
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, c.getQuantitaIngrediente());
            pstmt.setString(2, c.getUnitaSpecIngrediente());
            pstmt.setString(3, c.getCodFiscChef());
            pstmt.setString(4, c.getTitoloRicetta());
            pstmt.setTimestamp(5, Timestamp.valueOf(c.getDataCreazioneRicetta()));
            pstmt.setString(6, c.getNomeIngrediente());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento di ComposizioneRicetta:");
            e.printStackTrace();
            return false;
        }
    }


    public boolean eliminaComposizioneRicetta(String codFiscChef, String titoloRicetta,
                                              LocalDateTime dataCreazioneRicetta, String nomeIngrediente) {
        String sql = "DELETE FROM ComposizioneRicetta " +
                     "WHERE cod_fisc_chef = ? AND titolo_ricetta = ? AND data_creazione_ricetta = ? AND nome_ingrediente = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codFiscChef);
            pstmt.setString(2, titoloRicetta);
            pstmt.setTimestamp(3, Timestamp.valueOf(dataCreazioneRicetta));
            pstmt.setString(4, nomeIngrediente);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Errore durante l'eliminazione di ComposizioneRicetta:");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Mappa una riga del ResultSet a un oggetto ComposizioneRicetta
     */
    private ComposizioneRicetta mapRowToComposizioneRicetta(ResultSet rs) throws SQLException {
        return new ComposizioneRicetta(
            rs.getString("cod_fisc_chef"),
            rs.getString("titolo_ricetta"),
            rs.getTimestamp("data_creazione_ricetta").toLocalDateTime(),
            rs.getString("nome_ingrediente"),
            rs.getDouble("quantita_ingrediente"),
            rs.getString("unita_spec_ingrediente")
        );
    }
}