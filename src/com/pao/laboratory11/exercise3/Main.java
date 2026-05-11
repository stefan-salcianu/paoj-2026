package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<Transaction> data = List.of(
                new Transaction(1, new BigDecimal("1200.00"), LocalDate.of(2026, 5, 1),  "RO", "WEB"),
                new Transaction(2, new BigDecimal("90.00"),   LocalDate.of(2026, 5, 1),  "RU", "ATM"),
                new Transaction(3, new BigDecimal("6000.00"), LocalDate.of(2026, 5, 2),  "NG", "APP"),
                new Transaction(4, new BigDecimal("500.00"),  LocalDate.of(2026, 6, 1),  "RO", "WEB"),
                new Transaction(5, new BigDecimal("2500.00"), LocalDate.of(2026, 6, 3),  "RU", "CRYPTO"),
                new Transaction(6, new BigDecimal("150.00"),  LocalDate.of(2026, 6, 5),  "NG", "ATM"),
                new Transaction(7, new BigDecimal("6000.00"), LocalDate.of(2026, 7, 1),  "KP", "CRYPTO")
        );

        Snapshot snap = data.stream().collect(CustomCollectors.toSnapshot(3));

        // Interogare 1: top tranzactii dupa suma (din snapshot)
        System.out.println("=== Top 3 Transactions by Amount ===");
        snap.getTopTransactions().forEach(System.out::println);

        // Interogare 2: numar tranzactii per tara, descrescator
        System.out.println("\n=== Transaction Count by Country ===");
        snap.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

        // Interogare 3: clasament canale dupa numar tranzactii
        System.out.println("\n=== Transaction Count by Channel ===");
        snap.getCountByChannel().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

        // Interogare 4: suma totala
        System.out.println("\n=== Total Amount ===");
        System.out.printf("Total: %.2f%n", snap.getTotalAmount());
    }
}
