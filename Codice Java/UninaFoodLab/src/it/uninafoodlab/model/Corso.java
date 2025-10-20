package it.uninafoodlab.model;

import java.time.LocalDate;

public class Corso {

    private String codFiscChef;   // FK a Chef
    private String titolo;        // PK insieme a codFiscChef
    private String frequenza;     // es. "settimanale", "ogni due giorni"
    private LocalDate dataInizio; // data di inizio corso

    // Costruttore vuoto
    public Corso() {}

    // Costruttore completo
    public Corso(String codFiscChef, String titolo, String frequenza, LocalDate dataInizio) {
        this.codFiscChef = codFiscChef;
        this.titolo = titolo;
        this.frequenza = frequenza;
        this.dataInizio = dataInizio;
    }

    // Getter e Setter
    public String getCodFiscChef() {
        return codFiscChef;
    }

    public void setCodFiscChef(String codFiscChef) {
        this.codFiscChef = codFiscChef;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getFrequenza() {
        return frequenza;
    }

    public void setFrequenza(String frequenza) {
        this.frequenza = frequenza;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    // toString() per debug
    @Override
    public String toString() {
        return "Corso{" +
                "codFiscChef='" + codFiscChef + '\'' +
                ", titolo='" + titolo + '\'' +
                ", frequenza='" + frequenza + '\'' +
                ", dataInizio=" + dataInizio +
                '}';
    }
}