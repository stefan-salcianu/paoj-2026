package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = readStudenti();

        Scanner scanner = new Scanner(System.in);
        int prag = Integer.parseInt(scanner.nextLine().trim());

        List<Student> filtrati = new ArrayList<>();
        for (Student s : studenti) {
            if (s.getVarsta() >= prag) {
                filtrati.add(s);
            }
        }

        try (BufferedWriter fout = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("rezultate.txt"), StandardCharsets.UTF_8))) {
            for (Student s : filtrati) {
                fout.write(s.toString());
                fout.newLine();
            }
        }

        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + filtrati.size() + " studenti");
        System.out.println();
        for (Student s : filtrati) {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("Scris in: rezultate.txt");
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
}
