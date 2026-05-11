package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise1.Tranzactie;

import java.util.*;
import java.util.stream.*;

public class Main {

    static class TranzactieExt extends Tranzactie {
        private final String contSursa;

        TranzactieExt(int id, double suma, String data, TipTranzactie tip, String contSursa) {
            super(id, suma, data, tip);
            this.contSursa = contSursa;
        }

        String getContSursa() { return contSursa; }
    }

    public static void main(String[] args) {
        List<TranzactieExt> tranzactii = List.of(
            new TranzactieExt(1,  1500.00, "2024-01-10", TipTranzactie.CREDIT, "RO11RNCB01"),
            new TranzactieExt(2,   300.00, "2024-01-15", TipTranzactie.DEBIT,  "RO22RNCB02"),
            new TranzactieExt(3,   850.00, "2024-01-28", TipTranzactie.CREDIT, "RO11RNCB01"),
            new TranzactieExt(4,  2000.00, "2024-02-05", TipTranzactie.CREDIT, "RO33INGB03"),
            new TranzactieExt(5,   450.00, "2024-02-12", TipTranzactie.DEBIT,  "RO22RNCB02"),
            new TranzactieExt(6,   120.00, "2024-02-20", TipTranzactie.DEBIT,  "RO44BPOS04"),
            new TranzactieExt(7,  3200.00, "2024-03-03", TipTranzactie.CREDIT, "RO33INGB03"),
            new TranzactieExt(8,   990.00, "2024-03-14", TipTranzactie.DEBIT,  "RO11RNCB01"),
            new TranzactieExt(9,   250.00, "2024-03-22", TipTranzactie.CREDIT, "RO44BPOS04"),
            new TranzactieExt(10,  750.00, "2024-03-29", TipTranzactie.DEBIT,  "RO22RNCB02")
        );

        // 1. filter — toate tranzactiile CREDIT
        System.out.println("=== 1. Tranzactii CREDIT ===");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        // 2. mapToDouble + sum — total procesat
        System.out.println("=== 2. Total procesat ===");
        double total = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf("Total procesat: %.2f RON%n", total);

        // 3. groupingBy luna + summingDouble
        System.out.println("=== 3. Total per luna ===");
        Map<String, Double> perLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)
                ));
        perLuna.forEach((luna, suma) -> System.out.printf("%s: %.2f RON%n", luna, suma));

        // 4. sorted descrescator + limit 3
        System.out.println("=== 4. Top 3 tranzactii ===");
        System.out.println("Top 3 tranzactii:");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        // 5. map contSursa + distinct
        System.out.println("=== 5. Conturi sursa unice ===");
        List<String> conturiUnice = tranzactii.stream()
                .map(TranzactieExt::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturiUnice);

        // 6. mapToDouble + average
        System.out.println("=== 6. Suma medie ===");
        double medie = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf("Suma medie: %.2f RON%n", medie);

        // 7. groupingBy luna cu extras complet
        System.out.println("=== 7. Extrase de cont lunare ===");
        tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.toList()
                ))
                .forEach((luna, lista) -> {
                    double totalLuna = lista.stream().mapToDouble(Tranzactie::getSuma).sum();
                    System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                            luna, lista.size(), totalLuna);
                });
    }
}
