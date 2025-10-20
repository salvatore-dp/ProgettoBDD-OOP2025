package it.uninafoodlab.view;

import it.uninafoodlab.dao.ChefDAO;
import it.uninafoodlab.dao.UtenteDAO;
import it.uninafoodlab.model.Utente;

import javax.swing.*;
import java.awt.*;

public class RegisterUserFrame extends JFrame {

    @SuppressWarnings("unused")
    private UtenteDAO utenteDAO;

    public RegisterUserFrame(UtenteDAO utenteDAO) {
        super("Registrazione Utente");
        this.utenteDAO = utenteDAO;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7,2,5,5));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JTextField usernameField = new JTextField();
        JTextField nomeField = new JTextField();
        JTextField cognomeField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Username:")); panel.add(usernameField);
        panel.add(new JLabel("Nome:")); panel.add(nomeField);
        panel.add(new JLabel("Cognome:")); panel.add(cognomeField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Password:")); panel.add(passwordField);

        JButton registerButton = new JButton("Registrati");
        panel.add(new JLabel());
        panel.add(registerButton);

        JButton backButton = new JButton("Torna indietro");
        panel.add(new JLabel());
        panel.add(backButton);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if(username.isEmpty() || nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Compila tutti i campi!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Utente u = new Utente(username, nome, cognome, email, password);
            boolean success = utenteDAO.insertUtente(u);
            if(success) {
                JOptionPane.showMessageDialog(this, "Registrazione completata!");
                new LoginFrame(utenteDAO, new ChefDAO()); // torna al login
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Errore durante la registrazione!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            new LoginFrame(utenteDAO, new ChefDAO()); // torna al login
            dispose();
        });

        add(panel);
        setVisible(true);
    }
}