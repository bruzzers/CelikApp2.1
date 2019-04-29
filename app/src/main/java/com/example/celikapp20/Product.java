package com.example.celikapp20;

public class Product {
    private String nome;
    private String marca;
    private String prezzo;
    private String localita;
    private String codice;
    private String latitudine;
    private String longitudine;

    public Product(String an, String am, String ap, String al, String ac, String alat, String along){
        nome=an;
        marca=am;
        prezzo=ap;
        localita=al;
        codice=ac;
        latitudine=alat;
        longitudine=along;
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

    public String getCodice(){
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public void setLatitudine(String latitudine) {
        this.latitudine = latitudine;
    }

    public String getLatitudine() {
        return latitudine;
    }

    public void setLongitudine(String longitudine) {
        this.longitudine = longitudine;
    }

    public String getLongitudine() {
        return longitudine;
    }
}
