package it.uninafoodlab.model;

import java.time.LocalDateTime;

public class ProgrammaSessione {

    private String codFiscChef;
    private String titoloRicetta;
    private LocalDateTime dataCreazioneRicetta;
    private String titoloCorso;
    private String titoloSessione;
    private double porzioniPerPartecipante;

    public ProgrammaSessione() {}

    public ProgrammaSessione(String codFiscChef, String titoloRicetta, LocalDateTime dataCreazioneRicetta,
                             String titoloCorso, String titoloSessione, double porzioniPerPartecipante) {
        this.codFiscChef = codFiscChef;
        this.titoloRicetta = titoloRicetta;
        this.dataCreazioneRicetta = dataCreazioneRicetta;
        this.titoloCorso = titoloCorso;
        this.titoloSessione = titoloSessione;
        this.porzioniPerPartecipante = porzioniPerPartecipante;
    }

    // Getter e Setter
    public String getCodFiscChef() {
        return codFiscChef;
    }

    public void setCodFiscChef(String codFiscChef) {
        this.codFiscChef = codFiscChef;
    }

    public String getTitoloRicetta() {
        return titoloRicetta;
    }

    public void setTitoloRicetta(String titoloRicetta) {
        this.titoloRicetta = titoloRicetta;
    }

    public LocalDateTime getDataCreazioneRicetta() {
        return dataCreazioneRicetta;
    }

    public void setDataCreazioneRicetta(LocalDateTime dataCreazioneRicetta) {
        this.dataCreazioneRicetta = dataCreazioneRicetta;
    }

    public String getTitoloCorso() {
        return titoloCorso;
    }

    public void setTitoloCorso(String titoloCorso) {
        this.titoloCorso = titoloCorso;
    }

    public String getTitoloSessione() {
        return titoloSessione;
    }

    public void setTitoloSessione(String titoloSessione) {
        this.titoloSessione = titoloSessione;
    }

    public double getPorzioniPerPartecipante() {
        return porzioniPerPartecipante;
    }

    public void setPorzioniPerPartecipante(double porzioniPerPartecipante) {
        this.porzioniPerPartecipante = porzioniPerPartecipante;
    }

    @Override
    public String toString() {
        return "ProgrammaSessione{" +
                "codFiscChef='" + codFiscChef + '\'' +
                ", titoloRicetta='" + titoloRicetta + '\'' +
                ", dataCreazioneRicetta=" + dataCreazioneRicetta +
                ", titoloCorso='" + titoloCorso + '\'' +
                ", titoloSessione='" + titoloSessione + '\'' +
                ", porzioniPerPartecipante=" + porzioniPerPartecipante +
                '}';
    }
}