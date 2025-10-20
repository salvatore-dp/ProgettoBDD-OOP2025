package it.uninafoodlab.service;

import it.uninafoodlab.dao.*; // Importa tutti i DAO
import it.uninafoodlab.model.*; // Importa tutti i Model

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap; // <-- IMPORT
import java.util.List;
import java.util.Map;    // <-- IMPORT

/**
 * Classe Service per gestire la logica di business
 * relativa alle operazioni dello Chef.
 */
public class ChefService {

    private Chef chefLoggato;

    // DAO
    private CorsoDAO corsoDAO;
    private SessioneInPresenzaDAO sessioneInPresenzaDAO;
    private SessioneOnlineDAO sessioneOnlineDAO;
    private RicettaDAO ricettaDAO;
    private IngredienteDAO ingredienteDAO;
    private ComposizioneRicettaDAO composizioneRicettaDAO;
    private ProgrammaSessioneDAO programmaSessioneDAO;
    private AdesioneDAO adesioneDAO; // <-- DAO AGGIUNTO


    public ChefService(Chef chefLoggato) {
        this.chefLoggato = chefLoggato;

        // Inizializza DAO
        this.corsoDAO = new CorsoDAO();
        this.sessioneInPresenzaDAO = new SessioneInPresenzaDAO();
        this.sessioneOnlineDAO = new SessioneOnlineDAO();
        this.ricettaDAO = new RicettaDAO();
        this.ingredienteDAO = new IngredienteDAO();
        this.composizioneRicettaDAO = new ComposizioneRicettaDAO();
        this.programmaSessioneDAO = new ProgrammaSessioneDAO();
        this.adesioneDAO = new AdesioneDAO(); // <-- DAO AGGIUNTO
    }

    // --- Metodi CORSO ---
    // ... (invariati) ...
    public List<Corso> getMieiCorsi() { /*...*/ return corsoDAO.getCorsiByChef(chefLoggato.getCodiceFiscale()); }
    public boolean creaNuovoCorso(String t, String f, LocalDate d) { /*...*/ return corsoDAO.insertCorso(new Corso(chefLoggato.getCodiceFiscale(), t, f, d)); }
    public boolean aggiornaCorso(Corso c) { /*...*/ return corsoDAO.updateCorso(c); }
    public boolean eliminaCorso(Corso c) { /*...*/ return corsoDAO.deleteCorso(c.getCodFiscChef(), c.getTitolo()); }

    // --- Metodi RICETTA ---
    // ... (invariati) ...
    public List<Ricetta> getMieRicette() { /*...*/ return ricettaDAO.getRicetteByChef(chefLoggato.getCodiceFiscale()); }
    public boolean creaNuovaRicetta(String t, String d, String diff, int tp, int p) { /*...*/ return ricettaDAO.insertRicetta(new Ricetta(chefLoggato.getCodiceFiscale(), t, d, diff, tp, p, LocalDateTime.now().withNano(0))); }
    public boolean aggiornaRicetta(Ricetta r) { /*...*/ return ricettaDAO.updateRicetta(r); }
    public boolean eliminaRicetta(Ricetta r) { /*...*/ return ricettaDAO.deleteRicetta(r.getCodFiscChef(), r.getTitoloRicetta(), r.getDataCreazione()); }

    // --- Metodi COMPOSIZIONE RICETTA & INGREDIENTI ---
    // ... (invariati) ...
    public List<Ingrediente> getAllIngredientiDisponibili() { /*...*/ return ingredienteDAO.getAllIngredienti(); }
    public List<ComposizioneRicetta> getIngredientiDellaRicetta(Ricetta r) { /*...*/ return composizioneRicettaDAO.getComposizioniByRicetta(r.getCodFiscChef(), r.getTitoloRicetta(), r.getDataCreazione()); }
    public boolean aggiungiIngredienteARicetta(Ricetta r, Ingrediente i, double q, String u) { /*...*/ return composizioneRicettaDAO.aggiungiComposizioneRicetta(new ComposizioneRicetta(r.getCodFiscChef(), r.getTitoloRicetta(), r.getDataCreazione(), i.getNomeIngrediente(), q, u)); }
    public boolean aggiornaIngredienteInRicetta(ComposizioneRicetta c) { /*...*/ return composizioneRicettaDAO.updateComposizioneRicetta(c); }
    public boolean rimuoviIngredienteDaRicetta(ComposizioneRicetta c) { /*...*/ return composizioneRicettaDAO.eliminaComposizioneRicetta(c.getCodFiscChef(), c.getTitoloRicetta(), c.getDataCreazioneRicetta(), c.getNomeIngrediente()); }
    public boolean creaNuovoIngrediente(String n, String d) { /*...*/ return ingredienteDAO.insertIngrediente(new Ingrediente(n, d)); }

