package it.uninafoodlab.service;

import it.uninafoodlab.dao.*; // Importa tutti i DAO
import it.uninafoodlab.model.*; // Importa tutti i Model

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class UserService {

    private Utente utenteLoggato;
    private CorsoDAO corsoDAO;
    private IscrizioneDAO iscrizioneDAO;
    private SessioneOnlineDAO sessioneOnlineDAO;
    private SessioneInPresenzaDAO sessioneInPresenzaDAO;
    private AdesioneDAO adesioneDAO;

    public UserService(Utente utenteLoggato) {
        if (utenteLoggato == null) {
            throw new IllegalArgumentException("L'utente loggato non può essere nullo");
        }
        this.utenteLoggato = utenteLoggato;
        this.corsoDAO = new CorsoDAO();
        this.iscrizioneDAO = new IscrizioneDAO();
        this.sessioneOnlineDAO = new SessioneOnlineDAO();
        this.sessioneInPresenzaDAO = new SessioneInPresenzaDAO();
        this.adesioneDAO = new AdesioneDAO();
    }

    // --- Metodi CORSO ---
    public List<Corso> getTuttiICorsi() { return corsoDAO.getAllCorsi(); }
    public List<Iscrizione> getLeMieIscrizioni() { return iscrizioneDAO.getIscrizioniByUtente(utenteLoggato.getUsername()); }

    /**
     * @param corso Il corso a cui iscriversi
     * @return true se l'iscrizione e la creazione delle adesioni preliminari
     * hanno successo, false altrimenti.
     */
    public boolean iscrivitiACorso(Corso corso) {
        if (corso == null) return false;
        Iscrizione nuovaIscrizione = new Iscrizione(utenteLoggato.getUsername(), corso.getCodFiscChef(), corso.getTitolo());
        boolean iscrizioneOk = iscrizioneDAO.insertIscrizione(nuovaIscrizione);
        if (!iscrizioneOk) { System.err.println("Iscrizione fallita per " + utenteLoggato.getUsername() + " al corso " + corso.getTitolo()); return false; }

        List<SessioneInPresenza> sessioniPresenza = getSessioniInPresenzaDelCorso(corso);
        boolean adesioniOk = true;
        System.out.println("Trovate " + sessioniPresenza.size() + " sessioni in presenza per il corso " + corso.getTitolo());
        for (SessioneInPresenza sp : sessioniPresenza) {
            Adesione esistente = adesioneDAO.getAdesione(utenteLoggato.getUsername(), sp.getCodFiscChef(), sp.getTitoloCorso(), sp.getTitoloSessione());
            if (esistente == null) {
                 Adesione nuovaAdesione = new Adesione(utenteLoggato.getUsername(), sp.getCodFiscChef(), sp.getTitoloCorso(), sp.getTitoloSessione(), Timestamp.from(Instant.now()), false );
                boolean inserita = adesioneDAO.insertAdesione(nuovaAdesione);
                if (!inserita) { System.err.println("Errore critico: impossibile creare adesione preliminare per sessione " + sp.getTitoloSessione()); adesioniOk = false; }
                else { System.out.println("Adesione preliminare creata per sessione " + sp.getTitoloSessione()); }
            } else { System.out.println("Adesione già esistente per sessione " + sp.getTitoloSessione() + ", skipped."); }
        }
        return iscrizioneOk && adesioniOk;
    }

    /**
     * @param corso Il corso da cui disiscriversi
     * @return true se la disiscrizione ha successo, false altrimenti.
     */
    public boolean disiscrivitiDaCorso(Corso corso) {
        if (corso == null) {
            return false;
        }
        return iscrizioneDAO.deleteIscrizione(
            utenteLoggato.getUsername(),
            corso.getCodFiscChef(),
            corso.getTitolo()
        );
    }

    public List<Corso> getMieiCorsiIscritto() {List<Iscrizione> mieIscrizioni = getLeMieIscrizioni(); List<Corso> corsiIscritti = new ArrayList<>(); for (Iscrizione isc : mieIscrizioni) { Corso corso = corsoDAO.getCorso(isc.getCodFiscChef(), isc.getTitoloCorso()); if (corso != null) corsiIscritti.add(corso); else System.err.println("Attenzione: riferimento a corso inesistente: " + isc.getTitoloCorso()); } return corsiIscritti; }
    public List<Corso> getCorsiDisponibili() {List<Corso> tuttiCorsi = getTuttiICorsi(); List<Iscrizione> mieIscrizioni = getLeMieIscrizioni(); Map<String, Boolean> mappaIscrizioni = mieIscrizioni.stream().collect(Collectors.toMap(isc -> isc.getCodFiscChef() + ";" + isc.getTitoloCorso(), isc -> true )); return tuttiCorsi.stream().filter(corso -> !mappaIscrizioni.containsKey(corso.getCodFiscChef() + ";" + corso.getTitolo())).collect(Collectors.toList()); }

    public List<SessioneOnline> getSessioniOnlineDelCorso(Corso corso) { if (corso == null) return new ArrayList<>(); return sessioneOnlineDAO.getSessioniByCorso(corso.getCodFiscChef(), corso.getTitolo()); }
    public List<SessioneInPresenza> getSessioniInPresenzaDelCorso(Corso corso) { if (corso == null) return new ArrayList<>(); return sessioneInPresenzaDAO.getSessioniByCorso(corso.getCodFiscChef(), corso.getTitolo()); }
    public Adesione getMiaAdesionePerSessione(SessioneInPresenza sessione) { if (sessione == null) return null; return adesioneDAO.getAdesione(utenteLoggato.getUsername(), sessione.getCodFiscChef(), sessione.getTitoloCorso(), sessione.getTitoloSessione()); }
    public boolean aggiornaConfermaAdesione(SessioneInPresenza sessione, boolean confermato) { if (sessione == null) return false; return adesioneDAO.updateConferma(utenteLoggato.getUsername(), sessione.getCodFiscChef(), sessione.getTitoloCorso(), sessione.getTitoloSessione(), confermato); }

}