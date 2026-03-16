package com.pao.laboratory02.exercise3.service;

import com.pao.laboratory02.exercise3.model.Angajat;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Completează cele 3 metode.
 * Folosește ArrayList — nu mai e nevoie de redimensionare manuală.
 */
public class AngajatService {
    private List<Angajat> angajati;

    public AngajatService() {
        this.angajati = new ArrayList<>();
    }

    public void addAngajat(Angajat a) {
        angajati.add(a);
        System.out.println("Angajat adăugat: " + a.getName());
    }

    /** TODO: dacă goală → mesaj; altfel parcurge cu index și afișează (i+1) + ". " + angajat */
    public void listAll() {
        if (angajati.isEmpty()) {
            System.out.println("NU avem angajati");
            return;
        }
        for (int i=0; i<angajati.size(); i++){
            System.out.println(i+1 +' '+ angajati.get(i).getName());
        }
    }

    /** TODO: parcurge lista, sumează a.salariuTotal(), returnează totalul. */
    public double totalSalarii() {
        double s=0;
        for (Angajat a: angajati){
            s+= a.salariuTotal();
        }
        return s;
    }
}
