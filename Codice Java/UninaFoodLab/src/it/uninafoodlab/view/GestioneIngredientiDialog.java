package it.uninafoodlab.view;

import it.uninafoodlab.model.ComposizioneRicetta;
import it.uninafoodlab.model.Ingrediente;
import it.uninafoodlab.model.Ricetta;
import it.uninafoodlab.service.ChefService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter; 
import java.awt.event.MouseEvent;   
import java.util.ArrayList; 
import java.util.List;

/**
 * Un JDialog modale per gestire la lista di ingredienti
 */
@SuppressWarnings("unused")
public class GestioneIngredientiDialog extends JDialog {

    private ChefService chefService;
    private Ricetta ricetta;

    // Lista degli ingredienti GIÀ INSERITI nella ricetta
    private DefaultListModel<String> composizioneListModel;
    private JList<String> listComposizione;
    private List<ComposizioneRicetta> cacheComposizione;
    private ComposizioneRicetta composizioneSelezionata = null;
    
    // Lista di TUTTI gli ingredienti "base" (per la JComboBox)
    private List<Ingrediente> cacheIngredientiDisponibili;
    private JComboBox<String> ingredienteComboBox;

    // Pulsanti
    private JButton btnModificaQuantita;
    private JButton btnRimuoviIngrediente;
    private JButton btnAggiungiIngrediente;

    // Campi per Aggiungi/Modifica
    private JTextField quantitaField;
    private JTextField unitaField;


    public GestioneIngredientiDialog(Frame owner, ChefService chefService, Ricetta ricetta) {
        super(owner, "Gestione Ingredienti per: " + ricetta.getTitoloRicetta(), true); 
        
        this.chefService = chefService;
        this.ricetta = ricetta;
        this.cacheComposizione = new ArrayList<>();
        this.cacheIngredientiDisponibili = new ArrayList<>();

        setSize(750, 400);
        setLocationRelativeTo(owner); 
        setLayout(new BorderLayout(10, 10));

        // --- Pannello Principale: diviso in due (Lista e Form Aggiungi) ---
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 0)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Pannello SINISTRO: Ingredienti Attuali ---
        JPanel panelLista = new JPanel(new BorderLayout(5, 5));
        panelLista.setBorder(BorderFactory.createTitledBorder("Ingredienti nella Ricetta"));
        
        composizioneListModel = new DefaultListModel<>();
        listComposizione = new JList<>(composizioneListModel);
        listComposizione.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelLista.add(new JScrollPane(listComposizione), BorderLayout.CENTER);
        
        JPanel btnPanelLista = new JPanel(new FlowLayout());
        btnModificaQuantita = new JButton("Modifica Quantità");
        btnRimuoviIngrediente = new JButton("Rimuovi");
        btnPanelLista.add(btnModificaQuantita);
        btnPanelLista.add(btnRimuoviIngrediente);
        panelLista.add(btnPanelLista, BorderLayout.SOUTH);
        
        mainPanel.add(panelLista);

        // --- Pannello DESTRO: Aggiungi Ingrediente ---
        JPanel panelAggiungi = new JPanel(new BorderLayout(5, 5));
        panelAggiungi.setBorder(BorderFactory.createTitledBorder("Aggiungi Ingrediente"));
        
        // Pannello del form
        JPanel formAggiungiPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        ingredienteComboBox = new JComboBox<>();
        quantitaField = new JTextField("100");
        unitaField = new JTextField("g"); // es. g, ml, pz
        
        // 1. Creiamo il pulsante "+"
        JButton btnPlus = new JButton("+");
        btnPlus.setMargin(new Insets(2, 4, 2, 4));
        
        // 2. Creiamo un pannello per ComboBox + Pulsante
        JPanel comboPanel = new JPanel(new BorderLayout(5, 0));
        comboPanel.add(ingredienteComboBox, BorderLayout.CENTER);
        comboPanel.add(btnPlus, BorderLayout.EAST);
        
        // 3. Aggiungiamo i componenti al form
        formAggiungiPanel.add(new JLabel("Ingrediente:"));
        formAggiungiPanel.add(comboPanel);
        formAggiungiPanel.add(new JLabel("Quantità:"));
        formAggiungiPanel.add(quantitaField);
        formAggiungiPanel.add(new JLabel("Unità:"));
        formAggiungiPanel.add(unitaField);
        
