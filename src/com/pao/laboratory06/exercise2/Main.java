package com.pao.laboratory06.exercise2;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        in.useLocale(Locale.US);

        if (!in.hasNextInt()) return;
        int n = in.nextInt();

        List<Colaborator> colaboratori = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String tip = in.next();
            Colaborator c = switch (tip) {
                case "CIM" -> new CIMColaborator();
                case "PFA" -> new PFAColaborator();
                case "SRL" -> new SRLColaborator();
                default -> null;
            };

            if (c != null) {
                c.citeste(in);
                colaboratori.add(c);
            }
        }

        // Sortare descrescătoare
        colaboratori.sort((c1, c2) -> Double.compare(c2.calculeazaVenitNetAnual(), c1.calculeazaVenitNetAnual()));

        // Afișare colaboratori
        for (Colaborator c : colaboratori) {
            c.afiseaza();
        }
        System.out.println(); // Linie goală cerută de test

        // Venit maxim
        if (!colaboratori.isEmpty()) {
            System.out.print("Colaborator cu venit net maxim: ");
            colaboratori.get(0).afiseaza();
        }
        System.out.println(); // Linie goală cerută de test

        // Persoane juridice
        System.out.println("Colaboratori persoane juridice:");
        for (Colaborator c : colaboratori) {
            if (c instanceof PersoanaJuridica) {
                c.afiseaza();
            }
        }
        System.out.println(); // Linie goală cerută de test

        // Sumarizare pe tipuri (AFIȘĂM DOAR DACĂ COUNT > 0)
        System.out.println("Sume și număr colaboratori pe tip:");
        for (TipColaborator tipEnum : TipColaborator.values()) {
            String tipStr = tipEnum.name();
            double suma = 0;
            int count = 0;

            for (Colaborator c : colaboratori) {
                if (c.tipContract().equals(tipStr)) {
                    suma += c.calculeazaVenitNetAnual();
                    count++;
                }
            }

            // CRITIC: Testul nu vrea să vadă categorii cu 0
            if (count > 0) {
                System.out.printf(Locale.US, "%s: suma = %.2f lei, număr = %d\n", tipStr, suma, count);
            }
        }

        in.close();
    }
}