    // --- Metodi PROGRAMMA SESSIONE ---
    // ... (invariati) ...
     public List<ProgrammaSessione> getProgrammaDellaSessione(SessioneInPresenza s) { /*...*/ return programmaSessioneDAO.getProgrammaBySessione(s.getCodFiscChef(), s.getTitoloCorso(), s.getTitoloSessione()); }
     public boolean aggiungiRicettaAlProgramma(SessioneInPresenza s, Ricetta r, double p) { /*...*/ return programmaSessioneDAO.aggiungiProgrammaSessione(new ProgrammaSessione(r.getCodFiscChef(), r.getTitoloRicetta(), r.getDataCreazione(), s.getTitoloCorso(), s.getTitoloSessione(), p)); }
     public boolean rimuoviRicettaDalProgramma(ProgrammaSessione ps) { /*...*/ return programmaSessioneDAO.eliminaProgrammaSessione(ps.getCodFiscChef(), ps.getTitoloRicetta(), ps.getDataCreazioneRicetta(), ps.getTitoloCorso(), ps.getTitoloSessione()); }

    // --- NUOVO METODO: CALCOLO REPORT ---

    /**
     * Calcola la lista aggregata degli ingredienti necessari per una sessione
     * in presenza, basandosi sui partecipanti confermati.
     * @param sessione La sessione in presenza selezionata
     * @return Una Map<String, String> con [NomeIngrediente] -> "Quantità Unità",
     * oppure una mappa vuota se non ci sono partecipanti o programma.
     */
    public Map<String, String> calcolaIngredientiPerSessione(SessioneInPresenza sessione) {
        Map<String, String> listaSpesa = new HashMap<>();
        if (sessione == null) return listaSpesa;

        // 1. Conta i partecipanti confermati per QUESTA sessione
        //    (Dobbiamo aggiungere un metodo al DAO AdesioneDAO)
        //    Per ora, ipotizziamo di avere un metodo che ritorna il numero:
        int numPartecipantiConfermati = adesioneDAO.countPartecipantiConfermati(
            sessione.getCodFiscChef(),
            sessione.getTitoloCorso(),
            sessione.getTitoloSessione()
        );

        System.out.println("DEBUG Report: Trovati " + numPartecipantiConfermati + " partecipanti confermati.");

        if (numPartecipantiConfermati == 0) {
            return listaSpesa; // Nessuno partecipa, non serve nulla
        }

        // 2. Ottieni il programma della sessione (quali ricette fare)
        List<ProgrammaSessione> programma = getProgrammaDellaSessione(sessione);
        if (programma.isEmpty()) {
            System.out.println("DEBUG Report: Nessuna ricetta in programma per questa sessione.");
            return listaSpesa; // Nessuna ricetta, non serve nulla
        }

        // Mappa temporanea per aggregare le quantità (NomeIngrediente -> [QuantitàTotale, Unità])
        Map<String, Object[]> aggregato = new HashMap<>();

        // 3. Itera sul programma
        for (ProgrammaSessione ps : programma) {
            double porzioniPerPartecipante = ps.getPorzioniPerPartecipante();
            System.out.println("DEBUG Report: Elaboro ricetta '" + ps.getTitoloRicetta() + "' ("+porzioniPerPartecipante+" porz/pax)");


            // 4. Ottieni gli ingredienti per questa ricetta
            List<ComposizioneRicetta> ingredientiRicetta = composizioneRicettaDAO.getComposizioniByRicetta(
                ps.getCodFiscChef(),
                ps.getTitoloRicetta(),
                ps.getDataCreazioneRicetta()
            );

            // 5. Itera sugli ingredienti della ricetta
            for (ComposizioneRicetta comp : ingredientiRicetta) {
                String nomeIngrediente = comp.getNomeIngrediente();
                double quantitaPerRicetta = comp.getQuantitaIngrediente();
                String unita = comp.getUnitaSpecIngrediente();

                // 6. Calcola il fabbisogno totale per questo ingrediente in questa ricetta
                double fabbisogno = numPartecipantiConfermati * porzioniPerPartecipante * quantitaPerRicetta;

                 System.out.println("DEBUG Report:   - Ingrediente: " + nomeIngrediente + ", QtaRicetta: " + quantitaPerRicetta + " " + unita + ", Fabbisogno: " + fabbisogno);

                // 7. Aggrega nella mappa temporanea
                if (aggregato.containsKey(nomeIngrediente)) {
                    Object[] datiEsistenti = aggregato.get(nomeIngrediente);
                    double quantitaPrecedente = (Double) datiEsistenti[0];
                    String unitaEsistente = (String) datiEsistenti[1];

                    // Semplice controllo: se le unità sono diverse, non possiamo sommare
                    // In un'app reale, servirebbe conversione (es. ml -> l)
                    if (unitaEsistente != null && unitaEsistente.equalsIgnoreCase(unita)) {
                        aggregato.put(nomeIngrediente, new Object[]{quantitaPrecedente + fabbisogno, unita});
                    } else {
                        // Unità diverse: mettiamo un messaggio di errore o gestiamo separatamente
                        // Per ora, sovrascriviamo con un avviso (non ideale)
                        System.err.println("ATTENZIONE: Unità di misura diverse per " + nomeIngrediente + ". Impossibile aggregare correttamente.");
                        // Potremmo aggiungere una nuova entry con un nome tipo "nomeIngrediente (altra unità)"
                        // Ma per semplicità ora lo ignoriamo e teniamo solo l'ultima unità trovata
                         aggregato.put(nomeIngrediente, new Object[]{fabbisogno, unita + " (ATTENZIONE: UNITA' DIVERSE TROVATE!)"});
                    }
                } else {
                    aggregato.put(nomeIngrediente, new Object[]{fabbisogno, unita});
                }
            }
        }

        // 8. Formatta la mappa finale per la visualizzazione
        for (Map.Entry<String, Object[]> entry : aggregato.entrySet()) {
            String nomeIng = entry.getKey();
            double qtaTotale = (Double) entry.getValue()[0];
            String unitaFinale = (String) entry.getValue()[1];
            // Format to 1 decimal place, handle potential null unit
            listaSpesa.put(nomeIng, String.format("%.1f %s", qtaTotale, (unitaFinale != null ? unitaFinale : "")));
        }

        System.out.println("DEBUG Report: Calcolo completato. Lista spesa generata.");
        return listaSpesa;
    }


