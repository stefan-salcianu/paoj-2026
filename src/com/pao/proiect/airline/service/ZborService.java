package com.pao.proiect.airline.service;

import com.pao.proiect.airline.exception.EntitateNegasitaException;
import com.pao.proiect.airline.model.Pasager;
import com.pao.proiect.airline.model.Pilot;
import com.pao.proiect.airline.model.Zbor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class ZborService {

    private static ZborService instance;

    /** Colectie sortata dupa dataPlecare (Zbor implements Comparable). */
    private final TreeSet<Zbor> zboruri;
    /** Index rapid dupa ID. */
    private final Map<String, Zbor> zborById;
    /** Pasageri pentru fiecare zbor, indexati dupa idZbor. */
    private final Map<String, List<Pasager>> pasageriPerZbor;

    private final AuditService auditService = AuditService.getInstance();

    private ZborService() {
        zboruri = new TreeSet<>();
        zborById = new HashMap<>();
        pasageriPerZbor = new HashMap<>();
    }

    public static ZborService getInstance() {
        if (instance == null) {
            instance = new ZborService();
        }
        return instance;
    }

    // ----------------------------- Zbor CRUD -----------------------------

    public void adaugaZbor(Zbor zbor) {
        if (zbor == null || zbor.getIdZbor() == null) {
            throw new IllegalArgumentException("Zborul sau ID-ul sau nu pot fi null.");
        }
        zboruri.add(zbor);
        zborById.put(zbor.getIdZbor(), zbor);
        pasageriPerZbor.put(zbor.getIdZbor(), new ArrayList<>());
        System.out.println("[ADAUGAT] " + zbor);
    }

    public Zbor getZborById(String idZbor) {
        if (!zborById.containsKey(idZbor)) {
            throw new EntitateNegasitaException("Zbor", idZbor);
        }
        return zborById.get(idZbor);
    }

    public void stergeZbor(String idZbor) {
        Zbor zbor = getZborById(idZbor);
        zboruri.remove(zbor);
        zborById.remove(idZbor);
        pasageriPerZbor.remove(idZbor);
        System.out.println("[STERS] Zborul cu ID: " + idZbor);
    }

    /** Returneaza zborurile in ordine cronologica (TreeSet garanteaza ordinea). */
    public List<Zbor> getToateZborurile() {
        return new ArrayList<>(zboruri);
    }

    // ----------------------------- Operatii zbor -----------------------------

    public List<Zbor> cautaDupaDestinatie(String orasSosire) {
        if (orasSosire == null) {
            throw new IllegalArgumentException("Orasul de sosire nu poate fi null.");
        }
        List<Zbor> rezultate = new ArrayList<>();
        for (Zbor z : zboruri) {
            if (z.getAeroportSosire().getOras().equalsIgnoreCase(orasSosire)) {
                rezultate.add(z);
            }
        }
        auditService.logAction("cautareZboruriDestinatie");
        return rezultate;
    }

    public void alocaPilot(String idZbor, Pilot pilot) {
        if (pilot == null) {
            throw new IllegalArgumentException("Pilotul nu poate fi null.");
        }
        Zbor zbor = getZborById(idZbor);
        zbor.setPilot(pilot);
        System.out.println("[ACTUALIZAT] Pilot " + pilot.getNumeComplet() + " alocat zborului " + idZbor);
    }

    // ----------------------------- Pasageri per zbor -----------------------------

    public void adaugaPasagerLaZbor(String idZbor, Pasager pasager) {
        if (pasager == null) {
            throw new IllegalArgumentException("Pasagerul nu poate fi null.");
        }
        getZborById(idZbor);
        pasageriPerZbor.get(idZbor).add(pasager);
    }

    public void eliminaPasagerDinZbor(String idZbor, Pasager pasager) {
        getZborById(idZbor);
        pasageriPerZbor.get(idZbor).remove(pasager);
    }

    public List<Pasager> getPasageriZbor(String idZbor) {
        if (!pasageriPerZbor.containsKey(idZbor)) {
            throw new EntitateNegasitaException("Zbor", idZbor);
        }
        auditService.logAction("vizualizarePasageriZbor");
        return Collections.unmodifiableList(pasageriPerZbor.get(idZbor));
    }
}
