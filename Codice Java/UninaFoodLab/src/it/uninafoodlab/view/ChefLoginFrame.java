package it.uninafoodlab.view;

import it.uninafoodlab.dao.ChefDAO;
import it.uninafoodlab.dao.UtenteDAO;
import it.uninafoodlab.model.Chef;
import it.uninafoodlab.service.ChefService; 

import javax.swing.*;
import java.awt.*;

public class ChefLoginFrame extends JFrame {

    private ChefDAO chefDAO;

    public ChefLoginFrame() {
        super("Login Chef");
        chefDAO = new ChefDAO();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JButton loginButton = new JButton("Accedi");
        JButton backButton = new JButton("Torna Indietro");

        panel.add(loginButton);
        panel.add(backButton);

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            try {

                Chef chef = chefDAO.getChefByEmail(email); 
                
                if(chef != null && chef.getPassHash().equals(password)) {
                    JOptionPane.showMessageDialog(this, "Accesso chef effettuato!");
                    
                    ChefService service = new ChefService(chef);
                    new ChefMainFrame(chef, service);
                    
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Email o password non valide!", "Errore", JOptionPane.ERROR_MESSAGE);
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

        backButton.addActionListener(e -> {
            new LoginFrame(new UtenteDAO(), new ChefDAO()); 
            dispose(); 
        });

        add(panel);
        setVisible(true);
    }
}