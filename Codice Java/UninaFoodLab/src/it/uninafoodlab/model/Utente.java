package it.uninafoodlab.model;

public class Utente {

    private String username;   // PK
    private String nome;
    private String cognome;
    private String email;      // UNIQUE
    private String passHash;

    // Costruttore vuoto
    public Utente() {}

    // Costruttore completo
    public Utente(String username, String nome, String cognome, String email, String passHash) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.passHash = passHash;
    }

    // Getter e Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassHash() {
        return passHash;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "username='" + username + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}