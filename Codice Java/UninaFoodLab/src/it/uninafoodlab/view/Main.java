package it.uninafoodlab.view;

import it.uninafoodlab.dao.ChefDAO;
import it.uninafoodlab.dao.UtenteDAO;
import it.uninafoodlab.view.LoginFrame;

import javax.swing.SwingUtilities;

@SuppressWarnings("unused")
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UtenteDAO utenteDAO = new UtenteDAO();
            new LoginFrame(new UtenteDAO(), new ChefDAO());
        });
    }
}