package com.pao.proiect.airline.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {

    private static AuditService instance;

    private static final String FILE_PATH = "audit.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    // synchronized garanteaza thread-safety: doua thread-uri nu scriu simultan in fisier
    public synchronized void logAction(String numeActiune) {
        String linie = numeActiune + "," + LocalDateTime.now().format(FORMATTER);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(linie);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("[AUDIT] Eroare la scrierea in fisierul de audit: " + e.getMessage());
        }
    }
}
