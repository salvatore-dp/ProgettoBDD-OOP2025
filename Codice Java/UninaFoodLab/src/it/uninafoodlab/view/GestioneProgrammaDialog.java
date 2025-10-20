package it.uninafoodlab.view;

import it.uninafoodlab.model.ProgrammaSessione;
import it.uninafoodlab.model.Ricetta;
import it.uninafoodlab.model.SessioneInPresenza;
import it.uninafoodlab.service.ChefService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDialog modale per gestire il programma (lista di ricette)
 * di una specifica SessioneInPresenza.
 */
public class GestioneProgrammaDialog extends JDialog {

    private ChefService chefService;
    private SessioneInPresenza sessione;

    // Lista delle ricette GIA' NEL PROGRAMMA
    private DefaultListModel<String> programmaListModel;
    private JList<String> listProgramma;
    private List<ProgrammaSessione> cacheProgramma;
    private ProgrammaSessione programmaSelezionato = null;
    
    // Lista di TUTTE le ricette dello chef (per la JComboBox)
    private List<Ricetta> cacheRicetteDisponibili;
    private JComboBox<String> ricettaComboBox;

    // Pulsanti
    private JButton btnRimuoviRicetta;
    private JButton btnAggiungiRicetta;

    // Campi per Aggiungi
    private JTextField porzioniField;


    public GestioneProgrammaDialog(Dialog owner, ChefService chefService, SessioneInPresenza sessione) {
        super(owner, "Gestisci Programma per: " + sessione.getTitoloSessione(), true); 
        
        this.chefService = chefService;
        this.sessione = sessione;
        this.cacheProgramma = new ArrayList<>();
        this.cacheRicetteDisponibili = new ArrayList<>();

        setSize(700, 350);
        setLocationRelativeTo(owner); 
        setLayout(new BorderLayout(10, 10));

        // --- Pannello Principale: diviso in due ---
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 0)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Pannello SINISTRO: Ricette nel Programma ---
        JPanel panelLista = new JPanel(new BorderLayout(5, 5));
        panelLista.setBorder(BorderFactory.createTitledBorder("Ricette nel Programma"));
        
        programmaListModel = new DefaultListModel<>();
        listProgramma = new JList<>(programmaListModel);
        listProgramma.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelLista.add(new JScrollPane(listProgramma), BorderLayout.CENTER);
        
        JPanel btnPanelLista = new JPanel(new FlowLayout());
        btnRimuoviRicetta = new JButton("Rimuovi");
        btnPanelLista.add(btnRimuoviRicetta);
        panelLista.add(btnPanelLista, BorderLayout.SOUTH);
        
        mainPanel.add(panelLista);

        // --- Pannello DESTRO: Aggiungi Ricetta ---
        JPanel panelAggiungi = new JPanel(new BorderLayout(5, 5));
        panelAggiungi.setBorder(BorderFactory.createTitledBorder("Aggiungi Ricetta al Programma"));
        
        JPanel formAggiungiPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        ricettaComboBox = new JComboBox<>();
        porzioniField = new JTextField("1.0");
        
        formAggiungiPanel.add(new JLabel("Ricetta:"));
        formAggiungiPanel.add(ricettaComboBox);
        formAggiungiPanel.add(new JLabel("Porzioni per Partecipante:"));
        formAggiungiPanel.add(porzioniField);
        
        btnAggiungiRicetta = new JButton("Aggiungi al Programma");
        
        panelAggiungi.add(formAggiungiPanel, BorderLayout.NORTH);
        panelAggiungi.add(btnAggiungiRicetta, BorderLayout.SOUTH);
        
        mainPanel.add(panelAggiungi);
        
        add(mainPanel, BorderLayout.CENTER);

        // --- Pulsante CHIUDI in basso ---
        JButton closeButton = new JButton("Fatto");
        closeButton.addActionListener(e -> dispose()); 
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        southPanel.add(closeButton);
        add(southPanel, BorderLayout.SOUTH);
        
        // --- Carica Dati e Aggiungi Listener ---
        caricaDatiProgramma();
        aggiungiListeners();
        
        // Stato iniziale pulsanti
        btnRimuoviRicetta.setEnabled(false);