        btnAggiungiIngrediente = new JButton("Aggiungi alla Ricetta");
        
        panelAggiungi.add(formAggiungiPanel, BorderLayout.NORTH);
        panelAggiungi.add(btnAggiungiIngrediente, BorderLayout.SOUTH);
        
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
        caricaDatiIngredienti();
        aggiungiListeners();
        
        // Aggiungi listener per il nuovo pulsante "+"
        btnPlus.addActionListener(e -> gestisciCreazioneNuovoIngrediente());
        
        // Stato iniziale pulsanti
        btnModificaQuantita.setEnabled(false);
        btnRimuoviIngrediente.setEnabled(false);

        setVisible(true);
    }
    
    /**
     * Carica tutti i dati (ingredienti della ricetta e ingredienti disponibili)
     */
    private void caricaDatiIngredienti() {
        // 1. Pulisci tutto
        composizioneListModel.clear();
        cacheComposizione.clear();
        ingredienteComboBox.removeAllItems();
        cacheIngredientiDisponibili.clear();
        
        // 2. Carica la lista degli ingredienti GIA' NELLA ricetta (a sinistra)
        List<ComposizioneRicetta> composizioni = chefService.getIngredientiDellaRicetta(ricetta);
        if (composizioni.isEmpty()) {
            composizioneListModel.addElement("Nessun ingrediente in questa ricetta.");
        } else {
            for (ComposizioneRicetta c : composizioni) {
                composizioneListModel.addElement(
                    c.getNomeIngrediente() + ": " + 
                    c.getQuantitaIngrediente() + " " + 
                    c.getUnitaSpecIngrediente()
                );
                cacheComposizione.add(c);
            }
        }
        
        // 3. Carica TUTTI gli ingredienti disponibili (per la ComboBox a destra)
        cacheIngredientiDisponibili = chefService.getAllIngredientiDisponibili();
        for (Ingrediente ing : cacheIngredientiDisponibili) {
            ingredienteComboBox.addItem(ing.getNomeIngrediente());
        }
        
        // 4. Resetta stato
        composizioneSelezionata = null;
        btnModificaQuantita.setEnabled(false);
        btnRimuoviIngrediente.setEnabled(false);
        listComposizione.clearSelection();
    }
    
    /**
     * Aggiunge tutti i listener ai pulsanti e alla lista
     */
    private void aggiungiListeners() {
        
        // Listener per la SELEZIONE della lista ingredienti
        listComposizione.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = listComposizione.getSelectedIndex();
                if (selectedIndex != -1 && selectedIndex < cacheComposizione.size()) {
                    composizioneSelezionata = cacheComposizione.get(selectedIndex);
                    btnModificaQuantita.setEnabled(true);
                    btnRimuoviIngrediente.setEnabled(true);
                } else {
                    composizioneSelezionata = null;
                    btnModificaQuantita.setEnabled(false);
                    btnRimuoviIngrediente.setEnabled(false);
                }
            }
        });
        
        // Listener per AGGIUNGI ingrediente
        btnAggiungiIngrediente.addActionListener(e -> gestisciAggiungiIngrediente());
        
        // Listener per MODIFICA quantità
        btnModificaQuantita.addActionListener(e -> {
            if (composizioneSelezionata != null) {
                gestisciModificaQuantita();
            }
        });
        
        // Listener per RIMUOVI ingrediente
        btnRimuoviIngrediente.addActionListener(e -> {
             if (composizioneSelezionata != null) {
                gestisciRimuoviIngrediente();
            }
        });
    }
        
    private void gestisciAggiungiIngrediente() {
        try {
            int selectedIndex = ingredienteComboBox.getSelectedIndex();
            if (selectedIndex == -1) {
                 JOptionPane.showMessageDialog(this, "Nessun ingrediente selezionato.", "Errore", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            Ingrediente ingredienteSelezionato = cacheIngredientiDisponibili.get(selectedIndex);
            String quantitaTesto = quantitaField.getText().trim();
            String unitaTesto = unitaField.getText().trim();
            if (quantitaTesto.isEmpty() || unitaTesto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Quantità e Unità sono obbligatori.", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double quantita = Double.parseDouble(quantitaTesto);
            if (quantita <= 0) {
                 JOptionPane.showMessageDialog(this, "La quantità deve essere positiva.", "Errore", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            boolean successo = chefService.aggiungiIngredienteARicetta(
                ricetta, ingredienteSelezionato, quantita, unitaTesto
            );
            if (successo) {
                JOptionPane.showMessageDialog(this, "Ingrediente aggiunto!");
                caricaDatiIngredienti();
            } else {
                 JOptionPane.showMessageDialog(this, "Errore: Questo ingrediente è già presente nella ricetta.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La quantità deve essere un numero valido (es. 100.5).", "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore imprevisto: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gestisciModificaQuantita() {
        String quantitaPrecedente = String.valueOf(composizioneSelezionata.getQuantitaIngrediente());
        String unitaPrecedente = composizioneSelezionata.getUnitaSpecIngrediente();
        String quantitaTesto = JOptionPane.showInputDialog(
            this, "Modifica quantità per: " + composizioneSelezionata.getNomeIngrediente(), 
            quantitaPrecedente
        );
        if (quantitaTesto == null) return; 
        String unitaTesto = JOptionPane.showInputDialog(
            this, "Modifica unità per: " + composizioneSelezionata.getNomeIngrediente(), 
            unitaPrecedente
        );
        if (unitaTesto == null) return; 
        try {
            double quantita = Double.parseDouble(quantitaTesto.trim());
            if (quantita <= 0) {
                 JOptionPane.showMessageDialog(this, "La quantità deve essere positiva.", "Errore", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            composizioneSelezionata.setQuantitaIngrediente(quantita);
            composizioneSelezionata.setUnitaSpecIngrediente(unitaTesto.trim());
            boolean successo = chefService.aggiornaIngredienteInRicetta(composizioneSelezionata);
            if (successo) {
                JOptionPane.showMessageDialog(this, "Quantità aggiornata!");
                caricaDatiIngredienti();
            } else {
                JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La quantità deve essere un numero valido (es. 100.5).", "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore imprevisto: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gestisciRimuoviIngrediente() {
        int result = JOptionPane.showConfirmDialog(
            this, 
            "Rimuovere " + composizioneSelezionata.getNomeIngrediente() + " dalla ricetta?",
            "Conferma Eliminazione",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        if (result == JOptionPane.YES_OPTION) {
            boolean successo = chefService.rimuoviIngredienteDaRicetta(composizioneSelezionata);
            if (successo) {
                JOptionPane.showMessageDialog(this, "Ingrediente rimosso.");
                caricaDatiIngredienti();
            } else {
                JOptionPane.showMessageDialog(this, "Errore durante l'eliminazione.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
        
    /**
     * Apre un popup per creare un nuovo Ingrediente "base" nel database
     */
    private void gestisciCreazioneNuovoIngrediente() {
        // 1. Crea il form
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField nomeField = new JTextField();
        JTextField descrizioneField = new JTextField();
        
        formPanel.add(new JLabel("Nome Ingrediente:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("Descrizione (opzionale):"));
        formPanel.add(descrizioneField);
        
        // 2. Mostra il popup
        int result = JOptionPane.showConfirmDialog(
            this, formPanel, "Crea Nuovo Ingrediente", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nome = nomeField.getText().trim();
                String descrizione = descrizioneField.getText().trim();
                
                if (nome.isEmpty()) {
                     JOptionPane.showMessageDialog(this, "Il nome è obbligatorio!", "Errore", JOptionPane.ERROR_MESSAGE);
                     return;
                }
                
                // 3. Chiama il service
                boolean successo = chefService.creaNuovoIngrediente(nome, descrizione);
                
                if (successo) {
                    JOptionPane.showMessageDialog(this, "Ingrediente '" + nome + "' creato con successo!");
                    // 4. Ricarica tutto
                    caricaDatiIngredienti();
                    // Seleziona automaticamente l'ingrediente appena creato
                    ingredienteComboBox.setSelectedItem(nome);
                } else {
                     JOptionPane.showMessageDialog(this, "Errore: L'ingrediente '" + nome + "' esiste già.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception ex) {
                 JOptionPane.showMessageDialog(this, "Errore imprevisto: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}