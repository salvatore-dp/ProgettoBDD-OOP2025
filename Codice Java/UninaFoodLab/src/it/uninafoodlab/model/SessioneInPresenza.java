package it.uninafoodlab.model;

import java.time.LocalDateTime;

public class SessioneInPresenza {

    private String codFiscChef;
    private String titoloCorso;
    private String titoloSessione;
    private LocalDateTime dataOra;
    private int durata;
    private String luogo;
    private int postiTotali;
    private int postiDisponibili;

    // Costruttore vuoto
    public SessioneInPresenza() {}

    // --- COSTRUTTORE ---
    public SessioneInPresenza(String codFiscChef, String titoloCorso, String titoloSessione,
                               LocalDateTime dataOra, int durata, String luogo, 
                               int postiTotali, int postiDisponibili) { // Aggiunti
        this.codFiscChef = codFiscChef;
        this.titoloCorso = titoloCorso;
        this.titoloSessione = titoloSessione;
        this.dataOra = dataOra;
        this.durata = durata;
        this.luogo = luogo;
        // --- NUOVE RIGHE ---
        this.postiTotali = postiTotali;
        this.postiDisponibili = postiDisponibili;
    }

    // --- Tutti i Getter e Setter ---
    
    public String getCodFiscChef() { return codFiscChef; }
    public void setCodFiscChef(String codFiscChef) { this.codFiscChef = codFiscChef; }

    public String getTitoloCorso() { return titoloCorso; }
    public void setTitoloCorso(String titoloCorso) { this.titoloCorso = titoloCorso; }

    public String getTitoloSessione() { return titoloSessione; }
    public void setTitoloSessione(String titoloSessione) { this.titoloSessione = titoloSessione; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public int getDurata() { return durata; }
    public void setDurata(int durata) { this.durata = durata; }

    public String getLuogo() { return luogo; }
    public void setLuogo(String luogo) { this.luogo = luogo; }

    public int getPostiTotali() {
        return postiTotali;
    }

    public void setPostiTotali(int postiTotali) {
        this.postiTotali = postiTotali;
    }

    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    public void setPostiDisponibili(int postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }


    @Override
    public String toString() {
        return "SessioneInPresenza{" +
                "codFiscChef='" + codFiscChef + '\'' +
                ", titoloCorso='" + titoloCorso + '\'' +
                ", titoloSessione='" + titoloSessione + '\'' +
                ", dataOra=" + dataOra +
                ", durata=" + durata +
                ", luogo='" + luogo + '\'' +
                ", postiTotali=" + postiTotali +
                ", postiDisponibili=" + postiDisponibili +
                '}';
    }
}