package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends Colaborator implements PersoanaFizica {
    private boolean areBonus = false;

    @Override
    public void citeste(Scanner in) {
        super.citeste(in);
        // Verificăm dacă urmează opționalul DA sau NU
        if (in.hasNext("DA")) {
            areBonus = true;
            in.next(); // consumăm cuvântul
        } else if (in.hasNext("NU")) {
            areBonus = false;
            in.next(); // consumăm cuvântul
        } else {
            areBonus = false; // default
        }
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double net = venitBrutLunar * 12 * 0.55;
        if (areBonus()) {
            net += net * 0.10; // Adăugăm 10%
        }
        return net;
    }

    @Override
    public String tipContract() {
        return TipColaborator.CIM.name();
    }

    @Override
    public boolean areBonus() {
        return areBonus;
    }
}