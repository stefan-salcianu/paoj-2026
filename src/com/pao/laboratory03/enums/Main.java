package com.pao.laboratory03.enums;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== Toate prioritățile ===");
        for (Priority p : Priority.values()) {
            System.out.printf("%s %s (level=%d, color=%s)\n",
                    p.getEmoji(), p.name(), p.getLevel(), p.getColor());
        }

        System.out.println("\n=== Switch pe prioritate ===");
        Priority prioritateCurenta = Priority.HIGH;

        switch (prioritateCurenta) {
            case LOW:
                System.out.println("Totul este relaxat.");
                break;
            case MEDIUM:
                System.out.println("Atenție normală.");
                break;
            case HIGH:
                System.out.println("⚠️ Atenție! Prioritate ridicată!");
                break;
            case CRITICAL:
                System.out.println("🔥 PERICOL IMINENT!");
                break;
        }

        System.out.println("\n=== valueOf ===");
        Priority prioritateDinText = Priority.valueOf("HIGH");
        System.out.println("Priority.valueOf(\"HIGH\") = " + prioritateDinText);

        System.out.println("\n=== Comparare enum ===");
        System.out.println("HIGH == HIGH? " + (Priority.HIGH == Priority.HIGH));
        System.out.println("HIGH == LOW? " + (Priority.HIGH == Priority.LOW));

        System.out.println("\n=== name() și ordinal() ===");
        for (Priority p : Priority.values()) {
            System.out.printf("%s: name=%s, ordinal=%d\n", p.name(), p.name(), p.ordinal());
        }
    }
}