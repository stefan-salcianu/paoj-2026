package com.pao.proiect.airline.model;

public class Angajat extends Persoana {

    private String angajatId;
    private double salariu;
    private String departament;

    public Angajat(String id, String nume, String prenume, String email,
                   String angajatId, double salariu, String departament) {
        super(id, nume, prenume, email);
        this.angajatId = angajatId;
        this.salariu = salariu;
        this.departament = departament;
    }

    @Override
    public String getRol() {
        return "Angajat";
    }

    public String getAngajatId() { return angajatId; }
    public void setAngajatId(String angajatId) { this.angajatId = angajatId; }

    public double getSalariu() { return salariu; }
    public void setSalariu(double salariu) { this.salariu = salariu; }

    public String getDepartament() { return departament; }
    public void setDepartament(String departament) { this.departament = departament; }

    @Override
    public String toString() {
        return "Angajat{id='" + getId() + "', numeComplet='" + getNumeComplet() +
               "', departament='" + departament + "', salariu=" + salariu + "}";
    }
}
