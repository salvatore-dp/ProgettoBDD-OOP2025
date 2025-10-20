package it.uninafoodlab.model;

public class Ingrediente {

    private String nomeIngrediente;        // PK
    private String descrizioneIngrediente;

    // Costruttore vuoto
    public Ingrediente() {}

    // Costruttore completo
    public Ingrediente(String nomeIngrediente, String descrizioneIngrediente) {
        this.nomeIngrediente = nomeIngrediente;
        this.descrizioneIngrediente = descrizioneIngrediente;
    }

    // Getter e Setter
    public String getNomeIngrediente() {
        return nomeIngrediente;
    }

    public void setNomeIngrediente(String nomeIngrediente) {
        this.nomeIngrediente = nomeIngrediente;
    }

    public String getDescrizioneIngrediente() {
        return descrizioneIngrediente;
    }

    public void setDescrizioneIngrediente(String descrizioneIngrediente) {
        this.descrizioneIngrediente = descrizioneIngrediente;
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "nomeIngrediente='" + nomeIngrediente + '\'' +
                ", descrizioneIngrediente='" + descrizioneIngrediente + '\'' +
                '}';
    }
}