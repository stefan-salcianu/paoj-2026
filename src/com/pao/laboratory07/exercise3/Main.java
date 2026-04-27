package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] tokens = sc.nextLine().trim().split(" ");
            switch (tokens[0]) {
                case "STANDARD" -> comenzi.add(new ComandaStandard(
                        tokens[1], Double.parseDouble(tokens[2]), tokens[3]));
                case "DISCOUNTED" -> comenzi.add(new ComandaRedusa(
                        tokens[1], Double.parseDouble(tokens[2]), Integer.parseInt(tokens[3]), tokens[4]));
                case "GIFT" -> comenzi.add(new ComandaGratuita(tokens[1], tokens[2]));
            }
        }

        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }
        System.out.println();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("QUIT")) break;

            if (line.equals("STATS")) {
                System.out.println("--- STATS ---");
                Map<String, Double> avg = comenzi.stream().collect(Collectors.groupingBy(
                        c -> c instanceof ComandaStandard ? "STANDARD"
                                : c instanceof ComandaRedusa ? "DISCOUNTED" : "GIFT",
                        Collectors.averagingDouble(Comanda::pretFinal)));
                for (String tip : List.of("STANDARD", "DISCOUNTED", "GIFT")) {
                    if (avg.containsKey(tip)) {
                        System.out.printf("%s: medie = %.2f lei%n", tip, avg.get(tip));
                    }
                }
                System.out.println();

            } else if (line.startsWith("FILTER")) {
                double threshold = Double.parseDouble(line.split(" ")[1]);
                System.out.printf("--- FILTER (>= %.2f) ---%n", threshold);
                comenzi.stream()
                        .filter(c -> c.pretFinal() >= threshold)
                        .forEach(c -> System.out.println(c.descriereShort()));
                System.out.println();

            } else if (line.equals("SORT")) {
                System.out.println("--- SORT (by client, then by pret) ---");
                comenzi.stream()
                        .sorted(Comparator.comparing(Comanda::getClient)
                                .thenComparingDouble(Comanda::pretFinal))
                        .forEach(c -> System.out.println(c.descriereShort()));
                System.out.println();

            } else if (line.equals("SPECIAL")) {
                System.out.println("--- SPECIAL (discount > 15%) ---");
                comenzi.stream()
                        .filter(c -> c instanceof ComandaRedusa cr && cr.getDiscountProcent() > 15)
                        .forEach(c -> System.out.println(((ComandaRedusa) c).descriereDiscount()));
                System.out.println();
            }
        }
    }
}
