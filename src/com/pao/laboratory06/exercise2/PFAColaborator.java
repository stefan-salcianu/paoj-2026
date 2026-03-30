package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends Colaborator implements PersoanaFizica {
    private double cheltuieliLunare;

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double salariuMinimBrut = 3300.0;
        double venitBazaAnual = (venitBrutLunar - cheltuieliLunare) * 12;

        // 1. CASS (Sănătate) - 10%
        // Testul tău: Minim 6 salarii, Maxim 60 salarii.
        double pragCASS6 = 6 * salariuMinimBrut;
        double pragCASS60 = 60 * salariuMinimBrut;
        double bazaCASS = venitBazaAnual;

        if (bazaCASS < pragCASS6) {
            bazaCASS = pragCASS6;
        } else if (bazaCASS > pragCASS60) {
            bazaCASS = pragCASS60;
        }
        double cass = 0.10 * bazaCASS;

        // 2. CAS (Pensie) - 25%
        // Testul tău: Minim 12 salarii, Maxim 24 salarii.
        double pragCAS12 = 12 * salariuMinimBrut;
        double pragCAS24 = 24 * salariuMinimBrut;
        double bazaCAS;

        if (venitBazaAnual < pragCAS12) {
            bazaCAS = pragCAS12; // Obligatoriu 12 salarii chiar dacă venitul e mic
        } else if (venitBazaAnual < pragCAS24) {
            bazaCAS = pragCAS12;
        } else {
            bazaCAS = pragCAS24;
        }
        double cas = 0.25 * bazaCAS;

        // 3. Impozit (10%)
        // Baza de impozitare = Venit - CAS - CASS (Deducerea contribuțiilor)
        double impozit = 0.10 * Math.max(0, venitBazaAnual);

        return venitBazaAnual - impozit - cass - cas;
    }

    @Override
    public String tipContract() {
        return TipColaborator.PFA.name();
    }
}