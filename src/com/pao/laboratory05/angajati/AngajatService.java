package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati = new Angajat[0];

    // Singleton Pattern (Constructor privat + Holder)
    private AngajatService() {}

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat a) {
        // Resize array
        Angajat[] tmp = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, tmp, 0, angajati.length);
        tmp[tmp.length - 1] = a;
        angajati = tmp;

        System.out.println("Succes! Angajatul '" + a.getNume() + "' a fost adăugat.");
    }

    public void printAll() {
        if (angajati.length == 0) {
            System.out.println("Nu există angajați în sistem.");
            return;
        }
        for (Angajat a : angajati) {
            System.out.println(a);
        }
    }

    public void listBySalary() {
        if (angajati.length == 0) {
            System.out.println("Nu există angajați pentru sortare.");
            return;
        }
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy); // natural = descrescător după salariu
        for (Angajat a : copy) {
            System.out.println(a);
        }
    }

    public void findByDepartament(String numeDept) {
        boolean gasit = false;
        for (Angajat a : angajati) {
            // Aici vedem utilitatea record-ului: a.getDepartament().nume() accesează direct numele!
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(a);
                gasit = true;
            }
        }

        if (!gasit) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }
}