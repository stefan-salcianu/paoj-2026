package com.pao.laboratory08.exercise1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = readStudenti();

        Scanner scanner = new Scanner(System.in);
        String comanda = scanner.nextLine().trim();
        String[] parts = comanda.split(" ", 2);
        String tip = parts[0];

        if (tip.equals("PRINT")) {
            for (Student s : studenti) {
                System.out.println(s);
            }
        } else if (tip.equals("SHALLOW")) {
            String nume = parts[1];
            Student original = findByName(studenti, nume);
            Student clona = original.shallowClone();
            clona.getAdresa().setOras("MODIFICAT");
            System.out.println("Original: " + original);
            System.out.println("Clona: " + clona);
        } else if (tip.equals("DEEP")) {
            String nume = parts[1];
            Student original = findByName(studenti, nume);
            Student clona = original.deepClone();
            clona.getAdresa().setOras("MODIFICAT");
            System.out.println("Original: " + original);
            System.out.println("Clona: " + clona);
        }
    }

    private static List<Student> readStudenti() throws IOException {
        List<Student> studenti = new ArrayList<>();
        try (BufferedReader fin = new BufferedReader(
                new InputStreamReader(new FileInputStream(FILE_PATH), StandardCharsets.UTF_8))) {
            String linie;
            while ((linie = fin.readLine()) != null) {
                linie = linie.trim();
                if (linie.isEmpty()) continue;
                String[] campuri = linie.split(",");
                String nume = campuri[0].trim();
                int varsta = Integer.parseInt(campuri[1].trim());
                String oras = campuri[2].trim();
                String strada = campuri[3].trim();
                studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
            }
        }
        return studenti;
    }

    private static Student findByName(List<Student> studenti, String nume) {
        for (Student s : studenti) {
            if (s.getNume().equals(nume)) {
                return s;
            }
        }
        return null;
    }
}
