package com.pao.proiect.airline.service;

import com.pao.proiect.airline.exception.EntitateNegasitaException;
import com.pao.proiect.airline.exception.ZborIndisponibilException;
import com.pao.proiect.airline.model.Bilet;
import com.pao.proiect.airline.model.Pasager;
import com.pao.proiect.airline.model.StatusZbor;
import com.pao.proiect.airline.model.Zbor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RezervareService {

    private static RezervareService instance;

    private final Map<String, Pasager> pasageriById;
    private final List<Bilet> bilete;
    private final Set<String> biletIds;

    private RezervareService() {
        pasageriById = new HashMap<>();
        bilete = new ArrayList<>();
        biletIds = new HashSet<>();
    }

    public static RezervareService getInstance() {
        if (instance == null) {
            instance = new RezervareService();
        }
        return instance;
    }


    public void inregistreazaPasager(Pasager pasager) {
        if (pasager == null || pasager.getId() == null) {
            throw new IllegalArgumentException("Pasagerul sau ID-ul sau nu pot fi null.");
        }
        pasageriById.put(pasager.getId(), pasager);
        System.out.println("[INREGISTRAT] " + pasager);
    }

    public Pasager getPasagerById(String id) {
        if (!pasageriById.containsKey(id)) {
            throw new EntitateNegasitaException("Pasager", id);
        }
        return pasageriById.get(id);
    }

    public List<Pasager> getToatePasagerii() {
        return new ArrayList<>(pasageriById.values());
    }


    public Bilet rezervaBilet(String idBilet, String idPasager, String idZbor,
                              String clasa, double pret) throws ZborIndisponibilException {
        if (biletIds.contains(idBilet)) {
            throw new IllegalArgumentException("Biletul cu ID-ul '" + idBilet + "' exista deja.");
        }

        Pasager pasager = getPasagerById(idPasager);
        ZborService zborService = ZborService.getInstance();
        Zbor zbor = zborService.getZborById(idZbor);

        if (zbor.getStatus() == StatusZbor.ANULAT) {
            throw new ZborIndisponibilException(idZbor, "zborul a fost anulat");
        }
        if (zbor.getLocuriDisponibile() <= 0) {
            throw new ZborIndisponibilException(idZbor, "nu mai sunt locuri disponibile");
        }

        Bilet bilet = new Bilet(idBilet, pasager, zbor, clasa, pret);
        bilete.add(bilet);
        biletIds.add(idBilet);
        zbor.setLocuriDisponibile(zbor.getLocuriDisponibile() - 1);
        zborService.adaugaPasagerLaZbor(idZbor, pasager);

        System.out.println("[REZERVAT] " + bilet);
        return bilet;
    }

    public void anuleazaBilet(String idBilet) {
        Bilet bilet = getBiletById(idBilet);
        bilete.remove(bilet);
        biletIds.remove(idBilet);

        Zbor zbor = bilet.getZbor();
        zbor.setLocuriDisponibile(zbor.getLocuriDisponibile() + 1);
        ZborService.getInstance().eliminaPasagerDinZbor(zbor.getIdZbor(), bilet.getPasager());

        System.out.println("[ANULAT] Biletul cu ID: " + idBilet);
    }

    public Bilet getBiletById(String idBilet) {
        for (Bilet b : bilete) {
            if (b.getIdBilet().equals(idBilet)) {
                return b;
            }
        }
        throw new EntitateNegasitaException("Bilet", idBilet);
    }

    public List<Bilet> getBiletePasager(String idPasager) {
        List<Bilet> rezultate = new ArrayList<>();
        for (Bilet b : bilete) {
            if (b.getPasager().getId().equals(idPasager)) {
                rezultate.add(b);
            }
        }
        return rezultate;
    }

    public List<Bilet> getToateBiletele() {
        return Collections.unmodifiableList(bilete);
    }
}
