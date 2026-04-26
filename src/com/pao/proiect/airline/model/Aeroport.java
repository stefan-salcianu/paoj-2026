package com.pao.proiect.airline.model;

import java.util.Objects;

public class Aeroport {

    private String cod;
    private String nume;
    private String oras;
    private String tara;

    public Aeroport(String cod, String nume, String oras, String tara) {
        this.cod = cod;
        this.nume = nume;
        this.oras = oras;
        this.tara = tara;
    }

    public String getCod() { return cod; }
    public void setCod(String cod) { this.cod = cod; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getOras() { return oras; }
    public void setOras(String oras) { this.oras = oras; }

    public String getTara() { return tara; }
    public void setTara(String tara) { this.tara = tara; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aeroport)) return false;
        Aeroport aeroport = (Aeroport) o;
        return Objects.equals(cod, aeroport.cod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cod);
    }

    @Override
    public String toString() {
        return "Aeroport{cod='" + cod + "', nume='" + nume +
               "', oras='" + oras + "', tara='" + tara + "'}";
    }
}
