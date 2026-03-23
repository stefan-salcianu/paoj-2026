package com.pao.laboratory05.audit;

import java.time.LocalDateTime;
import java.util.Arrays;

public class AngajatService {
    // Cele două array-uri (unul pentru date, unul pentru istoric)
    private Angajat[] angajati = new Angajat[0];
    private AuditEntry[] auditLog = new AuditEntry[0];

    private AngajatService() {}

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    // --- Metoda nouă privată pentru AUDIT ---
    private void logAction(String action, String target) {
        // Obținem data și ora curentă exactă
        String timestamp = LocalDateTime.now().toString();
        AuditEntry entry = new AuditEntry(action, target, timestamp);

        // Resize pattern pentru array-ul de log-uri
        AuditEntry[] tmp = new AuditEntry[auditLog.length + 1];
        System.arraycopy(auditLog, 0, tmp, 0, auditLog.length);
        tmp[tmp.length - 1] = entry;
        auditLog = tmp;
    }

    // --- Metodele modificate pentru a include logarea ---
    public void addAngajat(Angajat a) {
        Angajat[] tmp = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, tmp, 0, angajati.length);
        tmp[tmp.length - 1] = a;
        angajati = tmp;

        System.out.println("Succes! Angajatul '" + a.getNume() + "' a fost adăugat.");

        // Înregistrăm acțiunea în log DUPĂ ce a fost adăugat
        logAction("ADD", a.getNume());
    }

    public void findByDepartament(String numeDept) {
        // Înregistrăm acțiunea LA ÎNCEPUT, conform cerinței
        logAction("FIND_BY_DEPT", numeDept);

        boolean gasit = false;
        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(a);
                gasit = true;
            }
        }

        if (!gasit) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }

    // Metodele nemodificate
    public void printAll() {
        if (angajati.length == 0) {
            System.out.println("Nu există angajați în sistem.");
            return;
        }
        for (Angajat a : angajati) {
            System.out.println(a);
        }
    }

    public void listBySalary() {
        if (angajati.length == 0) return;
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);
        for (Angajat a : copy) {
            System.out.println(a);
        }
    }

    // --- Metoda nouă de afișare a log-ului ---
    public void printAuditLog() {
        if (auditLog.length == 0) {
            System.out.println("Istoricul este gol.");
            return;
        }
        for (AuditEntry entry : auditLog) {
            System.out.println(entry);
        }
    }
}