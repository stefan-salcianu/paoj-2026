package com.pao.proiect.airline.model;

import java.util.Objects;

public class Pilot extends Angajat {

    private String licenta;
    private int oreDeZbor;

    public Pilot(String id, String nume, String prenume, String email,
                 String angajatId, double salariu, String licenta, int oreDeZbor) {
        super(id, nume, prenume, email, angajatId, salariu, "Operatiuni Zboruri");
        this.licenta = licenta;
        this.oreDeZbor = oreDeZbor;
    }

    @Override
    public String getRol() {
        return "Pilot";
    }

    public String getLicenta() { return licenta; }
    public void setLicenta(String licenta) { this.licenta = licenta; }

    public int getOreDeZbor() { return oreDeZbor; }
    public void setOreDeZbor(int oreDeZbor) { this.oreDeZbor = oreDeZbor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pilot)) return false;
        if (!super.equals(o)) return false;
        Pilot pilot = (Pilot) o;
        return Objects.equals(licenta, pilot.licenta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), licenta);
    }

    @Override
    public String toString() {
        return "Pilot{id='" + getId() + "', numeComplet='" + getNumeComplet() +
               "', licenta='" + licenta + "', oreDeZbor=" + oreDeZbor + "}";
    }
}
