package com.pao.proiect.airline;

import com.pao.proiect.airline.exception.ZborIndisponibilException;
import com.pao.proiect.airline.model.Aeroport;
import com.pao.proiect.airline.model.Avion;
import com.pao.proiect.airline.model.Bilet;
import com.pao.proiect.airline.model.Pasager;
import com.pao.proiect.airline.model.Pilot;
import com.pao.proiect.airline.model.Zbor;
import com.pao.proiect.airline.repository.AeroportRepository;
import com.pao.proiect.airline.repository.AvionRepository;
import com.pao.proiect.airline.repository.BiletRepository;
import com.pao.proiect.airline.repository.PasagerRepository;
import com.pao.proiect.airline.repository.PilotRepository;
import com.pao.proiect.airline.repository.StatisticiRepository;
import com.pao.proiect.airline.repository.ZborRepository;

import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        AeroportRepository aeroportRepo  = new AeroportRepository();
        AvionRepository    avionRepo     = new AvionRepository();
        PilotRepository    pilotRepo     = new PilotRepository();
        PasagerRepository  pasagerRepo   = new PasagerRepository();
        ZborRepository     zborRepo      = new ZborRepository();
        BiletRepository    biletRepo     = new BiletRepository();
        StatisticiRepository statisticiRepo = StatisticiRepository.getInstance();

        System.out.println("=========================================");
        System.out.println("  SISTEM MANAGEMENT COMPANIE AERIANA");
        System.out.println("=========================================\n");

        // Curatare date test anterioare (permite re-rulare fara duplicate key)
        System.out.println("--- Pregatire: stergere date test anterioare ---");
        try { biletRepo.delete("BIL001"); } catch (Exception ignored) {}
        try { zborRepo.delete("RO101");   } catch (Exception ignored) {}
        try { pilotRepo.delete("PIL001"); } catch (Exception ignored) {}
        try { pasagerRepo.delete("PAS001"); } catch (Exception ignored) {}
        try { avionRepo.delete("AV001");  } catch (Exception ignored) {}
        try { aeroportRepo.delete("OTP"); } catch (Exception ignored) {}
        try { aeroportRepo.delete("LHR"); } catch (Exception ignored) {}
        System.out.println("  Done.\n");

        System.out.println("--- Actiunea 1: Adaugare aeroporturi ---");
        Aeroport otopeni  = new Aeroport("OTP", "Henri Coanda", "Bucuresti", "Romania");
        Aeroport heathrow = new Aeroport("LHR", "Heathrow",     "Londra",    "Marea Britanie");
        aeroportRepo.save(otopeni);
        aeroportRepo.save(heathrow);
        aeroportRepo.findAll().forEach(a -> System.out.println("  [DB] " + a));
        System.out.println();

        System.out.println("--- Actiunea 2: Adaugare avion ---");
        Avion avion = new Avion("AV001", "Boeing 737-800", "YR-BGP", 180);
        avionRepo.save(avion);
        avionRepo.findById("AV001").ifPresent(a -> System.out.println("  [DB] " + a));
        System.out.println();

        System.out.println("--- Actiunea 3: Creare zbor ---");
        Zbor zbor = new Zbor(
                "RO101", otopeni, heathrow,
                LocalDateTime.of(2026, 5, 15, 10, 0),
                LocalDateTime.of(2026, 5, 15, 13, 30),
                avion
        );
        zborRepo.save(zbor);
        zborRepo.findById("RO101").ifPresent(z -> System.out.println("  [DB] " + z));
        System.out.println();

        System.out.println("--- Actiunea 4: Inregistrare pasager ---");
        Pasager pasager = new Pasager(
                "PAS001", "Ionescu", "Andrei",
                "andrei.ionescu@email.com", "PP123456", "Romana"
        );
        pasagerRepo.save(pasager);
        pasagerRepo.findById("PAS001").ifPresent(p -> System.out.println("  [DB] " + p));
        System.out.println();

        System.out.println("--- Actiunea 5: Adaugare pilot ---");
        Pilot pilot = new Pilot(
                "PIL001", "Popescu", "Ion",
                "ion.popescu@airline.ro", "EMP001", 8500.0,
                "ATP-LIC-001", 5200
        );
        pilotRepo.save(pilot);
        pilotRepo.findById("PIL001").ifPresent(p -> System.out.println("  [DB] " + p));
        System.out.println();

        System.out.println("--- Actiunea 6: Alocare pilot la zbor ---");
        zborRepo.updatePilot("RO101", pilot.getId());
        zborRepo.findById("RO101").ifPresent(z ->
                System.out.println("  Pilot confirmat: " + z.getPilot()));
        System.out.println();

        System.out.println("--- Actiunea 7: Rezervare bilet (tranzactional) ---");
        Zbor zborPentruRezervare = zborRepo.findById("RO101").orElseThrow();
        try {
            Bilet bilet = biletRepo.rezervaBiletTransactional(
                    "BIL001", pasager, zborPentruRezervare, "Economy", 350.0
            );
            System.out.println("  Bilet confirmat: " + bilet);
            int locuriDupaRezervare = zborRepo.findById("RO101")
                    .map(Zbor::getLocuriDisponibile).orElse(-1);
            System.out.println("  Locuri disponibile dupa rezervare: " + locuriDupaRezervare);
        } catch (ZborIndisponibilException e) {
            System.out.println("  EROARE rezervare: " + e.getMessage());
        }
        System.out.println();

        System.out.println("--- Actiunea 8: Cautare zboruri spre Londra ---");
        List<Zbor> zboruriGasite = zborRepo.findByDestinatie("Londra");
        if (zboruriGasite.isEmpty()) {
            System.out.println("  Niciun zbor gasit spre Londra.");
        } else {
            zboruriGasite.forEach(z -> System.out.println("  >> " + z));
        }
        System.out.println();

        System.out.println("--- Actiunea 9: Pasageri zbor RO101 (din bilete in DB) ---");
        List<Bilet> bileteZbor = biletRepo.findByZbor("RO101");
        if (bileteZbor.isEmpty()) {
            System.out.println("  Niciun pasager inregistrat.");
        } else {
            bileteZbor.forEach(b -> System.out.println("  >> " + b.getPasager()));
        }
        System.out.println();

        System.out.println("--- Actiunea 10: Anulare bilet BIL001 (tranzactional) ---");
        biletRepo.anuleazaBiletTransactional("BIL001");
        int locuriDupaAnulare = zborRepo.findById("RO101")
                .map(Zbor::getLocuriDisponibile).orElse(-1);
        System.out.println("  Locuri disponibile dupa anulare: " + locuriDupaAnulare);
        System.out.println();

        System.out.println("--- Actiunea 11: Statistici — zboruri cu nr. pasageri (JOIN) ---");
        statisticiRepo.getZboruriCuNrPasageri()
                .forEach(s -> System.out.println("  " + s));
        System.out.println();

        System.out.println("--- Actiunea 12: Statistici — pasageri cu nr. bilete (JOIN) ---");
        statisticiRepo.getPasageriCuNrBilete()
                .forEach(s -> System.out.println("  " + s));
        System.out.println();

        System.out.println("--- Actiunea 13: Top 3 destinatii dupa nr. zboruri (JOIN) ---");
        statisticiRepo.getTopDestinatii(3)
                .forEach(s -> System.out.println("  " + s));
        System.out.println();
    }
}
