package com.example.celikapp20;

public class Product {
    private String nome;
    private String marca;
    private String prezzo;
    private String localita;

    public Product(String an, String am, String ap, String al){
        nome=an;
        marca=am;
        prezzo=ap;
        localita=al;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getMarca() {
        return marca;
    }

    public void setLocalita(String localita) {
        this.localita = localita;
    }

    public String getLocalita() {
        return localita;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
