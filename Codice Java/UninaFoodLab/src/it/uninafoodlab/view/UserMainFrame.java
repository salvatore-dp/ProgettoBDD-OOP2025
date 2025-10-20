package it.uninafoodlab.view;

import it.uninafoodlab.model.*;
import it.uninafoodlab.service.UserService;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class UserMainFrame extends JFrame {

    private Utente utente;
    private UserService userService;

    // Liste Corsi
    private DefaultListModel<String> corsiDisponibiliModel;
    private JList<String> listCorsiDisponibili;
    private List<Corso> cacheCorsiDisponibili;
    private Corso corsoDisponibileSelezionato = null;

    private DefaultListModel<String> mieiCorsiModel;
    private JList<String> listMieiCorsi;
    private List<Corso> cacheMieiCorsi;
    private Corso corsoIscrittoSelezionato = null;

    // Liste Sessioni
    private DefaultListModel<String> sessioniOnlineModel;
    private JList<String> listSessioniOnline;
    private List<SessioneOnline> cacheSessioniOnline;
    private SessioneOnline sessioneOnlineSelezionata = null;

    private DefaultListModel<String> sessioniPresenzaModel;
    private JList<String> listSessioniPresenza;
    private List<SessioneInPresenza> cacheSessioniPresenza;
    private SessioneInPresenza sessionePresenzaSelezionata = null;

    // Pulsanti
    private JButton iscrivitiButton;
    private JButton confermaButton;
    private JButton disiscrivitiButton;


    public UserMainFrame(Utente utente, UserService userService) {
        super("UninaFoodLab - Area Utente: " + utente.getUsername());
        this.utente = utente;
        this.userService = userService;
        this.cacheCorsiDisponibili = new ArrayList<>();
        this.cacheMieiCorsi = new ArrayList<>();
        this.cacheSessioniPresenza = new ArrayList<>();
        this.cacheSessioniOnline = new ArrayList<>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Pannello SUPERIORE: Liste Corsi ---
        JPanel panelCorsi = new JPanel(new GridLayout(1, 2, 10, 10)); panelCorsi.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JPanel panelDisponibili = new JPanel(new BorderLayout(10, 5)); panelDisponibili.setBorder(BorderFactory.createTitledBorder("Corsi Disponibili")); corsiDisponibiliModel = new DefaultListModel<>(); listCorsiDisponibili = new JList<>(corsiDisponibiliModel); listCorsiDisponibili.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); panelDisponibili.add(new JScrollPane(listCorsiDisponibili), BorderLayout.CENTER);
        JPanel bottomDisponibiliPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2)); iscrivitiButton = new JButton("Iscriviti al Corso Selezionato"); iscrivitiButton.setEnabled(false); bottomDisponibiliPanel.add(iscrivitiButton); panelDisponibili.add(bottomDisponibiliPanel, BorderLayout.SOUTH); panelCorsi.add(panelDisponibili);
        JPanel panelMieiCorsi = new JPanel(new BorderLayout(10, 5)); panelMieiCorsi.setBorder(BorderFactory.createTitledBorder("I Miei Corsi")); mieiCorsiModel = new DefaultListModel<>(); listMieiCorsi = new JList<>(mieiCorsiModel); listMieiCorsi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); panelMieiCorsi.add(new JScrollPane(listMieiCorsi), BorderLayout.CENTER);
        JPanel bottomMieiCorsiPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2)); disiscrivitiButton = new JButton("Disiscriviti dal Corso Selezionato"); disiscrivitiButton.setEnabled(false); bottomMieiCorsiPanel.add(disiscrivitiButton); panelMieiCorsi.add(bottomMieiCorsiPanel, BorderLayout.SOUTH);
        panelCorsi.add(panelMieiCorsi); add(panelCorsi, BorderLayout.NORTH);
        int w1 = iscrivitiButton.getPreferredSize().width; int w2 = disiscrivitiButton.getPreferredSize().width; int maxWidth = Math.max(w1, w2); Dimension preferredButtonSize = new Dimension(maxWidth, iscrivitiButton.getPreferredSize().height); iscrivitiButton.setPreferredSize(preferredButtonSize); disiscrivitiButton.setPreferredSize(preferredButtonSize);

        // --- Pannello CENTRALE: Liste Sessioni ---
        JPanel panelSessioni = new JPanel(new GridLayout(1, 2, 10, 10)); panelSessioni.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        JPanel panelOnline = new JPanel(new BorderLayout(10, 5)); panelOnline.setBorder(BorderFactory.createTitledBorder("Sessioni Online (del corso selezionato)")); sessioniOnlineModel = new DefaultListModel<>(); listSessioniOnline = new JList<>(sessioniOnlineModel); listSessioniOnline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); panelOnline.add(new JScrollPane(listSessioniOnline), BorderLayout.CENTER); panelSessioni.add(panelOnline);
        JPanel panelPresenza = new JPanel(new BorderLayout(10, 5)); panelPresenza.setBorder(BorderFactory.createTitledBorder("Sessioni Pratiche (del corso selezionato)")); sessioniPresenzaModel = new DefaultListModel<>(); listSessioniPresenza = new JList<>(sessioniPresenzaModel); listSessioniPresenza.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); panelPresenza.add(new JScrollPane(listSessioniPresenza), BorderLayout.CENTER); confermaButton = new JButton("Conferma / Annulla Partecipazione"); confermaButton.setEnabled(false); panelPresenza.add(confermaButton, BorderLayout.SOUTH); panelSessioni.add(panelPresenza);
        add(panelSessioni, BorderLayout.CENTER);

        // --- Pannello INFERIORE: Logout ---
        JButton logoutButton = new JButton("Logout"); JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); southPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); southPanel.add(logoutButton); add(southPanel, BorderLayout.SOUTH);

        logoutButton.addActionListener(e -> { new LoginFrame(); dispose(); });
        iscrivitiButton.addActionListener(e -> { if (corsoDisponibileSelezionato != null) gestisciIscrizione(); });
        confermaButton.addActionListener(e -> { if (sessionePresenzaSelezionata != null) gestisciConferma(); });
        disiscrivitiButton.addActionListener(e -> { if (corsoIscrittoSelezionato != null) gestisciDisiscrizione(); });
        listCorsiDisponibili.addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) { int i=listCorsiDisponibili.getSelectedIndex(); if (i!=-1 && i<cacheCorsiDisponibili.size()) { corsoDisponibileSelezionato=cacheCorsiDisponibili.get(i); iscrivitiButton.setEnabled(true); listMieiCorsi.clearSelection(); corsoIscrittoSelezionato=null; disiscrivitiButton.setEnabled(false); pulisciListeSessioni(); } else { corsoDisponibileSelezionato=null; iscrivitiButton.setEnabled(false); } } });
        listMieiCorsi.addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) { int i=listMieiCorsi.getSelectedIndex(); if (i!=-1 && i<cacheMieiCorsi.size()) { corsoIscrittoSelezionato=cacheMieiCorsi.get(i); caricaSessioniPerCorso(corsoIscrittoSelezionato); disiscrivitiButton.setEnabled(true); listCorsiDisponibili.clearSelection(); corsoDisponibileSelezionato=null; iscrivitiButton.setEnabled(false); } else { corsoIscrittoSelezionato=null; disiscrivitiButton.setEnabled(false); pulisciListeSessioni(); } } });
        listSessioniPresenza.addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) { int i=listSessioniPresenza.getSelectedIndex(); if (i!=-1 && i<cacheSessioniPresenza.size()) { sessionePresenzaSelezionata=cacheSessioniPresenza.get(i); confermaButton.setEnabled(true); Adesione a=userService.getMiaAdesionePerSessione(sessionePresenzaSelezionata); if (a!=null && a.getConfermata()!=null && a.getConfermata()) { confermaButton.setText("Annulla Partecipazione"); confermaButton.setBackground(Color.ORANGE); } else { confermaButton.setText("Conferma Partecipazione"); confermaButton.setBackground(Color.GREEN); } listSessioniOnline.clearSelection(); sessioneOnlineSelezionata=null; } else { sessionePresenzaSelezionata=null; confermaButton.setEnabled(false); confermaButton.setText("Conferma / Annulla Partecipazione"); confermaButton.setBackground(null); } } });
        listSessioniOnline.addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) { int i=listSessioniOnline.getSelectedIndex(); if (i!=-1 && i<cacheSessioniOnline.size()) { sessioneOnlineSelezionata=cacheSessioniOnline.get(i); listSessioniPresenza.clearSelection(); sessionePresenzaSelezionata=null; confermaButton.setEnabled(false); confermaButton.setText("Conferma / Annulla Partecipazione"); confermaButton.setBackground(null); } else { sessioneOnlineSelezionata=null; } } });
        listSessioniOnline.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { if (e.getClickCount()==2 && sessioneOnlineSelezionata!=null) { mostraDettagliSessioneOnline(sessioneOnlineSelezionata); } } });

        listSessioniPresenza.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && sessionePresenzaSelezionata != null) {
                    mostraDettagliSessionePresenza(sessionePresenzaSelezionata);
                }
            }
        });

        aggiornaListeCorsi();
        pulisciListeSessioni();
        setVisible(true);
    }

    private void aggiornaListeCorsi() {corsiDisponibiliModel.clear(); mieiCorsiModel.clear(); cacheCorsiDisponibili.clear(); cacheMieiCorsi.clear(); List<Corso> disponibili = userService.getCorsiDisponibili(); if (disponibili.isEmpty()) { corsiDisponibiliModel.addElement("Nessun corso disponibile al momento."); } else { disponibili.forEach(c -> { corsiDisponibiliModel.addElement(c.getTitolo() + " (Inizio: " + c.getDataInizio() + ")"); cacheCorsiDisponibili.add(c); }); } List<Corso> iscritti = userService.getMieiCorsiIscritto(); if (iscritti.isEmpty()) { mieiCorsiModel.addElement("Non sei iscritto a nessun corso."); } else { iscritti.forEach(c -> { mieiCorsiModel.addElement(c.getTitolo() + " (Iscritto)"); cacheMieiCorsi.add(c); }); } corsoDisponibileSelezionato = null; iscrivitiButton.setEnabled(false); listCorsiDisponibili.clearSelection(); listMieiCorsi.clearSelection(); disiscrivitiButton.setEnabled(false); pulisciListeSessioni(); }
    private void gestisciIscrizione() {int result = JOptionPane.showConfirmDialog(this, "Vuoi iscriverti al corso '" + corsoDisponibileSelezionato.getTitolo() + "'?", "Conferma Iscrizione", JOptionPane.YES_NO_OPTION); if (result == JOptionPane.YES_OPTION) { boolean successo = userService.iscrivitiACorso(corsoDisponibileSelezionato); if (successo) { JOptionPane.showMessageDialog(this, "Iscrizione effettuata con successo!"); aggiornaListeCorsi(); } else { JOptionPane.showMessageDialog(this, "Errore durante l'iscrizione.\nPotresti essere già iscritto o il corso non è più disponibile.", "Errore", JOptionPane.ERROR_MESSAGE); } } }
    private void pulisciListeSessioni() {sessioniOnlineModel.clear(); sessioniPresenzaModel.clear(); cacheSessioniPresenza.clear(); cacheSessioniOnline.clear(); sessioniOnlineModel.addElement("(Seleziona un corso a cui sei iscritto)"); sessioniPresenzaModel.addElement("(per vedere le sessioni)"); sessionePresenzaSelezionata = null; sessioneOnlineSelezionata = null; confermaButton.setEnabled(false); confermaButton.setText("Conferma / Annulla Partecipazione"); confermaButton.setBackground(null); }
    private void caricaSessioniPerCorso(Corso corso) {pulisciListeSessioni(); List<SessioneOnline> online = userService.getSessioniOnlineDelCorso(corso); if (online.isEmpty()) { sessioniOnlineModel.set(0, "Nessuna sessione online per questo corso."); } else { sessioniOnlineModel.remove(0); online.forEach(s -> { sessioniOnlineModel.addElement(s.getTitoloSessione() + " (" + s.getDataOra() + ")"); cacheSessioniOnline.add(s); }); } List<SessioneInPresenza> presenza = userService.getSessioniInPresenzaDelCorso(corso); if (presenza.isEmpty()) { sessioniPresenzaModel.set(0, "Nessuna sessione pratica per questo corso."); } else { sessioniPresenzaModel.remove(0); presenza.forEach(s -> { Adesione miaAdesione = userService.getMiaAdesionePerSessione(s); String stato = "(Non confermato)"; if (miaAdesione != null && miaAdesione.getConfermata() != null && miaAdesione.getConfermata()) { stato = "(CONFERMATO)"; } sessioniPresenzaModel.addElement(s.getTitoloSessione() + " " + stato); cacheSessioniPresenza.add(s); }); } }
    private void gestisciConferma() {Adesione miaAdesione = userService.getMiaAdesionePerSessione(sessionePresenzaSelezionata); boolean statoAttuale = (miaAdesione != null && miaAdesione.getConfermata() != null && miaAdesione.getConfermata()); boolean nuovoStato = !statoAttuale; String azione = nuovoStato ? "confermare" : "annullare"; int result = JOptionPane.showConfirmDialog(this, "Vuoi " + azione + " la partecipazione per la sessione:\n" + sessionePresenzaSelezionata.getTitoloSessione() + "?", "Conferma Azione", JOptionPane.YES_NO_OPTION); if (result == JOptionPane.YES_OPTION) { boolean successo = userService.aggiornaConfermaAdesione(sessionePresenzaSelezionata, nuovoStato); if (successo) { String msgSuccesso = nuovoStato ? "Partecipazione confermata!" : "Conferma annullata."; JOptionPane.showMessageDialog(this, msgSuccesso); if (corsoIscrittoSelezionato != null) caricaSessioniPerCorso(corsoIscrittoSelezionato); else pulisciListeSessioni(); } else { JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento della partecipazione.", "Errore", JOptionPane.ERROR_MESSAGE); } } }
    private void gestisciDisiscrizione() {int result = JOptionPane.showConfirmDialog(this, "Vuoi disiscriverti dal corso '" + corsoIscrittoSelezionato.getTitolo() + "'?\nPerderai l'accesso a tutte le sessioni.", "Conferma Disiscrizione", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); if (result == JOptionPane.YES_OPTION) { boolean successo = userService.disiscrivitiDaCorso(corsoIscrittoSelezionato); if (successo) { JOptionPane.showMessageDialog(this, "Disiscrizione effettuata con successo!"); aggiornaListeCorsi(); } else { JOptionPane.showMessageDialog(this, "Errore durante la disiscrizione.", "Errore", JOptionPane.ERROR_MESSAGE); } } }
    private void mostraDettagliSessioneOnline(SessioneOnline sessione) {JEditorPane editorPane = new JEditorPane(); editorPane.setContentType("text/html"); editorPane.setEditable(false); editorPane.setOpaque(false); String htmlContent = "<html><body><b>Titolo:</b> " + sessione.getTitoloSessione() + "<br><b>Data e Ora:</b> " + sessione.getDataOra() + "<br><b>Durata:</b> " + sessione.getDurata() + " minuti<br><b>Link:</b> <a href=\"" + sessione.getLinkVideo() + "\">" + sessione.getLinkVideo() + "</a></body></html>"; editorPane.setText(htmlContent); editorPane.addHyperlinkListener(e -> { if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) { if (Desktop.isDesktopSupported()) { try { Desktop.getDesktop().browse(e.getURL().toURI()); } catch (Exception ex) { JOptionPane.showMessageDialog(UserMainFrame.this, "Impossibile aprire il link.", "Errore", JOptionPane.ERROR_MESSAGE); } } else { JOptionPane.showMessageDialog(UserMainFrame.this, "Apertura automatica link non supportata.", "Errore", JOptionPane.WARNING_MESSAGE); } } }); JOptionPane.showMessageDialog(this, editorPane, "Dettagli Sessione Online", JOptionPane.INFORMATION_MESSAGE); }

    /**
     * Mostra un JOptionPane con i dettagli della sessione in presenza.
     */
    private void mostraDettagliSessionePresenza(SessioneInPresenza sessione) {
        // Recupera lo stato attuale dell'adesione
        Adesione miaAdesione = userService.getMiaAdesionePerSessione(sessione);
        String statoPartecipazione = "Non Confermato";
        if (miaAdesione != null && miaAdesione.getConfermata() != null && miaAdesione.getConfermata()) {
            statoPartecipazione = "CONFERMATO";
        }

        // Costruisci il messaggio
        String message = String.format(
            "<html><body>" +
            "<b>Titolo:</b> %s<br>" +
            "<b>Data e Ora:</b> %s<br>" +
            "<b>Durata:</b> %d minuti<br>" +
            "<b>Luogo:</b> %s<br>" +
            "<b>Posti Disponibili:</b> %d / %d<br>" +
            "<b>Tua Partecipazione:</b> %s" +
            "</body></html>",
            sessione.getTitoloSessione(),
            sessione.getDataOra(),
            sessione.getDurata(),
            sessione.getLuogo(),
            sessione.getPostiDisponibili(),
            sessione.getPostiTotali(),
            statoPartecipazione
        );

        // Mostra il popup
        JOptionPane.showMessageDialog(
            this,
            message, // Il messaggio HTML
            "Dettagli Sessione Pratica",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

}