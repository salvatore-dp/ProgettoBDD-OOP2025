package it.uninafoodlab.view;

import it.uninafoodlab.dao.ChefDAO;
import it.uninafoodlab.dao.UtenteDAO;
import it.uninafoodlab.model.Chef;
import it.uninafoodlab.model.Utente;
import it.uninafoodlab.service.ChefService;
import it.uninafoodlab.service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    @SuppressWarnings("unused")
    private UtenteDAO utenteDAO;
    @SuppressWarnings("unused")
    private ChefDAO chefDAO;

    public LoginFrame() {
        this(new UtenteDAO(), new ChefDAO());
    }

    public LoginFrame(UtenteDAO utenteDAO, ChefDAO chefDAO) {
        super("Login");
        this.utenteDAO = utenteDAO;
        this.chefDAO = chefDAO;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints(); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; gbc.insets = new Insets(5, 0, 5, 0);
        int fieldWidth = 180; int fieldHeight = 25;
        JLabel emailLabel = new JLabel("Email/Username:"); JTextField emailField = new JTextField(); emailField.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0; gbc.insets = new Insets(5, 0, 5, 0); formPanel.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1.0; gbc.insets = new Insets(5, 5, 5, 0); formPanel.add(emailField, gbc);
        JLabel passwordLabel = new JLabel("Password:"); JPasswordField passwordField = new JPasswordField(); passwordField.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0; gbc.insets = new Insets(5, 0, 5, 0); formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1.0; gbc.insets = new Insets(5, 5, 5, 0); formPanel.add(passwordField, gbc);
        JCheckBox chefCheckBox = new JCheckBox("Accedi come Chef");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(15, 0, 15, 0); formPanel.add(chefCheckBox, gbc);
        JButton loginButton = new JButton("Accedi"); JButton registratiButton = new JButton("Registrati");
        JPanel buttonPanel = new JPanel(); buttonPanel.add(loginButton); buttonPanel.add(registratiButton);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.insets = new Insets(5, 0, 5, 0); formPanel.add(buttonPanel, gbc);
        mainPanel.add(formPanel, BorderLayout.CENTER); add(mainPanel);


        registratiButton.addActionListener(e -> {
             new RegisterUserFrame(utenteDAO);
             dispose();
         });

        // --- LISTENER DI LOGIN ---
        loginButton.addActionListener(e -> {
             String emailOrUsername = emailField.getText().trim();
             String password = new String(passwordField.getPassword()).trim();

             try {
                 if (chefCheckBox.isSelected()) {
                     // --- LOGICA CHEF) ---
                     Chef chef = chefDAO.getChefByEmail(emailOrUsername);
                     if (chef != null && chef.getPassHash().equals(password)) {
                         JOptionPane.showMessageDialog(this, "Accesso chef effettuato!");
                         ChefService service = new ChefService(chef);
                         new ChefMainFrame(chef, service);
                         dispose();
                     } else {
                         JOptionPane.showMessageDialog(this, "Credenziali chef non valide!", "Errore", JOptionPane.ERROR_MESSAGE);
                     }
                 } else {
                     // --- LOGICA UTENTE---
                     // 1. Chiama il metodo DAO
                     Utente utenteLoggato = utenteDAO.getUtenteIfValidLogin(emailOrUsername, password);

                     if (utenteLoggato != null) { // Login valido se l'utente non è null
                         JOptionPane.showMessageDialog(this, "Login effettuato!");

                         // 2. Crea il UserService
                         UserService userService = new UserService(utenteLoggato);

                         // 3. Apri la UserMainFrame passando Utente e Service
                         new UserMainFrame(utenteLoggato, userService);

                         dispose();
                     } else {
                         JOptionPane.showMessageDialog(this, "Username o email o password errati", "Errore", JOptionPane.ERROR_MESSAGE);
                     }
                 }

             } catch (Exception ex) {
                 System.err.println("❌ Errore critico durante l'apertura della finestra principale!");
                 ex.printStackTrace();
                 JOptionPane.showMessageDialog(
                     this,
                     "Errore imprevisto nell'apertura del pannello.\nControlla la console o la connessione al DB.\n\n" + ex.getMessage(),
                     "Errore Critico",
                     JOptionPane.ERROR_MESSAGE
                 );
             }
         });

        setVisible(true);
    }
}