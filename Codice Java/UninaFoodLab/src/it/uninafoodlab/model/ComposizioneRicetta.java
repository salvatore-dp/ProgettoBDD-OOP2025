package it.uninafoodlab.model;

import java.time.LocalDateTime;

public class ComposizioneRicetta {

    private String codFiscChef;
    private String titoloRicetta;
    private LocalDateTime dataCreazioneRicetta;
    private String nomeIngrediente;
    private double quantitaIngrediente;
    private String unitaSpecIngrediente;

    public ComposizioneRicetta() {}

    public ComposizioneRicetta(String codFiscChef, String titoloRicetta, LocalDateTime dataCreazioneRicetta,
                               String nomeIngrediente, double quantitaIngrediente, String unitaSpecIngrediente) {
        this.codFiscChef = codFiscChef;
        this.titoloRicetta = titoloRicetta;
        this.dataCreazioneRicetta = dataCreazioneRicetta;
        this.nomeIngrediente = nomeIngrediente;
        this.quantitaIngrediente = quantitaIngrediente;
        this.unitaSpecIngrediente = unitaSpecIngrediente;
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

    public String getNomeIngrediente() {
        return nomeIngrediente;
    }

    public void setNomeIngrediente(String nomeIngrediente) {
        this.nomeIngrediente = nomeIngrediente;
    }

    public double getQuantitaIngrediente() {
        return quantitaIngrediente;
    }

    public void setQuantitaIngrediente(double quantitaIngrediente) {
        this.quantitaIngrediente = quantitaIngrediente;
    }

    public String getUnitaSpecIngrediente() {
        return unitaSpecIngrediente;
    }

    public void setUnitaSpecIngrediente(String unitaSpecIngrediente) {
        this.unitaSpecIngrediente = unitaSpecIngrediente;
    }

    @Override
    public String toString() {
        return "ComposizioneRicetta{" +
                "codFiscChef='" + codFiscChef + '\'' +
                ", titoloRicetta='" + titoloRicetta + '\'' +
                ", dataCreazioneRicetta=" + dataCreazioneRicetta +
                ", nomeIngrediente='" + nomeIngrediente + '\'' +
                ", quantitaIngrediente=" + quantitaIngrediente +
                ", unitaSpecIngrediente='" + unitaSpecIngrediente + '\'' +
                '}';
    }
}