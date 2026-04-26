package com.pao.proiect.airline.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Zbor implements Comparable<Zbor> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private String idZbor;
    private Aeroport aeroportPlecare;
    private Aeroport aeroportSosire;
    private LocalDateTime dataPlecare;
    private LocalDateTime dataSosire;
    private Avion avion;
    private Pilot pilot;
    private StatusZbor status;
    private int locuriDisponibile;

    public Zbor(String idZbor, Aeroport aeroportPlecare, Aeroport aeroportSosire,
                LocalDateTime dataPlecare, LocalDateTime dataSosire, Avion avion) {
        this.idZbor = idZbor;
        this.aeroportPlecare = aeroportPlecare;
        this.aeroportSosire = aeroportSosire;
        this.dataPlecare = dataPlecare;
        this.dataSosire = dataSosire;
        this.avion = avion;
        this.status = StatusZbor.PROGRAMAT;
        this.locuriDisponibile = avion.getCapacitate();
    }

    public String getIdZbor() { return idZbor; }
    public void setIdZbor(String idZbor) { this.idZbor = idZbor; }

    public Aeroport getAeroportPlecare() { return aeroportPlecare; }
    public void setAeroportPlecare(Aeroport aeroportPlecare) { this.aeroportPlecare = aeroportPlecare; }

    public Aeroport getAeroportSosire() { return aeroportSosire; }
    public void setAeroportSosire(Aeroport aeroportSosire) { this.aeroportSosire = aeroportSosire; }

    public LocalDateTime getDataPlecare() { return dataPlecare; }
    public void setDataPlecare(LocalDateTime dataPlecare) { this.dataPlecare = dataPlecare; }

    public LocalDateTime getDataSosire() { return dataSosire; }
    public void setDataSosire(LocalDateTime dataSosire) { this.dataSosire = dataSosire; }

    public Avion getAvion() { return avion; }
    public void setAvion(Avion avion) { this.avion = avion; }

    public Pilot getPilot() { return pilot; }
    public void setPilot(Pilot pilot) { this.pilot = pilot; }

    public StatusZbor getStatus() { return status; }
    public void setStatus(StatusZbor status) { this.status = status; }

    public int getLocuriDisponibile() { return locuriDisponibile; }
    public void setLocuriDisponibile(int locuriDisponibile) { this.locuriDisponibile = locuriDisponibile; }

    /**
     * Sortare primara dupa dataPlecare; secundara dupa idZbor — garanteaza ordine stabila in TreeSet.
     */
    @Override
    public int compareTo(Zbor other) {
        int cmp = this.dataPlecare.compareTo(other.dataPlecare);
        if (cmp != 0) return cmp;
        return this.idZbor.compareTo(other.idZbor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zbor)) return false;
        Zbor zbor = (Zbor) o;
        return Objects.equals(idZbor, zbor.idZbor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idZbor);
    }

    @Override
    public String toString() {
        return "Zbor{id='" + idZbor +
               "', ruta=" + aeroportPlecare.getCod() + " -> " + aeroportSosire.getCod() +
               ", plecare=" + dataPlecare.format(FORMATTER) +
               ", sosire=" + dataSosire.format(FORMATTER) +
               ", status=" + status +
               ", locuriDisponibile=" + locuriDisponibile + "}";
    }
}
