package it.uninafoodlab.model;

import java.time.LocalDateTime;

public class SessioneOnline {

    private String codFiscChef;
    private String titoloCorso;
    private String titoloSessione;
    private LocalDateTime dataOra;
    private int durata;       // minuti
    private String linkVideo;

    // Costruttore vuoto
    public SessioneOnline() {}

    // Costruttore completo
    public SessioneOnline(String codFiscChef, String titoloCorso, String titoloSessione,
                          LocalDateTime dataOra, int durata, String linkVideo) {
        this.codFiscChef = codFiscChef;
        this.titoloCorso = titoloCorso;
        this.titoloSessione = titoloSessione;
        this.dataOra = dataOra;
        this.durata = durata;
        this.linkVideo = linkVideo;
    }

    // Getter e Setter
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

    public String getLinkVideo() { return linkVideo; }
    public void setLinkVideo(String linkVideo) { this.linkVideo = linkVideo; }

    @Override
    public String toString() {
        return "SessioneOnline{" +
                "codFiscChef='" + codFiscChef + '\'' +
                ", titoloCorso='" + titoloCorso + '\'' +
                ", titoloSessione='" + titoloSessione + '\'' +
                ", dataOra=" + dataOra +
                ", durata=" + durata +
                ", linkVideo='" + linkVideo + '\'' +
                '}';
    }
}