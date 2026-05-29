package com.pao.proiect.airline.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class Bilet {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private final String idBilet;
    private final Pasager pasager;
    private final Zbor zbor;
    private final String clasa;
    private final double pret;
    private final LocalDateTime dataRezervare;

    public Bilet(String idBilet, Pasager pasager, Zbor zbor, String clasa, double pret) {
        this.idBilet = idBilet;
        this.pasager = pasager;
        this.zbor = zbor;
        this.clasa = clasa;
        this.pret = pret;
        this.dataRezervare = LocalDateTime.now();
    }

    // Constructor folosit de BiletRepository pentru reconstructia din baza de date
    public Bilet(String idBilet, Pasager pasager, Zbor zbor, String clasa, double pret,
                 LocalDateTime dataRezervare) {
        this.idBilet = idBilet;
        this.pasager = pasager;
        this.zbor = zbor;
        this.clasa = clasa;
        this.pret = pret;
        this.dataRezervare = dataRezervare;
    }

    public String getIdBilet() { return idBilet; }
    public Pasager getPasager() { return pasager; }
    public Zbor getZbor() { return zbor; }
    public String getClasa() { return clasa; }
    public double getPret() { return pret; }
    public LocalDateTime getDataRezervare() { return dataRezervare; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bilet)) return false;
        Bilet bilet = (Bilet) o;
        return Objects.equals(idBilet, bilet.idBilet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBilet);
    }

    @Override
    public String toString() {
        return "Bilet{id='" + idBilet +
               "', pasager='" + pasager.getNumeComplet() +
               "', zbor='" + zbor.getIdZbor() +
               "', clasa='" + clasa +
               "', pret=" + pret + " RON" +
               ", rezervatLa=" + dataRezervare.format(FORMATTER) + "}";
    }
}
