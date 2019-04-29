package com.example.celikapp20;

public class Prodotto {
    private String Nome;
    private String Marca;
    private float Prezzo;
    private String Localita;

    public Prodotto(){}

    public Prodotto(String an, String am, int ap, String al){
        Nome=an;
        Marca=am;
        Prezzo=ap;
        Localita=al;
    }

    public void setNome(String nome) {
        this.Nome = nome;
    }

    public void setLocalita(String localita) {
        this.Localita = localita;
    }

    public void setMarca(String marca) {
        this.Marca = marca;
    }

    public void setPrezzo(int prezzo) {
        this.Prezzo = prezzo;
    }

    public String getNome() {
        return Nome;
    }

    public float getPrezzo() {
        return Prezzo;
    }

    public String getLocalita() {
        return Localita;
    }

    public String getMarca() {
        return Marca;
    }
}
