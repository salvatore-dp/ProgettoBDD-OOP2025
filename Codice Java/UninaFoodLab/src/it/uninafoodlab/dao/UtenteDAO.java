package it.uninafoodlab.dao;

import it.uninafoodlab.model.Utente;
import it.uninafoodlab.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAO {

    // CREATE: aggiunge un nuovo utente
    public boolean insertUtente(Utente utente) {
        String sql = "INSERT INTO Utente(username, nome, cognome, email, pass_hash) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utente.getUsername());
            stmt.setString(2, utente.getNome());
            stmt.setString(3, utente.getCognome());
            stmt.setString(4, utente.getEmail());
            stmt.setString(5, utente.getPassHash());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore nell'inserimento dell'utente!");
            e.printStackTrace();
            return false;
        }
    }

    // READ: cerca un utente per username o email
    public Utente getUtenteByUsernameOrEmail(String usernameOrEmail) {
        String sql = "SELECT * FROM Utente WHERE username = ? OR email = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usernameOrEmail);
            stmt.setString(2, usernameOrEmail);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utente(
                    rs.getString("username"),
                    rs.getString("nome"),
                    rs.getString("cognome"),
                    rs.getString("email"),
                    rs.getString("pass_hash")
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura dell'utente!");
            e.printStackTrace();
        }
        return null;
    }

    // READ: ritorna tutti gli utenti
    public List<Utente> getAllUtenti() {
        // ... (metodo invariato) ...
        List<Utente> utenti = new ArrayList<>();
        String sql = "SELECT * FROM Utente";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                utenti.add(new Utente(
                        rs.getString("username"), rs.getString("nome"),
                        rs.getString("cognome"), rs.getString("email"),
                        rs.getString("pass_hash")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Errore nella lettura di tutti gli utenti!");
            e.printStackTrace();
        }
        return utenti;
    }

    // UPDATE: aggiorna i dati di un utente
    public boolean updateUtente(Utente utente) {
        // ... (metodo invariato) ...
        String sql = "UPDATE Utente SET nome = ?, cognome = ?, email = ?, pass_hash = ? WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getCognome());
            stmt.setString(3, utente.getEmail());
            stmt.setString(4, utente.getPassHash());
            stmt.setString(5, utente.getUsername());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("❌ Errore nell'aggiornamento dell'utente!");
            e.printStackTrace();
            return false;
        }
    }

    // DELETE: elimina un utente
    public boolean deleteUtente(String username) {
        // ... (metodo invariato) ...
         String sql = "DELETE FROM Utente WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("❌ Errore nella cancellazione dell'utente!");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica le credenziali dell'utente.
     * @param usernameOrEmail Username o Email inseriti
     * @param password Password inserita
     * @return L'oggetto Utente se le credenziali sono valide, altrimenti null.
     */
    public Utente getUtenteIfValidLogin(String usernameOrEmail, String password) {
        Utente u = getUtenteByUsernameOrEmail(usernameOrEmail);
        if (u != null && u.getPassHash().equals(password)) {
            return u; // Credenziali corrette, ritorna l'utente
        }
        return null; // Credenziali errate
    }

}