package com.pao.proiect.airline.service;

import com.pao.proiect.airline.exception.EntitateNegasitaException;
import com.pao.proiect.airline.model.Aeroport;
import com.pao.proiect.airline.model.Avion;
import com.pao.proiect.airline.model.Pilot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AeroportService {

    private static AeroportService instance;

    private final Map<String, Aeroport> aeroporturiById;
    private final Map<String, Avion> avioanById;
    private final Map<String, Pilot> pilotiById;
    private final List<Pilot> listaPiloti;

    private AeroportService() {
        aeroporturiById = new HashMap<>();
        avioanById = new HashMap<>();
        pilotiById = new HashMap<>();
        listaPiloti = new ArrayList<>();
    }

    public static AeroportService getInstance() {
        if (instance == null) {
            instance = new AeroportService();
        }
        return instance;
    }


    public void adaugaAeroport(Aeroport aeroport) {
        if (aeroport == null || aeroport.getCod() == null) {
            throw new IllegalArgumentException("Aeroportul sau codul sau nu pot fi null.");
        }
        aeroporturiById.put(aeroport.getCod(), aeroport);
        System.out.println("[ADAUGAT] " + aeroport);
    }

    public Aeroport getAeroportByCod(String cod) {
        if (!aeroporturiById.containsKey(cod)) {
            throw new EntitateNegasitaException("Aeroport", cod);
        }
        return aeroporturiById.get(cod);
    }

    public void stergeAeroport(String cod) {
        if (!aeroporturiById.containsKey(cod)) {
            throw new EntitateNegasitaException("Aeroport", cod);
        }
        aeroporturiById.remove(cod);
        System.out.println("[STERS] Aeroportul cu codul: " + cod);
    }

    public List<Aeroport> getToateAeroporturile() {
        return new ArrayList<>(aeroporturiById.values());
    }


    public void adaugaAvion(Avion avion) {
        if (avion == null || avion.getId() == null) {
            throw new IllegalArgumentException("Avionul sau ID-ul sau nu pot fi null.");
        }
        avioanById.put(avion.getId(), avion);
        System.out.println("[ADAUGAT] " + avion);
    }

    public Avion getAvionById(String id) {
        if (!avioanById.containsKey(id)) {
            throw new EntitateNegasitaException("Avion", id);
        }
        return avioanById.get(id);
    }

    public void stergeAvion(String id) {
        if (!avioanById.containsKey(id)) {
            throw new EntitateNegasitaException("Avion", id);
        }
        avioanById.remove(id);
        System.out.println("[STERS] Avionul cu ID: " + id);
    }

    public List<Avion> getToateAvioanele() {
        return new ArrayList<>(avioanById.values());
    }


    public void adaugaPilot(Pilot pilot) {
        if (pilot == null || pilot.getId() == null) {
            throw new IllegalArgumentException("Pilotul sau ID-ul sau nu pot fi null.");
        }
        pilotiById.put(pilot.getId(), pilot);
        listaPiloti.add(pilot);
        System.out.println("[ADAUGAT] " + pilot);
    }

    public Pilot getPilotById(String id) {
        if (!pilotiById.containsKey(id)) {
            throw new EntitateNegasitaException("Pilot", id);
        }
        return pilotiById.get(id);
    }

    public void stergePilot(String id) {
        Pilot pilot = getPilotById(id);
        pilotiById.remove(id);
        listaPiloti.remove(pilot);
        System.out.println("[STERS] Pilotul cu ID: " + id);
    }

    public List<Pilot> getToatePilotii() {
        return Collections.unmodifiableList(listaPiloti);
    }
}
