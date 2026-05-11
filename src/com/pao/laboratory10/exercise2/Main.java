package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = Integer.parseInt(scanner.nextLine().trim());
        List<Tranzactie> lista = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] parts = scanner.nextLine().trim().split("\\s+");
            int id = Integer.parseInt(parts[0]);
            double suma = Double.parseDouble(parts[1]);
            String data = parts[2];
            TipTranzactie tip = TipTranzactie.valueOf(parts[3]);
            lista.add(new Tranzactie(id, suma, data, tip));
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String cmd = parts[0];

            switch (cmd) {
                case "UNIQUE_IDS": {
                    LinkedHashSet<Integer> ids = new LinkedHashSet<>();
                    for (Tranzactie t : lista) ids.add(t.getId());
                    System.out.println("IDs unice (" + ids.size() + "): " + ids);
                    break;
                }
                case "MONTHLY_REPORT": {
                    TreeMap<String, double[]> report = new TreeMap<>();
                    for (Tranzactie t : lista) {
                        String luna = t.getData().substring(0, 7);
                        report.putIfAbsent(luna, new double[]{0.0, 0.0});
                        if (t.getTip() == TipTranzactie.CREDIT) {
                            report.get(luna)[0] += t.getSuma();
                        } else {
                            report.get(luna)[1] += t.getSuma();
                        }
                    }
                    for (Map.Entry<String, double[]> entry : report.entrySet()) {
                        System.out.printf("%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                                entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
                    }
                    break;
                }
                case "TOP": {
                    int topN = Integer.parseInt(parts[1]);
                    List<Tranzactie> copie = new ArrayList<>(lista);
                    Collections.sort(copie, Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    System.out.println("Top " + topN + ":");
                    for (Tranzactie t : copie.subList(0, Math.min(topN, copie.size()))) {
                        System.out.println(t);
                    }
                    break;
                }
                case "SORT_ASC": {
                    Collections.sort(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                    for (Tranzactie t : lista) System.out.println(t);
                    break;
                }
                case "SORT_DESC": {
                    Collections.sort(lista, Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    for (Tranzactie t : lista) System.out.println(t);
                    break;
                }
                case "REVERSE": {
                    Collections.reverse(lista);
                    for (Tranzactie t : lista) System.out.println(t);
                    break;
                }
                case "MIN_MAX": {
                    Tranzactie min = Collections.min(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                    Tranzactie max = Collections.max(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                    System.out.println("MIN: " + min);
                    System.out.println("MAX: " + max);
                    break;
                }
                case "CME_DEMO": {
                    try {
                        for (Tranzactie t : lista) lista.remove(t);
                    } catch (ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                    break;
                }
            }
        }
    }
}
