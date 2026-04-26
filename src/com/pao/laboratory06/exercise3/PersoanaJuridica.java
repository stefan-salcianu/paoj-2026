package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private List<String> smsTrimise = new ArrayList<>();
    private double soldCompanie = 100000.0;

    public PersoanaJuridica(String numeCompanie, String telefon) {
        super(numeCompanie, "", telefon);
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || parola == null) throw new IllegalArgumentException();
        System.out.println("Compania " + nume + " s-a conectat la portalul bancar.");
    }

    @Override
    public double consultareSold() {
        return soldCompanie;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma > soldCompanie) return false;
        soldCompanie -= suma;
        return true;
    }

    @Override
    public boolean trimiteSMS(String mesaj) {
        if (mesaj == null || mesaj.isEmpty()) return false;
        if (telefon == null || telefon.isEmpty()) return false;

        smsTrimise.add(mesaj);
        return true;
    }

    public List<String> getSmsTrimise() {
        return smsTrimise;
    }
}