package com.pao.proiect.airline.model;

import java.util.Objects;

public abstract class Persoana {

    private String id;
    private String nume;
    private String prenume;
    private String email;

    protected Persoana(String id, String nume, String prenume, String email) {
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
    }

    public abstract String getRol();

    public String getNumeComplet() {
        return prenume + " " + nume;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getPrenume() { return prenume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persoana)) return false;
        Persoana persoana = (Persoana) o;
        return Objects.equals(id, persoana.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
               "{id='" + id + "', numeComplet='" + getNumeComplet() +
               "', email='" + email + "', rol='" + getRol() + "'}";
    }
}
