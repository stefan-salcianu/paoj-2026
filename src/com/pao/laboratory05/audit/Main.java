package com.pao.laboratory05.audit;

/**
 * Exercise 4 (Bonus) — Audit Log
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 4 (Bonus) — Audit"
 *
 * Extinde soluția de la Exercise 3 cu un sistem de audit bazat pe record.
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println("\n===== Gestionare Angajați (cu Audit) =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("4. Afișează audit log");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            String optiune = scanner.nextLine().trim();

            if (optiune.equals("0")) {
                System.out.println("La revedere!");
                break;
            }

            switch (optiune) {
                case "1":
                    System.out.print("Nume angajat: ");
                    String nume = scanner.nextLine().trim();
                    System.out.print("Nume departament (ex: IT, HR): ");
                    String numeDept = scanner.nextLine().trim();
                    System.out.print("Locație departament: ");
                    String locatieDept = scanner.nextLine().trim();
                    System.out.print("Salariu: ");

                    double salariu;
                    try {
                        salariu = Double.parseDouble(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Eroare! Trebuie să introduci un număr pentru salariu.");
                        break;
                    }

                    Departament dept = new Departament(numeDept, locatieDept);
                    Angajat angajat = new Angajat(nume, dept, salariu);
                    service.addAngajat(angajat);
                    break;

                case "2":
                    System.out.println("--- Angajați sortați după salariu (descrescător) ---");
                    service.listBySalary();
                    break;

                case "3":
                    System.out.print("Introdu numele departamentului pe care îl cauți: ");
                    String searchDept = scanner.nextLine().trim();
                    System.out.println("--- Rezultate ---");
                    service.findByDepartament(searchDept);
                    break;

                case "4":
                    System.out.println("--- Audit Log ---");
                    service.printAuditLog();
                    break;

                default:
                    System.out.println("Opțiune invalidă! Te rog să alegi o cifră din meniu.");
            }
        }

        scanner.close();
    }
}