    // --- Metodi SESSIONE (Tutti) ---
    // ... (invariati) ...
    public List<SessioneInPresenza> getSessioniInPresenzaDelCorso(Corso c) { /*...*/ return sessioneInPresenzaDAO.getSessioniByCorso(c.getCodFiscChef(), c.getTitolo()); }
    public List<SessioneOnline> getSessioniOnlineDelCorso(Corso c) { /*...*/ return sessioneOnlineDAO.getSessioniByCorso(c.getCodFiscChef(), c.getTitolo()); }
    public boolean creaNuovaSessioneInPresenza(Corso c, String ts, LocalDateTime d, int dur, String l, int p) { /*...*/ return sessioneInPresenzaDAO.insertSessione(new SessioneInPresenza(c.getCodFiscChef(), c.getTitolo(), ts, d, dur, l, p, p)); }
    public boolean creaNuovaSessioneOnline(Corso c, String ts, LocalDateTime d, int dur, String link) { /*...*/ return sessioneOnlineDAO.insertSessione(new SessioneOnline(c.getCodFiscChef(), c.getTitolo(), ts, d, dur, link)); }
    public boolean aggiornaSessioneInPresenza(SessioneInPresenza s) { /*...*/ return sessioneInPresenzaDAO.updateSessione(s); }
    public boolean aggiornaSessioneOnline(SessioneOnline s) { /*...*/ return sessioneOnlineDAO.updateSessione(s); }
    public boolean eliminaSessioneInPresenza(SessioneInPresenza s) { /*...*/ return sessioneInPresenzaDAO.deleteSessione(s.getCodFiscChef(), s.getTitoloCorso(), s.getTitoloSessione()); }
    public boolean eliminaSessioneOnline(SessioneOnline s) { /*...*/ return sessioneOnlineDAO.deleteSessione(s.getCodFiscChef(), s.getTitoloCorso(), s.getTitoloSessione()); }

}