package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti = new Carte[0];

    private BibliotecaService() {}

    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return Holder.INSTANCE;
    }

    public void addCarte(Carte carte) {
        Carte[] tmp = new Carte[carti.length + 1];
        System.arraycopy(carti, 0, tmp, 0, carti.length);
        tmp[tmp.length - 1] = carte;
        carti = tmp;

        System.out.println("A fost adăugată cartea: " + carte.getTitlu());
    }

    public void listSortedByRating() {
        if (carti.length == 0) return;
        Carte[] copy = carti.clone();
        Arrays.sort(copy); // Folosește Comparable (rating descrescător)
        for (Carte c : copy) {
            System.out.println(c);
        }
    }

    public void listSortedBy(Comparator<Carte> comparator) {
        if (carti.length == 0) return;
        Carte[] copy = carti.clone();
        Arrays.sort(copy, comparator); // Folosește Comparatorul primit ca parametru
        for (Carte c : copy) {
            System.out.println(c);
        }
    }
}