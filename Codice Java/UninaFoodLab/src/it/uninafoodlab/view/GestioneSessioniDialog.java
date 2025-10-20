package it.uninafoodlab.view;

import it.uninafoodlab.model.*;
import it.uninafoodlab.service.ChefService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Un JDialog modale per visualizzare e gestire le sessioni
 * di uno specifico corso.
 */
public class GestioneSessioniDialog extends JDialog {

    private ChefService chefService;
    private Corso corso;

    // Modelli per le JList
    private DefaultListModel<String> onlineListModel;
    private DefaultListModel<String> inPresenzaListModel;

    // Cache per gli oggetti sessione
    private List<SessioneInPresenza> cacheSessioniPresenza;
    private List<SessioneOnline> cacheSessioniOnline;

    // Oggetti selezionati
    private SessioneInPresenza sessionePresenzaSelezionata = null;
    private SessioneOnline sessioneOnlineSelezionata = null;

    // Riferimenti ai pulsanti
    private JButton btnAggiungiPresenza, btnModificaPresenza, btnEliminaPresenza, btnGestisciProgramma; // Sinistra
    private JButton btnAggiungiOnline, btnModificaOnline, btnEliminaOnline; // Destra
    private JList<String> listInPresenza;
    private JList<String> listOnline;


    public GestioneSessioniDialog(Frame owner, ChefService chefService, Corso corso) {
        super(owner, "Gestione Sessioni per: " + corso.getTitolo(), true);

        this.chefService = chefService;
        this.corso = corso;
        this.cacheSessioniPresenza = new ArrayList<>();
        this.cacheSessioniOnline = new ArrayList<>();

        setSize(780, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // --- Pannello Principale: diviso in due ---
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Pannello SINISTRO: Sessioni In Presenza ---
        JPanel panelInPresenza = new JPanel(new BorderLayout(5, 5));
        panelInPresenza.setBorder(BorderFactory.createTitledBorder("Sessioni In Presenza"));

        inPresenzaListModel = new DefaultListModel<>();
        listInPresenza = new JList<>(inPresenzaListModel);
        listInPresenza.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelInPresenza.add(new JScrollPane(listInPresenza), BorderLayout.CENTER);

        // Pannello pulsanti per "In Presenza" con BoxLayout
        JPanel btnPanelPresenza = new JPanel();
        btnPanelPresenza.setLayout(new BoxLayout(btnPanelPresenza, BoxLayout.X_AXIS));
        btnPanelPresenza.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        btnAggiungiPresenza = new JButton("Aggiungi...");
        btnModificaPresenza = new JButton("Modifica...");
        btnEliminaPresenza = new JButton("Elimina");
        btnGestisciProgramma = new JButton("Programma");

        Insets smallMargin = new Insets(2, 6, 2, 6);
        btnAggiungiPresenza.setMargin(smallMargin);
        btnModificaPresenza.setMargin(smallMargin);
        btnEliminaPresenza.setMargin(smallMargin);
        btnGestisciProgramma.setMargin(smallMargin);

        btnPanelPresenza.add(btnAggiungiPresenza);
        btnPanelPresenza.add(Box.createHorizontalStrut(5));
        btnPanelPresenza.add(btnModificaPresenza);
        btnPanelPresenza.add(Box.createHorizontalStrut(5));
        btnPanelPresenza.add(btnEliminaPresenza);
        btnPanelPresenza.add(Box.createHorizontalGlue());
        btnPanelPresenza.add(btnGestisciProgramma);

        panelInPresenza.add(btnPanelPresenza, BorderLayout.SOUTH);
        mainPanel.add(panelInPresenza);


        // --- Pannello DESTRO: Sessioni Online ---
        JPanel panelOnline = new JPanel(new BorderLayout(5, 5));
        panelOnline.setBorder(BorderFactory.createTitledBorder("Sessioni Online"));

        onlineListModel = new DefaultListModel<>();
        listOnline = new JList<>(onlineListModel);
        listOnline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelOnline.add(new JScrollPane(listOnline), BorderLayout.CENTER);

        // Pannello pulsanti per "Online" con BoxLayout
        JPanel btnPanelOnline = new JPanel();
        btnPanelOnline.setLayout(new BoxLayout(btnPanelOnline, BoxLayout.X_AXIS));
        btnPanelOnline.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        btnAggiungiOnline = new JButton("Aggiungi...");
        btnModificaOnline = new JButton("Modifica...");
        btnEliminaOnline = new JButton("Elimina");

        btnAggiungiOnline.setMargin(smallMargin);
        btnModificaOnline.setMargin(smallMargin);
        btnEliminaOnline.setMargin(smallMargin);

        btnPanelOnline.add(btnAggiungiOnline);
        btnPanelOnline.add(Box.createHorizontalStrut(5));
        btnPanelOnline.add(btnModificaOnline);
        btnPanelOnline.add(Box.createHorizontalStrut(5));
        btnPanelOnline.add(btnEliminaOnline);

        panelOnline.add(btnPanelOnline, BorderLayout.SOUTH);
        mainPanel.add(panelOnline);

        add(mainPanel, BorderLayout.CENTER);

        // --- Pulsante CHIUDI in basso ---
        JButton closeButton = new JButton("Chiudi");
        closeButton.addActionListener(e -> dispose());
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        southPanel.add(closeButton);
        add(southPanel, BorderLayout.SOUTH);

        btnAggiungiPresenza.addActionListener(e -> gestisciCreazioneSessioneInPresenza());
        btnModificaPresenza.addActionListener(e -> { if (sessionePresenzaSelezionata != null) gestisciModificaSessioneInPresenza(); });
        btnEliminaPresenza.addActionListener(e -> { if (sessionePresenzaSelezionata != null) gestisciEliminaSessioneInPresenza(); });
        btnGestisciProgramma.addActionListener(e -> { if (sessionePresenzaSelezionata != null) { new GestioneProgrammaDialog(this, chefService, sessionePresenzaSelezionata); caricaDatiSessioni(); } });
        btnAggiungiOnline.addActionListener(e -> gestisciCreazioneSessioneOnline());
        btnModificaOnline.addActionListener(e -> { if (sessioneOnlineSelezionata != null) gestisciModificaSessioneOnline(); });
        btnEliminaOnline.addActionListener(e -> { if (sessioneOnlineSelezionata != null) gestisciEliminaSessioneOnline(); });

        aggiungiListenersListe();

        // Carica i dati e imposta stato iniziale pulsanti
        caricaDatiSessioni();

        setVisible(true);
    }

    /**
     * Aggiunge tutti i listener necessari alle JList
     */
    private void aggiungiListenersListe() {
        // --- Lista SESSIONI IN PRESENZA ---
        listInPresenza.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = listInPresenza.getSelectedIndex();
                boolean enabled = (selectedIndex != -1 && selectedIndex < cacheSessioniPresenza.size());
                sessionePresenzaSelezionata = enabled ? cacheSessioniPresenza.get(selectedIndex) : null;
                btnModificaPresenza.setEnabled(enabled);
                btnEliminaPresenza.setEnabled(enabled);
                btnGestisciProgramma.setEnabled(enabled);
                if (enabled) {
                    listOnline.clearSelection();
                    btnModificaOnline.setEnabled(false);
                    btnEliminaOnline.setEnabled(false);
                    sessioneOnlineSelezionata = null;
                }
            }
        });
        listInPresenza.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && sessionePresenzaSelezionata != null) gestisciModificaSessioneInPresenza();
            }
        });
        // --- Lista SESSIONI ONLINE ---
        listOnline.addListSelectionListener(e -> {
             if (!e.getValueIsAdjusting()) {
                int selectedIndex = listOnline.getSelectedIndex();
                boolean enabled = (selectedIndex != -1 && selectedIndex < cacheSessioniOnline.size());
                sessioneOnlineSelezionata = enabled ? cacheSessioniOnline.get(selectedIndex) : null;
                btnModificaOnline.setEnabled(enabled);
                btnEliminaOnline.setEnabled(enabled);
                if (enabled) {
                    listInPresenza.clearSelection();
                    btnModificaPresenza.setEnabled(false);
                    btnEliminaPresenza.setEnabled(false);
                    btnGestisciProgramma.setEnabled(false);
                    sessionePresenzaSelezionata = null;
                }
            }
        });
        listOnline.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && sessioneOnlineSelezionata != null) gestisciModificaSessioneOnline();
            }
        });
    }

    /**
     * Carica i dati delle sessioni e resetta i pulsanti
     */
    private void caricaDatiSessioni() {
        onlineListModel.clear(); inPresenzaListModel.clear(); cacheSessioniPresenza.clear(); cacheSessioniOnline.clear();
        List<SessioneOnline> sessioniOnline = chefService.getSessioniOnlineDelCorso(corso);
        if (sessioniOnline.isEmpty()) { onlineListModel.addElement("Nessuna sessione online."); } else { sessioniOnline.forEach(s -> { onlineListModel.addElement(s.getTitoloSessione() + " (" + s.getDataOra() + ")"); cacheSessioniOnline.add(s); }); }
        List<SessioneInPresenza> sessioniInPresenza = chefService.getSessioniInPresenzaDelCorso(corso);
        if (sessioniInPresenza.isEmpty()) { inPresenzaListModel.addElement("Nessuna sessione in presenza."); } else { sessioniInPresenza.forEach(s -> { inPresenzaListModel.addElement(s.getTitoloSessione() + " (Posti: " + s.getPostiDisponibili() + "/" + s.getPostiTotali() + ")"); cacheSessioniPresenza.add(s); }); }
        sessionePresenzaSelezionata = null; sessioneOnlineSelezionata = null;
        btnModificaPresenza.setEnabled(false); btnEliminaPresenza.setEnabled(false); btnGestisciProgramma.setEnabled(false);
        btnModificaOnline.setEnabled(false); btnEliminaOnline.setEnabled(false);
        listInPresenza.clearSelection(); listOnline.clearSelection();
    }

    private void gestisciCreazioneSessioneInPresenza() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField titoloField = new JTextField();
        JTextField dataOraField = new JTextField(LocalDateTime.now().withSecond(0).withNano(0).toString());
        JTextField durataField = new JTextField("120");
        JTextField luogoField = new JTextField();
        JTextField postiField = new JTextField("20");
        formPanel.add(new JLabel("Titolo Sessione:")); formPanel.add(titoloField);
        formPanel.add(new JLabel("Data/Ora (YYYY-MM-DDTHH:MM):")); formPanel.add(dataOraField);
        formPanel.add(new JLabel("Durata (minuti):")); formPanel.add(durataField);
        formPanel.add(new JLabel("Luogo:")); formPanel.add(luogoField);
        formPanel.add(new JLabel("Posti Totali:")); formPanel.add(postiField);
        int result = JOptionPane.showConfirmDialog(this, formPanel, "Crea Nuova Sessione in Presenza", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                 String titolo = titoloField.getText().trim(); String dataTesto = dataOraField.getText().trim(); String durataTesto = durataField.getText().trim(); String luogo = luogoField.getText().trim(); String postiTesto = postiField.getText().trim();
                if (titolo.isEmpty() || dataTesto.isEmpty() || durataTesto.isEmpty() || luogo.isEmpty() || postiTesto.isEmpty()) { JOptionPane.showMessageDialog(this, "Tutti i campi sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE); return; }
                LocalDateTime dataOra = LocalDateTime.parse(dataTesto); int durata = Integer.parseInt(durataTesto); int postiTotali = Integer.parseInt(postiTesto);
                if (durata <= 0 || postiTotali <= 0) { JOptionPane.showMessageDialog(this, "Durata e Posti devono essere numeri positivi.", "Errore", JOptionPane.ERROR_MESSAGE); return; }
                boolean successo = chefService.creaNuovaSessioneInPresenza(this.corso, titolo, dataOra, durata, luogo, postiTotali);
                if (successo) { JOptionPane.showMessageDialog(this, "Sessione creata con successo!"); caricaDatiSessioni(); }
                else { JOptionPane.showMessageDialog(this, "Errore nella creazione della sessione.\n(Il titolo sessione potrebbe esistere già per questo corso).", "Errore", JOptionPane.ERROR_MESSAGE); }
            } catch (DateTimeParseException ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Formato data non valido! Usare YYYY-MM-DDTHH:MM (es. 2025-12-01T18:00)", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Durata e Posti devono essere numeri validi.", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Errore imprevisto: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void gestisciCreazioneSessioneOnline() {
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField titoloField = new JTextField();
        JTextField dataOraField = new JTextField(LocalDateTime.now().withSecond(0).withNano(0).toString());
        JTextField durataField = new JTextField("60");
        JTextField linkField = new JTextField("https://");
        formPanel.add(new JLabel("Titolo Sessione:")); formPanel.add(titoloField);
        formPanel.add(new JLabel("Data/Ora (YYYY-MM-DDTHH:MM):")); formPanel.add(dataOraField);
        formPanel.add(new JLabel("Durata (minuti):")); formPanel.add(durataField);
        formPanel.add(new JLabel("Link Video:")); formPanel.add(linkField);
        int result = JOptionPane.showConfirmDialog(this, formPanel, "Crea Nuova Sessione Online", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String titolo = titoloField.getText().trim(); String dataTesto = dataOraField.getText().trim(); String durataTesto = durataField.getText().trim(); String link = linkField.getText().trim();
                if (titolo.isEmpty() || dataTesto.isEmpty() || durataTesto.isEmpty() || link.isEmpty()) { JOptionPane.showMessageDialog(this, "Tutti i campi sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE); return; }
                LocalDateTime dataOra = LocalDateTime.parse(dataTesto); int durata = Integer.parseInt(durataTesto);
                if (durata <= 0) { JOptionPane.showMessageDialog(this, "La durata deve essere un numero positivo.", "Errore", JOptionPane.ERROR_MESSAGE); return; }
                boolean successo = chefService.creaNuovaSessioneOnline(this.corso, titolo, dataOra, durata, link);
                if (successo) { JOptionPane.showMessageDialog(this, "Sessione creata con successo!"); caricaDatiSessioni(); }
                else { JOptionPane.showMessageDialog(this, "Errore nella creazione della sessione.\n(Il titolo sessione potrebbe esistere già per questo corso).", "Errore", JOptionPane.ERROR_MESSAGE); }
            } catch (DateTimeParseException ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Formato data non valido! Usare YYYY-MM-DDTHH:MM (es. 2025-12-01T18:00)", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "La durata deve essere un numero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Errore imprevisto: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void gestisciModificaSessioneInPresenza() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField titoloField = new JTextField(sessionePresenzaSelezionata.getTitoloSessione());
        titoloField.setEnabled(false);
        JTextField dataOraField = new JTextField(sessionePresenzaSelezionata.getDataOra().toString());
        JTextField durataField = new JTextField(String.valueOf(sessionePresenzaSelezionata.getDurata()));
        JTextField luogoField = new JTextField(sessionePresenzaSelezionata.getLuogo());
        JTextField postiField = new JTextField(String.valueOf(sessionePresenzaSelezionata.getPostiTotali()));
        formPanel.add(new JLabel("Titolo Sessione (non modif.):")); formPanel.add(titoloField);
        formPanel.add(new JLabel("Data/Ora (YYYY-MM-DDTHH:MM):")); formPanel.add(dataOraField);
        formPanel.add(new JLabel("Durata (minuti):")); formPanel.add(durataField);
        formPanel.add(new JLabel("Luogo:")); formPanel.add(luogoField);
        formPanel.add(new JLabel("Posti Totali:")); formPanel.add(postiField);
        int result = JOptionPane.showConfirmDialog(this, formPanel, "Modifica Sessione in Presenza", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
             try {
                 String dataTesto = dataOraField.getText().trim(); String durataTesto = durataField.getText().trim(); String luogo = luogoField.getText().trim(); String postiTesto = postiField.getText().trim();
                if (dataTesto.isEmpty() || durataTesto.isEmpty() || luogo.isEmpty() || postiTesto.isEmpty()) { JOptionPane.showMessageDialog(this, "Tutti i campi sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE); return; }
                LocalDateTime dataOra = LocalDateTime.parse(dataTesto); int durata = Integer.parseInt(durataTesto); int postiTotali = Integer.parseInt(postiTesto);
                if (durata <= 0 || postiTotali <= 0) { JOptionPane.showMessageDialog(this, "Durata e Posti devono essere numeri positivi.", "Errore", JOptionPane.ERROR_MESSAGE); return; }
                sessionePresenzaSelezionata.setDataOra(dataOra); sessionePresenzaSelezionata.setDurata(durata); sessionePresenzaSelezionata.setLuogo(luogo); sessionePresenzaSelezionata.setPostiTotali(postiTotali);
                boolean successo = chefService.aggiornaSessioneInPresenza(sessionePresenzaSelezionata);
                if (successo) { JOptionPane.showMessageDialog(this, "Sessione aggiornata con successo!"); caricaDatiSessioni(); }
                else { JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento.", "Errore", JOptionPane.ERROR_MESSAGE); }
            } catch (DateTimeParseException ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Formato data non valido! Usare YYYY-MM-DDTHH:MM", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Durata e Posti devono essere numeri validi.", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Errore imprevisto: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void gestisciModificaSessioneOnline() {
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField titoloField = new JTextField(sessioneOnlineSelezionata.getTitoloSessione());
        titoloField.setEnabled(false);
        JTextField dataOraField = new JTextField(sessioneOnlineSelezionata.getDataOra().toString());
        JTextField durataField = new JTextField(String.valueOf(sessioneOnlineSelezionata.getDurata()));
        JTextField linkField = new JTextField(sessioneOnlineSelezionata.getLinkVideo());
        formPanel.add(new JLabel("Titolo Sessione (non modif.):")); formPanel.add(titoloField);
        formPanel.add(new JLabel("Data/Ora (YYYY-MM-DDTHH:MM):")); formPanel.add(dataOraField);
        formPanel.add(new JLabel("Durata (minuti):")); formPanel.add(durataField);
        formPanel.add(new JLabel("Link Video:")); formPanel.add(linkField);
        int result = JOptionPane.showConfirmDialog(this, formPanel, "Modifica Sessione Online", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
             try {
                String dataTesto = dataOraField.getText().trim(); String durataTesto = durataField.getText().trim(); String link = linkField.getText().trim();
                if (dataTesto.isEmpty() || durataTesto.isEmpty() || link.isEmpty()) { JOptionPane.showMessageDialog(this, "Tutti i campi sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE); return; }
                LocalDateTime dataOra = LocalDateTime.parse(dataTesto); int durata = Integer.parseInt(durataTesto);
                if (durata <= 0) { JOptionPane.showMessageDialog(this, "La durata deve essere un numero positivo.", "Errore", JOptionPane.ERROR_MESSAGE); return; }
                sessioneOnlineSelezionata.setDataOra(dataOra); sessioneOnlineSelezionata.setDurata(durata); sessioneOnlineSelezionata.setLinkVideo(link);
                boolean successo = chefService.aggiornaSessioneOnline(sessioneOnlineSelezionata);
                if (successo) { JOptionPane.showMessageDialog(this, "Sessione aggiornata con successo!"); caricaDatiSessioni(); }
                else { JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento.", "Errore", JOptionPane.ERROR_MESSAGE); }
            } catch (DateTimeParseException ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Formato data non valido! Usare YYYY-MM-DDTHH:MM", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "La durata deve essere un numero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Errore imprevisto: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE); }
        }
    }


    private void gestisciEliminaSessioneInPresenza() {
        int result = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler eliminare la sessione:\n" + sessionePresenzaSelezionata.getTitoloSessione() + "?\n(L'operazione è irreversibile)", "Conferma Eliminazione", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            try {
                boolean successo = chefService.eliminaSessioneInPresenza(sessionePresenzaSelezionata);
                if (successo) { JOptionPane.showMessageDialog(this, "Sessione eliminata."); caricaDatiSessioni(); }
                else { JOptionPane.showMessageDialog(this, "Errore durante l'eliminazione.", "Errore", JOptionPane.ERROR_MESSAGE); }
            } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Errore imprevisto durante l'eliminazione: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void gestisciEliminaSessioneOnline() {
        int result = JOptionPane.showConfirmDialog( this, "Sei sicuro di voler eliminare la sessione:\n" + sessioneOnlineSelezionata.getTitoloSessione() + "?\n(L'operazione è irreversibile)", "Conferma Eliminazione", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
             try {
                boolean successo = chefService.eliminaSessioneOnline(sessioneOnlineSelezionata);
                if (successo) { JOptionPane.showMessageDialog(this, "Sessione eliminata."); caricaDatiSessioni(); }
                else { JOptionPane.showMessageDialog(this, "Errore durante l'eliminazione.", "Errore", JOptionPane.ERROR_MESSAGE); }
            } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Errore imprevisto durante l'eliminazione: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE); }
        }
    }
}