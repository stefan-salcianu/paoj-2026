package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        new File("output").mkdirs();

        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine().trim());

        List<Tranzactie> tranzactii = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String[] parts = scanner.nextLine().trim().split(" ");
            int id = Integer.parseInt(parts[0]);
            double suma = Double.parseDouble(parts[1]);
            String data = parts[2];
            String contSursa = parts[3];
            String contDestinatie = parts[4];
            TipTranzactie tip = TipTranzactie.valueOf(parts[5]);
            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            t.note = "procesat";
            tranzactii.add(t);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            oos.writeObject(tranzactii);
        }

        List<Tranzactie> loaded;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            @SuppressWarnings("unchecked")
            List<Tranzactie> tmp = (List<Tranzactie>) ois.readObject();
            loaded = tmp;
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            if (line.equals("LIST")) {
                for (Tranzactie t : loaded) {
                    System.out.println(t);
                }
            } else if (line.startsWith("FILTER ")) {
                String prefix = line.substring(7).trim();
                List<Tranzactie> filtered = new ArrayList<>();
                for (Tranzactie t : loaded) {
                    if (t.getData().startsWith(prefix)) filtered.add(t);
                }
                if (filtered.isEmpty()) {
                    System.out.println("Niciun rezultat.");
                } else {
                    for (Tranzactie t : filtered) System.out.println(t);
                }
            } else if (line.startsWith("NOTE ")) {
                int id = Integer.parseInt(line.substring(5).trim());
                Tranzactie found = null;
                for (Tranzactie t : loaded) {
                    if (t.getId() == id) { found = t; break; }
                }
                if (found == null) {
                    System.out.println("NOTE[" + id + "]: not found");
                } else {
                    System.out.println("NOTE[" + id + "]: " + found.note);
                }
            }
        }
    }
}
