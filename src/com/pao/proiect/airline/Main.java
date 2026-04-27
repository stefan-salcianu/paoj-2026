package com.pao.proiect.airline;

import com.pao.proiect.airline.exception.ZborIndisponibilException;
import com.pao.proiect.airline.model.Aeroport;
import com.pao.proiect.airline.model.Avion;
import com.pao.proiect.airline.model.Bilet;
import com.pao.proiect.airline.model.Pasager;
import com.pao.proiect.airline.model.Pilot;
import com.pao.proiect.airline.model.Zbor;
import com.pao.proiect.airline.service.AeroportService;
import com.pao.proiect.airline.service.RezervareService;
import com.pao.proiect.airline.service.ZborService;

import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        AeroportService aeroportService = AeroportService.getInstance();
        ZborService zborService = ZborService.getInstance();
        RezervareService rezervareService = RezervareService.getInstance();

        System.out.println("=========================================");
        System.out.println("  SISTEM MANAGEMENT COMPANIE AERIANA");
        System.out.println("=========================================\n");

        System.out.println("--- Actiunea 1: Adaugare aeroporturi ---");
        Aeroport otopeni = new Aeroport("OTP", "Henri Coanda", "Bucuresti", "Romania");
        Aeroport heathrow = new Aeroport("LHR", "Heathrow", "Londra", "Marea Britanie");
        aeroportService.adaugaAeroport(otopeni);
        aeroportService.adaugaAeroport(heathrow);
        System.out.println();

        System.out.println("--- Actiunea 2: Adaugare avion ---");
        Avion avion = new Avion("AV001", "Boeing 737-800", "YR-BGP", 180);
        aeroportService.adaugaAvion(avion);
        System.out.println();

        System.out.println("--- Actiunea 3: Creare zbor ---");
        Zbor zbor = new Zbor(
                "RO101",
                otopeni,
                heathrow,
                LocalDateTime.of(2026, 5, 15, 10, 0),
                LocalDateTime.of(2026, 5, 15, 13, 30),
                avion
        );
        zborService.adaugaZbor(zbor);
        System.out.println();

        System.out.println("--- Actiunea 4: Inregistrare pasager ---");
        Pasager pasager = new Pasager(
                "PAS001", "Ionescu", "Andrei",
                "andrei.ionescu@email.com", "PP123456", "Romana"
        );
        rezervareService.inregistreazaPasager(pasager);
        System.out.println();

        System.out.println("--- Actiunea 5: Adaugare pilot ---");
        Pilot pilot = new Pilot(
                "PIL001", "Popescu", "Ion",
                "ion.popescu@airline.ro", "EMP001", 8500.0,
                "ATP-LIC-001", 5200
        );
        aeroportService.adaugaPilot(pilot);
        System.out.println();

        System.out.println("--- Actiunea 6: Alocare pilot la zbor ---");
        zborService.alocaPilot("RO101", pilot);
        Pilot pilotAlocat = zborService.getZborById("RO101").getPilot();
        System.out.println("  Pilot confirmat: " + pilotAlocat);
        System.out.println();

        System.out.println("--- Actiunea 7: Rezervare bilet ---");
        try {
            Bilet bilet = rezervareService.rezervaBilet(
                    "BIL001", "PAS001", "RO101", "Economy", 350.0
            );
            System.out.println("  Bilet confirmat: " + bilet);
        } catch (ZborIndisponibilException e) {
            System.out.println("  EROARE rezervare: " + e.getMessage());
        }
        System.out.println();

        System.out.println("--- Actiunea 8: Cautare zboruri spre Londra ---");
        List<Zbor> zboruriGasite = zborService.cautaDupaDestinatie("Londra");
        if (zboruriGasite.isEmpty()) {
            System.out.println("  Niciun zbor gasit spre Londra.");
        } else {
            zboruriGasite.forEach(z -> System.out.println("  >> " + z));
        }
        System.out.println();

        System.out.println("--- Actiunea 9: Pasageri zbor RO101 ---");
        List<Pasager> pasageriZbor = zborService.getPasageriZbor("RO101");
        if (pasageriZbor.isEmpty()) {
            System.out.println("  Niciun pasager inregistrat.");
        } else {
            pasageriZbor.forEach(p -> System.out.println("  >> " + p));
        }
        System.out.println();

        System.out.println("--- Actiunea 10: Anulare bilet BIL001 ---");
        rezervareService.anuleazaBilet("BIL001");
        int locuriDupaAnulare = zborService.getZborById("RO101").getLocuriDisponibile();
        System.out.println("  Locuri disponibile dupa anulare: " + locuriDupaAnulare);
        System.out.println();

    }
}