        setVisible(true);
    }
    
    /**
     * Carica tutti i dati (ricette nel programma e ricette disponibili)
     */
    private void caricaDatiProgramma() {
        // 1. Pulisci tutto
        programmaListModel.clear();
        cacheProgramma.clear();
        ricettaComboBox.removeAllItems(); 
        cacheRicetteDisponibili.clear();
        
        // 2. Carica il programma GIA' ESISTENTE per la sessione (a sinistra)
        List<ProgrammaSessione> programma = chefService.getProgrammaDellaSessione(sessione);
        if (programma.isEmpty()) {
            programmaListModel.addElement("Nessuna ricetta programmata.");
        } else {
            for (ProgrammaSessione ps : programma) {
                programmaListModel.addElement(
                    ps.getTitoloRicetta() + " (" + 
                    ps.getPorzioniPerPartecipante() + " porz.)"
                );
                cacheProgramma.add(ps);
            }
        }
        
        // 3. Carica TUTTE le ricette dello chef (per la ComboBox a destra)
        cacheRicetteDisponibili = chefService.getMieRicette();
        for (Ricetta r : cacheRicetteDisponibili) {
            ricettaComboBox.addItem(r.getTitoloRicetta());
        }
        
        // 4. Resetta stato
        programmaSelezionato = null;
        btnRimuoviRicetta.setEnabled(false);
        listProgramma.clearSelection();
    }
    
    /**
     * Aggiunge tutti i listener ai pulsanti e alla lista
     */
    private void aggiungiListeners() {
        
        // Listener per la SELEZIONE della lista programma
        listProgramma.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = listProgramma.getSelectedIndex();
                if (selectedIndex != -1 && selectedIndex < cacheProgramma.size()) {
                    programmaSelezionato = cacheProgramma.get(selectedIndex);
                    btnRimuoviRicetta.setEnabled(true);
                } else {
                    programmaSelezionato = null;
                    btnRimuoviRicetta.setEnabled(false);
                }
            }
        });
        
        // Listener per AGGIUNGI ricetta
        btnAggiungiRicetta.addActionListener(e -> gestisciAggiungiRicetta());
        
        // Listener per RIMUOVI ricetta
        btnRimuoviRicetta.addActionListener(e -> {
             if (programmaSelezionato != null) {
                gestisciRimuoviRicetta();
            }
        });
    }
    
    
    private void gestisciAggiungiRicetta() {
        try {
            // 1. Recupera i dati dal form
            int selectedIndex = ricettaComboBox.getSelectedIndex();
            if (selectedIndex == -1) {
                 JOptionPane.showMessageDialog(this, "Nessuna ricetta selezionata.", "Errore", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            Ricetta ricettaSelezionata = cacheRicetteDisponibili.get(selectedIndex);
            
            String porzioniTesto = porzioniField.getText().trim();
            
            if (porzioniTesto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Specificare le porzioni per partecipante.", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double porzioni = Double.parseDouble(porzioniTesto);
            if (porzioni <= 0) {
                 JOptionPane.showMessageDialog(this, "Le porzioni devono essere un numero positivo.", "Errore", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            
            // 2. Chiama il service
            boolean successo = chefService.aggiungiRicettaAlProgramma(
                sessione, ricettaSelezionata, porzioni
            );
            
            // 3. Feedback e aggiorna
            if (successo) {
                JOptionPane.showMessageDialog(this, "Ricetta aggiunta al programma!");
                caricaDatiProgramma();
            } else {
                 JOptionPane.showMessageDialog(this, "Errore: Questa ricetta è già nel programma.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Le porzioni devono essere un numero valido (es. 1.5).", "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore imprevisto: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gestisciRimuoviRicetta() {
        int result = JOptionPane.showConfirmDialog(
            this, 
            "Rimuovere la ricetta '" + programmaSelezionato.getTitoloRicetta() + "' dal programma?",
            "Conferma Rimozione",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            boolean successo = chefService.rimuoviRicettaDalProgramma(programmaSelezionato);
            if (successo) {
                JOptionPane.showMessageDialog(this, "Ricetta rimossa dal programma.");
                caricaDatiProgramma();
            } else {
                JOptionPane.showMessageDialog(this, "Errore durante la rimozione.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}