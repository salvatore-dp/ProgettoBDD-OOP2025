package it.uninafoodlab.view;

import it.uninafoodlab.model.*; // Importa tutto
import it.uninafoodlab.service.ChefService;
import it.uninafoodlab.view.GestioneIngredientiDialog;
import it.uninafoodlab.view.GestioneProgrammaDialog;
import it.uninafoodlab.view.GestioneSessioniDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ChefMainFrame extends JFrame {

    private Chef chef;
    private ChefService chefService;
    private DefaultListModel<String> corsoListModel; private List<Corso> corsiCache; private Corso corsoSelezionato = null;
    private JButton modificaCorsoButton; private JButton eliminaCorsoButton; private JButton gestisciSessioniButton;
    private DefaultListModel<String> ricettaListModel; private List<Ricetta> ricetteCache; private Ricetta ricettaSelezionata = null;
    private JButton modificaRicettaButton; private JButton eliminaRicettaButton; private JButton gestisciIngredientiButton;
    private JComboBox<String> corsoReportComboBox; private JComboBox<String> sessioneReportComboBox; private List<Corso> cacheCorsiReport; private List<SessioneInPresenza> cacheSessioniReport;
    private JTextArea reportTextArea; private JButton generaReportButton;


    public ChefMainFrame(Chef chef, ChefService chefService) {
         super("UninaFoodLab - Area Chef: " + chef.getNome() + " " + chef.getCognome());
        this.chef = chef; this.chefService = chefService; this.corsiCache = new ArrayList<>(); this.ricetteCache = new ArrayList<>(); this.cacheCorsiReport = new ArrayList<>(); this.cacheSessioniReport = new ArrayList<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setSize(800, 600); setLocationRelativeTo(null);
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel panelCorsi = creaPannelloGestioneCorsi(); tabbedPane.addTab("Gestione Corsi", panelCorsi);
        JPanel panelRicette = creaPannelloGestioneRicette(); tabbedPane.addTab("Gestione Ricette", panelRicette);
        JPanel panelReport = creaPannelloReportIngredienti(); tabbedPane.addTab("Report Ingredienti", panelReport);
        add(tabbedPane, BorderLayout.CENTER);
        JButton logoutButton = new JButton("Logout"); logoutButton.addActionListener(e -> { new LoginFrame(); dispose(); });
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); southPanel.add(logoutButton); add(southPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    // ===================================================================
    // --- METODI GESTIONE CORSI ---
    // ===================================================================
    private JPanel creaPannelloGestioneCorsi() {JPanel panel = new JPanel(new BorderLayout(10, 10)); panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); JLabel titleLabel = new JLabel("I Miei Corsi"); titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); titleLabel.setHorizontalAlignment(SwingConstants.CENTER); panel.add(titleLabel, BorderLayout.NORTH); corsoListModel = new DefaultListModel<>(); JList<String> jlistCorsi = new JList<>(corsoListModel); jlistCorsi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); JScrollPane scrollPane = new JScrollPane(jlistCorsi); panel.add(scrollPane, BorderLayout.CENTER); JPanel buttonPanel = new JPanel(); buttonPanel.setLayout(new GridLayout(4, 1, 5, 5)); JButton creaCorsoButton = new JButton("Crea Nuovo Corso"); modificaCorsoButton = new JButton("Modifica Corso Selezionato"); eliminaCorsoButton = new JButton("Elimina Corso Selezionato"); gestisciSessioniButton = new JButton("Gestisci Sessioni del Corso"); buttonPanel.add(creaCorsoButton); buttonPanel.add(modificaCorsoButton); buttonPanel.add(eliminaCorsoButton); buttonPanel.add(new JSeparator()); buttonPanel.add(gestisciSessioniButton); panel.add(buttonPanel, BorderLayout.EAST); aggiornaListaCorsi(); creaCorsoButton.addActionListener(e -> gestisciCreazioneNuovoCorso()); modificaCorsoButton.addActionListener(e -> { if (corsoSelezionato != null) gestisciModificaCorso(); }); eliminaCorsoButton.addActionListener(e -> { if (corsoSelezionato != null) gestisciEliminaCorso(); }); jlistCorsi.addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) { int i = jlistCorsi.getSelectedIndex(); if (i != -1 && i < corsiCache.size()) { corsoSelezionato = corsiCache.get(i); modificaCorsoButton.setEnabled(true); eliminaCorsoButton.setEnabled(true); gestisciSessioniButton.setEnabled(true); } else { corsoSelezionato = null; modificaCorsoButton.setEnabled(false); eliminaCorsoButton.setEnabled(false); gestisciSessioniButton.setEnabled(false); } } }); gestisciSessioniButton.addActionListener(e -> { if (corsoSelezionato != null) { new GestioneSessioniDialog(this, chefService, corsoSelezionato); } else { JOptionPane.showMessageDialog(this, "Nessun corso selezionato.", "Errore", JOptionPane.WARNING_MESSAGE); } }); return panel; }
    private void aggiornaListaCorsi() {corsoListModel.clear(); corsiCache.clear(); List<Corso> c = chefService.getMieiCorsi(); if (c.isEmpty()) { corsoListModel.addElement("Non hai ancora creato nessun corso."); } else { c.forEach(co -> { corsoListModel.addElement(co.getTitolo() + " (Inizio: " + co.getDataInizio() + ")"); corsiCache.add(co); }); } corsoSelezionato = null; modificaCorsoButton.setEnabled(false); eliminaCorsoButton.setEnabled(false); gestisciSessioniButton.setEnabled(false); }
    private void gestisciCreazioneNuovoCorso() {JPanel p = new JPanel(new GridLayout(3, 2, 5, 5)); JTextField tf=new JTextField(), ff=new JTextField(), df=new JTextField(LocalDate.now().toString()); p.add(new JLabel("Titolo:"));p.add(tf);p.add(new JLabel("Frequenza:"));p.add(ff);p.add(new JLabel("Data Inizio (YYYY-MM-DD):"));p.add(df); int r=JOptionPane.showConfirmDialog(this,p,"Crea Nuovo Corso",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE); if(r==JOptionPane.OK_OPTION){try{String t=tf.getText().trim(), f=ff.getText().trim(), dt=df.getText().trim(); if(t.isEmpty()||f.isEmpty()||dt.isEmpty()){JOptionPane.showMessageDialog(this,"Campi obbligatori!","Errore",JOptionPane.ERROR_MESSAGE);return;} LocalDate di=LocalDate.parse(dt); if(chefService.creaNuovoCorso(t,f,di)){JOptionPane.showMessageDialog(this,"Corso creato!");aggiornaListaCorsi();}else{JOptionPane.showMessageDialog(this,"Errore creazione.\n(Titolo già esistente?).","Errore",JOptionPane.ERROR_MESSAGE);}}catch(DateTimeParseException ex){JOptionPane.showMessageDialog(this,"Formato data non valido!","Errore",JOptionPane.ERROR_MESSAGE);}catch(Exception ex){JOptionPane.showMessageDialog(this,"Errore: "+ex.getMessage(),"Errore",JOptionPane.ERROR_MESSAGE);}}}
    private void gestisciModificaCorso() {JPanel p=new JPanel(new GridLayout(3,2,5,5)); JTextField tf=new JTextField(corsoSelezionato.getTitolo()); tf.setEnabled(false); JTextField ff=new JTextField(corsoSelezionato.getFrequenza()), df=new JTextField(corsoSelezionato.getDataInizio().toString()); p.add(new JLabel("Titolo (non modif.):"));p.add(tf);p.add(new JLabel("Frequenza:"));p.add(ff);p.add(new JLabel("Data Inizio (YYYY-MM-DD):"));p.add(df); int r=JOptionPane.showConfirmDialog(this,p,"Modifica Corso",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE); if(r==JOptionPane.OK_OPTION){try{String f=ff.getText().trim(), dt=df.getText().trim(); if(f.isEmpty()||dt.isEmpty()){JOptionPane.showMessageDialog(this,"Campi obbligatori!","Errore",JOptionPane.ERROR_MESSAGE);return;} LocalDate di=LocalDate.parse(dt); corsoSelezionato.setFrequenza(f); corsoSelezionato.setDataInizio(di); if(chefService.aggiornaCorso(corsoSelezionato)){JOptionPane.showMessageDialog(this,"Corso aggiornato!");aggiornaListaCorsi();}else{JOptionPane.showMessageDialog(this,"Errore aggiornamento.","Errore",JOptionPane.ERROR_MESSAGE);}}catch(DateTimeParseException ex){JOptionPane.showMessageDialog(this,"Formato data non valido!","Errore",JOptionPane.ERROR_MESSAGE);}catch(Exception ex){JOptionPane.showMessageDialog(this,"Errore: "+ex.getMessage(),"Errore",JOptionPane.ERROR_MESSAGE);}}}
    private void gestisciEliminaCorso() {int r=JOptionPane.showConfirmDialog(this,"Eliminare corso "+corsoSelezionato.getTitolo()+"?\nATTENZIONE: Elimina anche sessioni, iscrizioni, adesioni!","Conferma",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE); if(r==JOptionPane.YES_OPTION){try{if(chefService.eliminaCorso(corsoSelezionato)){JOptionPane.showMessageDialog(this,"Corso eliminato.");aggiornaListaCorsi();}else{JOptionPane.showMessageDialog(this,"Errore eliminazione.","Errore",JOptionPane.ERROR_MESSAGE);}}catch(Exception ex){JOptionPane.showMessageDialog(this,"Errore: "+ex.getMessage(),"Errore",JOptionPane.ERROR_MESSAGE);}}}

    // ===================================================================
    // --- METODI GESTIONE RICETTE (COMPLETI) ---
    // ===================================================================
    private JPanel creaPannelloGestioneRicette() {JPanel panel = new JPanel(new BorderLayout(10, 10)); panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); JLabel titleLabel = new JLabel("Le Mie Ricette"); titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); titleLabel.setHorizontalAlignment(SwingConstants.CENTER); panel.add(titleLabel, BorderLayout.NORTH); ricettaListModel = new DefaultListModel<>(); JList<String> jlistRicette = new JList<>(ricettaListModel); jlistRicette.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); JScrollPane scrollPane = new JScrollPane(jlistRicette); panel.add(scrollPane, BorderLayout.CENTER); JPanel buttonPanel = new JPanel(); buttonPanel.setLayout(new GridLayout(4, 1, 5, 5)); JButton creaRicettaButton = new JButton("Crea Nuova Ricetta"); modificaRicettaButton = new JButton("Modifica Ricetta Selezionata"); eliminaRicettaButton = new JButton("Elimina Ricetta Selezionata"); gestisciIngredientiButton = new JButton("Gestisci Ingredienti Ricetta"); buttonPanel.add(creaRicettaButton); buttonPanel.add(modificaRicettaButton); buttonPanel.add(eliminaRicettaButton); buttonPanel.add(new JSeparator()); buttonPanel.add(gestisciIngredientiButton); panel.add(buttonPanel, BorderLayout.EAST); aggiornaListaRicette(); creaRicettaButton.addActionListener(e -> gestisciCreazioneNuovaRicetta()); modificaRicettaButton.addActionListener(e -> { if (ricettaSelezionata != null) gestisciModificaRicetta(); }); eliminaRicettaButton.addActionListener(e -> { if (ricettaSelezionata != null) gestisciEliminaRicetta(); }); gestisciIngredientiButton.addActionListener(e -> { if (ricettaSelezionata != null) { new GestioneIngredientiDialog(this, chefService, ricettaSelezionata); aggiornaListaRicette(); } }); jlistRicette.addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) { int i = jlistRicette.getSelectedIndex(); if (i != -1 && i < ricetteCache.size()) { ricettaSelezionata = ricetteCache.get(i); modificaRicettaButton.setEnabled(true); eliminaRicettaButton.setEnabled(true); gestisciIngredientiButton.setEnabled(true); } else { ricettaSelezionata = null; modificaRicettaButton.setEnabled(false); eliminaRicettaButton.setEnabled(false); gestisciIngredientiButton.setEnabled(false); } } }); return panel; }
    private void aggiornaListaRicette() {ricettaListModel.clear(); ricetteCache.clear(); List<Ricetta> r = chefService.getMieRicette(); if (r.isEmpty()) { ricettaListModel.addElement("Non hai ancora creato nessuna ricetta."); } else { r.forEach(ri -> { ricettaListModel.addElement(ri.getTitoloRicetta() + " (" + ri.getDifficolta() + ", " + ri.getTempoPrep() + " min)"); ricetteCache.add(ri); }); } ricettaSelezionata = null; modificaRicettaButton.setEnabled(false); eliminaRicettaButton.setEnabled(false); gestisciIngredientiButton.setEnabled(false); }
    private void gestisciCreazioneNuovaRicetta() {JPanel fp=new JPanel(new BorderLayout(5,5)); JPanel f=new JPanel(new GridLayout(4,2,5,5)); JTextField tf=new JTextField(); String[] dO={"facile","medio","difficile"}; JComboBox<String> db=new JComboBox<>(dO); JTextField tpf=new JTextField("30"), pf=new JTextField("4"); f.add(new JLabel("Titolo Ricetta:"));f.add(tf); f.add(new JLabel("Difficoltà:"));f.add(db); f.add(new JLabel("Tempo Prep. (minuti):"));f.add(tpf); f.add(new JLabel("Porzioni:"));f.add(pf); JPanel dp=new JPanel(new BorderLayout()); dp.add(new JLabel("Descrizione:"),BorderLayout.NORTH); JTextArea da=new JTextArea(5,30); dp.add(new JScrollPane(da),BorderLayout.CENTER); fp.add(f,BorderLayout.NORTH); fp.add(dp,BorderLayout.CENTER); int res=JOptionPane.showConfirmDialog(this,fp,"Crea Nuova Ricetta",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE); if(res==JOptionPane.OK_OPTION){try{String t=tf.getText().trim(), desc=da.getText().trim(), diff=(String)db.getSelectedItem(), tt=tpf.getText().trim(), pt=pf.getText().trim(); if(t.isEmpty()||tt.isEmpty()||pt.isEmpty()||diff==null){JOptionPane.showMessageDialog(this,"Titolo, Difficoltà, Tempo, Porzioni obbligatori!","Errore",JOptionPane.ERROR_MESSAGE);return;} int tp=Integer.parseInt(tt), p=Integer.parseInt(pt); if(tp<=0||p<=0){JOptionPane.showMessageDialog(this,"Tempo e Porzioni positivi.","Errore",JOptionPane.ERROR_MESSAGE);return;} if(chefService.creaNuovaRicetta(t,desc,diff,tp,p)){JOptionPane.showMessageDialog(this,"Ricetta creata!");aggiornaListaRicette();}else{JOptionPane.showMessageDialog(this,"Errore creazione.\n(Titolo già esistente?).","Errore",JOptionPane.ERROR_MESSAGE);}}catch(NumberFormatException ex){JOptionPane.showMessageDialog(this,"Tempo e Porzioni numerici.","Errore",JOptionPane.ERROR_MESSAGE);}catch(Exception ex){JOptionPane.showMessageDialog(this,"Errore: "+ex.getMessage(),"Errore",JOptionPane.ERROR_MESSAGE);}}}
    private void gestisciModificaRicetta() {JPanel fp=new JPanel(new BorderLayout(5,5)); JPanel f=new JPanel(new GridLayout(4,2,5,5)); JTextField tf=new JTextField(ricettaSelezionata.getTitoloRicetta()); tf.setEnabled(false); String[] dO={"facile","medio","difficile"}; JComboBox<String> db=new JComboBox<>(dO); db.setSelectedItem(ricettaSelezionata.getDifficolta()); JTextField tpf=new JTextField(String.valueOf(ricettaSelezionata.getTempoPrep())), pf=new JTextField(String.valueOf(ricettaSelezionata.getPorzioni())); f.add(new JLabel("Titolo (non modif.):"));f.add(tf); f.add(new JLabel("Difficoltà:"));f.add(db); f.add(new JLabel("Tempo Prep. (minuti):"));f.add(tpf); f.add(new JLabel("Porzioni:"));f.add(pf); JPanel dp=new JPanel(new BorderLayout()); dp.add(new JLabel("Descrizione:"),BorderLayout.NORTH); JTextArea da=new JTextArea(5,30); da.setText(ricettaSelezionata.getDescrizione()); dp.add(new JScrollPane(da),BorderLayout.CENTER); fp.add(f,BorderLayout.NORTH); fp.add(dp,BorderLayout.CENTER); int res=JOptionPane.showConfirmDialog(this,fp,"Modifica Ricetta",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE); if(res==JOptionPane.OK_OPTION){try{String desc=da.getText().trim(), diff=(String)db.getSelectedItem(), tt=tpf.getText().trim(), pt=pf.getText().trim(); if(tt.isEmpty()||pt.isEmpty()||diff==null){JOptionPane.showMessageDialog(this,"Difficoltà, Tempo, Porzioni obbligatori!","Errore",JOptionPane.ERROR_MESSAGE);return;} int tp=Integer.parseInt(tt), p=Integer.parseInt(pt); if(tp<=0||p<=0){JOptionPane.showMessageDialog(this,"Tempo e Porzioni positivi.","Errore",JOptionPane.ERROR_MESSAGE);return;} ricettaSelezionata.setDescrizione(desc); ricettaSelezionata.setDifficolta(diff); ricettaSelezionata.setTempoPrep(tp); ricettaSelezionata.setPorzioni(p); if(chefService.aggiornaRicetta(ricettaSelezionata)){JOptionPane.showMessageDialog(this,"Ricetta aggiornata!");aggiornaListaRicette();}else{JOptionPane.showMessageDialog(this,"Errore aggiornamento.","Errore",JOptionPane.ERROR_MESSAGE);}}catch(NumberFormatException ex){JOptionPane.showMessageDialog(this,"Tempo e Porzioni numerici.","Errore",JOptionPane.ERROR_MESSAGE);}catch(Exception ex){JOptionPane.showMessageDialog(this,"Errore: "+ex.getMessage(),"Errore",JOptionPane.ERROR_MESSAGE);}}}
    private void gestisciEliminaRicetta() {int r=JOptionPane.showConfirmDialog(this,"Eliminare ricetta "+ricettaSelezionata.getTitoloRicetta()+"?\nATTENZIONE: Verrà rimossa da sessioni e ingredienti!","Conferma",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE); if(r==JOptionPane.YES_OPTION){try{if(chefService.eliminaRicetta(ricettaSelezionata)){JOptionPane.showMessageDialog(this,"Ricetta eliminata.");aggiornaListaRicette();}else{JOptionPane.showMessageDialog(this,"Errore eliminazione.","Errore",JOptionPane.ERROR_MESSAGE);}}catch(Exception ex){JOptionPane.showMessageDialog(this,"Errore: "+ex.getMessage(),"Errore",JOptionPane.ERROR_MESSAGE);}}}

    // ===================================================================
    // --- METODI REPORT INGREDIENTI ---
    // ===================================================================
    private JPanel creaPannelloReportIngredienti() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Pannello SUPERIORE: Selettori con BoxLayout ---
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.X_AXIS));

        selectionPanel.add(new JLabel("Corso:"));
        corsoReportComboBox = new JComboBox<>();
        corsoReportComboBox.setMaximumSize(new Dimension(200, corsoReportComboBox.getPreferredSize().height)); // Limita larghezza
        selectionPanel.add(Box.createHorizontalStrut(5));
        selectionPanel.add(corsoReportComboBox);

        selectionPanel.add(Box.createHorizontalStrut(15));

        selectionPanel.add(new JLabel("Sessione:"));
        sessioneReportComboBox = new JComboBox<>();
        sessioneReportComboBox.setMaximumSize(new Dimension(220, sessioneReportComboBox.getPreferredSize().height)); // Limita larghezza
        sessioneReportComboBox.setEnabled(false);
        selectionPanel.add(Box.createHorizontalStrut(5));
        selectionPanel.add(sessioneReportComboBox);

        selectionPanel.add(Box.createHorizontalStrut(10));

        generaReportButton = new JButton("Genera Lista");
        generaReportButton.setEnabled(false);
        selectionPanel.add(generaReportButton);

        panel.add(selectionPanel, BorderLayout.NORTH);

        // --- Area CENTRALE: Risultato Report ---
        reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);
        reportTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panel.add(new JScrollPane(reportTextArea), BorderLayout.CENTER);

        // --- Popola ComboBox Corsi ---
        popolaComboCorsiReport();

        // --- LISTENERS ---
        corsoReportComboBox.addItemListener(e -> { if (e.getStateChange() == ItemEvent.SELECTED) popolaComboSessioniReport(); });
        sessioneReportComboBox.addItemListener(e -> { if (e.getStateChange() == ItemEvent.SELECTED) generaReportButton.setEnabled(sessioneReportComboBox.getSelectedIndex() > 0); });
        generaReportButton.addActionListener(e -> generaReport());

        return panel;
    }

    private void popolaComboCorsiReport() {corsoReportComboBox.removeAllItems(); cacheCorsiReport.clear(); sessioneReportComboBox.removeAllItems(); cacheSessioniReport.clear(); reportTextArea.setText(""); sessioneReportComboBox.setEnabled(false); generaReportButton.setEnabled(false); corsoReportComboBox.addItem("-- Seleziona un corso --"); List<Corso> c = chefService.getMieiCorsi(); cacheCorsiReport.addAll(c); c.forEach(co -> corsoReportComboBox.addItem(co.getTitolo())); }
    private void popolaComboSessioniReport() {sessioneReportComboBox.removeAllItems(); cacheSessioniReport.clear(); reportTextArea.setText(""); generaReportButton.setEnabled(false); int i = corsoReportComboBox.getSelectedIndex(); if (i <= 0) { sessioneReportComboBox.setEnabled(false); sessioneReportComboBox.addItem("-- Prima seleziona un corso --"); return; } Corso cs = cacheCorsiReport.get(i - 1); sessioneReportComboBox.addItem("-- Seleziona una sessione --"); sessioneReportComboBox.setEnabled(true); List<SessioneInPresenza> s = chefService.getSessioniInPresenzaDelCorso(cs); cacheSessioniReport.addAll(s); if (s.isEmpty()) { sessioneReportComboBox.addItem("Nessuna sessione pratica trovata"); sessioneReportComboBox.setEnabled(false); } else { s.forEach(se -> sessioneReportComboBox.addItem(se.getTitoloSessione() + " (" + se.getDataOra().toLocalDate() + ")")); } }

    private void generaReport() {
        System.out.println("DEBUG Report: Bottone 'Genera' cliccato.");
        int selectedSessioneIndex = sessioneReportComboBox.getSelectedIndex();
        System.out.println("DEBUG Report: Indice sessione selezionato: " + selectedSessioneIndex);

        if (selectedSessioneIndex <= 0) { System.out.println("DEBUG Report: Nessuna sessione valida selezionata."); reportTextArea.setText("Seleziona una sessione valida per generare il report."); return; }

        SessioneInPresenza sessioneSelezionataReport = null;
        if(selectedSessioneIndex - 1 < cacheSessioniReport.size()){
             sessioneSelezionataReport = cacheSessioniReport.get(selectedSessioneIndex - 1);
             System.out.println("DEBUG Report: Sessione selezionata: " + sessioneSelezionataReport.getTitoloSessione());
        } else { System.err.println("DEBUG Report: ERRORE! Indice sessione fuori dai limiti della cache."); reportTextArea.setText("Errore: Indice sessione non valido."); return; }

        try {
            reportTextArea.setText("Calcolo in corso..."); System.out.println("DEBUG Report: Chiamo chefService.calcolaIngredientiPerSessione...");
            Map<String, String> listaSpesa = chefService.calcolaIngredientiPerSessione(sessioneSelezionataReport);
            System.out.println("DEBUG Report: Mappa risultato dal service: " + listaSpesa);

            if (listaSpesa == null || listaSpesa.isEmpty()) {
                 System.out.println("DEBUG Report: Lista spesa vuota o null ritornata dal service.");
                 reportTextArea.setText("Nessun ingrediente necessario per la sessione selezionata.\n"
                                     + "(Possibili cause: nessun partecipante confermato, nessuna ricetta programmata, errore nel calcolo).");
            } else {
                StringBuilder report = new StringBuilder(); report.append("Lista della Spesa per: ").append(sessioneSelezionataReport.getTitoloSessione()).append(" (Corso: ").append(sessioneSelezionataReport.getTitoloCorso()).append(")\n"); report.append("====================================================\n\n");
                List<String> ingredientiOrdinati = new ArrayList<>(listaSpesa.keySet()); ingredientiOrdinati.sort(String.CASE_INSENSITIVE_ORDER);
                for (String ingrediente : ingredientiOrdinati) { report.append(String.format("- %-30s : %s\n", ingrediente, listaSpesa.get(ingrediente))); }
                reportTextArea.setText(report.toString()); reportTextArea.setCaretPosition(0); System.out.println("DEBUG Report: Report visualizzato.");
            }
        } catch (Exception ex) {
            System.err.println("DEBUG Report: Eccezione durante la generazione del report!");
            reportTextArea.setText("Errore durante la generazione del report:\n" + ex.getMessage()); ex.printStackTrace();
        }
    }

}