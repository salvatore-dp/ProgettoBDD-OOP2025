package it.uninafoodlab.model;

import java.sql.Timestamp;

public class Iscrizione {
    private String usernameUtente;
    private String codFiscChef;
    private String titoloCorso;
    private Timestamp dataIscrizione;

    public Iscrizione(String usernameUtente, String codFiscChef, String titoloCorso, Timestamp dataIscrizione) {
        this.usernameUtente = usernameUtente;
        this.codFiscChef = codFiscChef;
        this.titoloCorso = titoloCorso;
        this.dataIscrizione = dataIscrizione;
    }

    // Costruttore senza timestamp (usa quello generato dal DB)
    public Iscrizione(String usernameUtente, String codFiscChef, String titoloCorso) {
        this(usernameUtente, codFiscChef, titoloCorso, null);
    }

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

    public Timestamp getDataIscrizione() {
        return dataIscrizione;
    }

    public void setDataIscrizione(Timestamp dataIscrizione) {
        this.dataIscrizione = dataIscrizione;
    }

    @Override
    public String toString() {
        return "Iscrizione{" +
                "usernameUtente='" + usernameUtente + '\'' +
                ", codFiscChef='" + codFiscChef + '\'' +
                ", titoloCorso='" + titoloCorso + '\'' +
                ", dataIscrizione=" + dataIscrizione +
                '}';
    }
}