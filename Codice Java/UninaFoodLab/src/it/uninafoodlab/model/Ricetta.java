package it.uninafoodlab.model;

import java.time.LocalDateTime;

public class Ricetta {

    private String codFiscChef;     // FK a Chef
    private String titoloRicetta;    // PK insieme a codFiscChef + dataCreazione
    private String descrizione;
    private String difficolta;       // 'facile', 'medio', 'difficile'
    private int tempoPrep;           // minuti
    private int porzioni;
    private LocalDateTime dataCreazione; // PK insieme a codFiscChef + titoloRicetta

    // Costruttore vuoto
    public Ricetta() {}

    // Costruttore completo
    public Ricetta(String codFiscChef, String titoloRicetta, String descrizione,
                   String difficolta, int tempoPrep, int porzioni, LocalDateTime dataCreazione) {
        this.codFiscChef = codFiscChef;
        this.titoloRicetta = titoloRicetta;
        this.descrizione = descrizione;
        this.difficolta = difficolta;
        this.tempoPrep = tempoPrep;
        this.porzioni = porzioni;
        this.dataCreazione = dataCreazione;
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDifficolta() {
        return difficolta;
    }

    public void setDifficolta(String difficolta) {
        this.difficolta = difficolta;
    }

    public int getTempoPrep() {
        return tempoPrep;
    }

    public void setTempoPrep(int tempoPrep) {
        this.tempoPrep = tempoPrep;
    }

    public int getPorzioni() {
        return porzioni;
    }

    public void setPorzioni(int porzioni) {
        this.porzioni = porzioni;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    @Override
    public String toString() {
        return "Ricetta{" +
                "codFiscChef='" + codFiscChef + '\'' +
                ", titoloRicetta='" + titoloRicetta + '\'' +
                ", difficolta='" + difficolta + '\'' +
                ", tempoPrep=" + tempoPrep +
                ", porzioni=" + porzioni +
                ", dataCreazione=" + dataCreazione +
                '}';
    }
}