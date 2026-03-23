package com.pao.laboratory05.angajati;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            String optiune = scanner.nextLine().trim();

            if (optiune.equals("0")) {
                System.out.println("La revedere!");
                break; // Iese din while
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

                    // Creăm record-ul și apoi angajatul
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

                default:
                    System.out.println("Opțiune invalidă! Te rog să alegi o cifră din meniu.");
            }
        }

        scanner.close();
    }
}