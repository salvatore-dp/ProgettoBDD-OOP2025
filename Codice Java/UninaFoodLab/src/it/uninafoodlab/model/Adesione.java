package it.uninafoodlab.model;

import java.sql.Timestamp;

public class Adesione {

    private String usernameUtente;
    private String codFiscChef;
    private String titoloCorso;
    private String titoloSessione;
    private Timestamp dataAdesione;
    private Boolean confermata;

    // Costruttore completo
    public Adesione(String usernameUtente, String codFiscChef, String titoloCorso,
                    String titoloSessione, Timestamp dataAdesione, Boolean confermata) {
        this.usernameUtente = usernameUtente;
        this.codFiscChef = codFiscChef;
        this.titoloCorso = titoloCorso;
        this.titoloSessione = titoloSessione;
        this.dataAdesione = dataAdesione;
        this.confermata = confermata;
    }

    // Costruttore base (senza data e conferma)
    public Adesione(String usernameUtente, String codFiscChef, String titoloCorso, String titoloSessione) {
        this(usernameUtente, codFiscChef, titoloCorso, titoloSessione, null, null);
    }

    // Getter e Setter
    public String getUsernameUtente() {
        return usernameUtente;
    }

    public void setUsernameUtente(String usernameUtente) {
        this.usernameUtente = usernameUtente;
    }

    public String getCodFiscChef() {
        return codFiscChef;
    }

    public void setCodFiscChef(String codFiscChef) {
        this.codFiscChef = codFiscChef;
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

    public Timestamp getDataAdesione() {
        return dataAdesione;
    }

    public void setDataAdesione(Timestamp dataAdesione) {
        this.dataAdesione = dataAdesione;
    }

    public Boolean getConfermata() {
        return confermata;
    }

    public void setConfermata(Boolean confermata) {
        this.confermata = confermata;
    }

    @Override
    public String toString() {
        return "Adesione{" +
                "usernameUtente='" + usernameUtente + '\'' +
                ", codFiscChef='" + codFiscChef + '\'' +
                ", titoloCorso='" + titoloCorso + '\'' +
                ", titoloSessione='" + titoloSessione + '\'' +
                ", dataAdesione=" + dataAdesione +
                ", confermata=" + confermata +
                '}';
    }
}