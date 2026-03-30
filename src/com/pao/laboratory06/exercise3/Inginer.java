package com.pao.laboratory06.exercise3;

import java.util.Objects;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private double soldCont = 0;

    public Inginer(String nume, String prenume, String telefon, double salariu) {
        super(nume, prenume, telefon, salariu);
        this.soldCont = salariu; // sold inițial egal cu salariul pentru demo
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isEmpty() || parola == null || parola.isEmpty()) {
            throw new IllegalArgumentException("User sau parola invalide!");
        }
        System.out.println("Inginer " + nume + " autentificat cu succes.");
    }

    @Override
    public double consultareSold() {
        return soldCont;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) return false;
        if (soldCont >= suma) {
            soldCont -= suma;
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Inginer o) {
        return this.nume.compareTo(o.nume); // Ordine naturală alfabetică
    }

    @Override
    public String toString() {
        return String.format("Inginer{nume='%s', salariu=%.2f}", nume, salariu);
    }
}