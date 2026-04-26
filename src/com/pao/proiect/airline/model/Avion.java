package com.pao.proiect.airline.model;

import java.util.Objects;

public class Avion {

    private String id;
    private String model;
    private String numarInregistrare;
    private int capacitate;

    public Avion(String id, String model, String numarInregistrare, int capacitate) {
        this.id = id;
        this.model = model;
        this.numarInregistrare = numarInregistrare;
        this.capacitate = capacitate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getNumarInregistrare() { return numarInregistrare; }
    public void setNumarInregistrare(String numarInregistrare) { this.numarInregistrare = numarInregistrare; }

    public int getCapacitate() { return capacitate; }
    public void setCapacitate(int capacitate) { this.capacitate = capacitate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Avion)) return false;
        Avion avion = (Avion) o;
        return Objects.equals(numarInregistrare, avion.numarInregistrare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numarInregistrare);
    }

    @Override
    public String toString() {
        return "Avion{id='" + id + "', model='" + model +
               "', numarInregistrare='" + numarInregistrare +
               "', capacitate=" + capacitate + "}";
    }
}
