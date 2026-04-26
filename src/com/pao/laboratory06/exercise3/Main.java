package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- DEMO CONSTANTE ---");
        System.out.println("TVA Actual: " + ConstanteFinanciare.TVA.getValoare() * 100 + "%");

        System.out.println("\n--- DEMO SORTARE INGINERI ---");
        Inginer i1 = new Inginer("Popescu", "Ion", "0722", 8000);
        Inginer i2 = new Inginer("Ababei", "Dan", "0733", 12000);
        Inginer i3 = new Inginer("Zaharia", "Alin", "0744", 5000);

        Inginer[] ingineri = {i1, i2, i3};

        Arrays.sort(ingineri); // Natural order (Nume)
        System.out.println("Sortare Alfabetică: " + Arrays.toString(ingineri));

        Arrays.sort(ingineri, new ComparatorInginerSalariu()); // Salariu descrescator
        System.out.println("Sortare Salariu Desc: " + Arrays.toString(ingineri));

        System.out.println("\n--- DEMO POLIMORFISM ---");
        PlataOnline plataInginer = i1;
        System.out.println("Sold consultat prin interfață: " + plataInginer.consultareSold());

        System.out.println("\n--- DEMO SMS & EDGE CASES ---");
        PersoanaJuridica firma = new PersoanaJuridica("TechCorp SRL", "021-999");
        PlataOnlineSMS plataFirma = firma;

        System.out.println("Trimitere SMS valid: " + plataFirma.trimiteSMS("Plata factură acceptată."));

        PersoanaJuridica firmaFaraTel = new PersoanaJuridica("NoPhone SRL", null);
        System.out.println("Trimitere SMS fără telefon: " + firmaFaraTel.trimiteSMS("Test")); // returnează false

        System.out.println("\n--- DEMO TRATARE ERORI ---");

        try {
            i1.autentificare(null, "parola123");
        } catch (IllegalArgumentException e) {
            System.out.println("Capturat eroare autentificare: " + e.getMessage());
        }

        try {
            PlataOnline p = i1;
            if (!(p instanceof PlataOnlineSMS)) {
                System.out.println("Sistemul detectează: Acest utilizator NU suportă SMS.");
                // Dacă am fi avut o metodă generică de procesare care făcea cast forțat,
                // aici ar fi aruncat excepția cerută.
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}