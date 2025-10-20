package it.uninafoodlab.dao;

import it.uninafoodlab.model.Iscrizione;
import it.uninafoodlab.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IscrizioneDAO {

    // Inserisce una nuova iscrizione
    public boolean insertIscrizione(Iscrizione iscrizione) {
        String query = "INSERT INTO Iscrizione (username_utente, cod_fisc_chef, titolo_corso) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, iscrizione.getUsernameUtente());
            ps.setString(2, iscrizione.getCodFiscChef());
            ps.setString(3, iscrizione.getTitoloCorso());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("? Errore nell'inserimento dell'iscrizione!");
            e.printStackTrace();
            return false;
        }
    }

    // Recupera un'iscrizione specifica
    public Iscrizione getIscrizione(String username, String codFiscChef, String titoloCorso) {
        String query = "SELECT * FROM Iscrizione WHERE username_utente = ? AND cod_fisc_chef = ? AND titolo_corso = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, codFiscChef);
            ps.setString(3, titoloCorso);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Timestamp dataIscrizione = rs.getTimestamp("data_iscrizione");
                return new Iscrizione(username, codFiscChef, titoloCorso, dataIscrizione);
            }

        } catch (SQLException e) {
            System.err.println("? Errore nel recupero dell'iscrizione!");
            e.printStackTrace();
        }

        return null;
    }

    // Elimina un'iscrizione
    public boolean deleteIscrizione(String username, String codFiscChef, String titoloCorso) {
        String query = "DELETE FROM Iscrizione WHERE username_utente = ? AND cod_fisc_chef = ? AND titolo_corso = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, codFiscChef);
            ps.setString(3, titoloCorso);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("? Errore nella cancellazione dell'iscrizione!");
            e.printStackTrace();
            return false;
        }
    }

    // Recupera tutte le iscrizioni di un utente
    public List<Iscrizione> getIscrizioniByUtente(String username) {
        List<Iscrizione> iscrizioni = new ArrayList<>();
        String query = "SELECT * FROM Iscrizione WHERE username_utente = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String codFiscChef = rs.getString("cod_fisc_chef");
                String titoloCorso = rs.getString("titolo_corso");
                Timestamp dataIscrizione = rs.getTimestamp("data_iscrizione");
                iscrizioni.add(new Iscrizione(username, codFiscChef, titoloCorso, dataIscrizione));
            }

        } catch (SQLException e) {
            System.err.println("? Errore nel recupero delle iscrizioni dell'utente!");
            e.printStackTrace();
        }

        return iscrizioni;
    }
}