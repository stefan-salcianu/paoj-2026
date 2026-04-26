package com.pao.proiect.airline.model;

import java.util.Objects;

public class Pasager extends Persoana {

    private String pasaportId;
    private String nationalitate;

    public Pasager(String id, String nume, String prenume, String email,
                   String pasaportId, String nationalitate) {
        super(id, nume, prenume, email);
        this.pasaportId = pasaportId;
        this.nationalitate = nationalitate;
    }

    @Override
    public String getRol() {
        return "Pasager";
    }

    public String getPasaportId() { return pasaportId; }
    public void setPasaportId(String pasaportId) { this.pasaportId = pasaportId; }

    public String getNationalitate() { return nationalitate; }
    public void setNationalitate(String nationalitate) { this.nationalitate = nationalitate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pasager)) return false;
        Pasager pasager = (Pasager) o;
        return Objects.equals(pasaportId, pasager.pasaportId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pasaportId);
    }

    @Override
    public String toString() {
        return "Pasager{numeComplet='" + getNumeComplet() +
               "', pasaportId='" + pasaportId +
               "', nationalitate='" + nationalitate + "'}";
    }
}
