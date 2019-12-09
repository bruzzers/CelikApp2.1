package com.example.celikapp20;

import android.util.Log;

public class Prodotto {
    private String Nome;
    private String Marca;
    private float Prezzo;
    private String Localita;
    private String Codice;
    private double Latitudine;
    private double Longitudine;

    public Prodotto(){}

    public Prodotto(String an, String am, int ap, String al, String ac, long alat, long along){
        Nome=an;
        Marca=am;
        Prezzo=ap;
        Localita=al;
        Codice=ac;
        Latitudine=alat;
        Longitudine=along;
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

    public void setCodice(String codice) {
        Codice = codice;
    }

    public String getCodice() {
        return Codice;
    }

    public void setLongitudine(double longitudine) {
        Longitudine = longitudine;
    }

    public double getLongitudine() {
        return Longitudine;
    }

    public void setLatitudine(double latitudine) {
        Latitudine = latitudine;
    }

    public double getLatitudine() {
        return Latitudine;
    }
}
