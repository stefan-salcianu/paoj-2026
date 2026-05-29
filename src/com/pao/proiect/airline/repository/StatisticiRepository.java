package com.pao.proiect.airline.repository;

import com.pao.proiect.airline.service.AuditService;
import com.pao.proiect.airline.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatisticiRepository {

    private static StatisticiRepository instance;

    private final Connection con = DatabaseConnection.getInstance().getConnection();
    private final AuditService auditService = AuditService.getInstance();

    private StatisticiRepository() {}

    public static StatisticiRepository getInstance() {
        if (instance == null) {
            instance = new StatisticiRepository();
        }
        return instance;
    }

    // -----------------------------------------------------------------
    //  JOIN 1: Toate zborurile cu numarul de pasageri imbarcati
    //  Tabele: zboruri JOIN aeroporturi(x2) LEFT JOIN bilete
    // -----------------------------------------------------------------
    public List<String> getZboruriCuNrPasageri() {
        String sql =
                "SELECT z.id_zbor, ap.oras AS plecare, as2.oras AS sosire, " +
                "       z.data_plecare, z.status, COUNT(b.id_bilet) AS nr_pasageri " +
                "FROM zboruri z " +
                "JOIN aeroporturi ap  ON z.aeroport_plecare_cod = ap.cod " +
                "JOIN aeroporturi as2 ON z.aeroport_sosire_cod  = as2.cod " +
                "LEFT JOIN bilete b   ON b.zbor_id = z.id_zbor " +
                "GROUP BY z.id_zbor, ap.oras, as2.oras, z.data_plecare, z.status " +
                "ORDER BY nr_pasageri DESC";

        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultate.add(String.format("Zbor %-8s | %-12s -> %-12s | %s | %-10s | %d pasageri",
                        rs.getString("id_zbor"),
                        rs.getString("plecare"),
                        rs.getString("sosire"),
                        rs.getTimestamp("data_plecare").toLocalDateTime().toLocalDate(),
                        rs.getString("status"),
                        rs.getInt("nr_pasageri")));
            }
            auditService.logAction("statisticiZboruriNrPasageri");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la obtinerea statisticilor zboruri.", e);
        }
        return rezultate;
    }

    // -----------------------------------------------------------------
    //  JOIN 2: Toti pasagerii cu numarul total de bilete rezervate
    //  Tabele: pasageri LEFT JOIN bilete
    // -----------------------------------------------------------------
    public List<String> getPasageriCuNrBilete() {
        String sql =
                "SELECT p.id, p.prenume, p.nume, p.nationalitate, " +
                "       COUNT(b.id_bilet) AS nr_bilete " +
                "FROM pasageri p " +
                "LEFT JOIN bilete b ON b.pasager_id = p.id " +
                "GROUP BY p.id, p.prenume, p.nume, p.nationalitate " +
                "ORDER BY nr_bilete DESC";

        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultate.add(String.format("%-30s | %-15s | %d bilete",
                        rs.getString("prenume") + " " + rs.getString("nume"),
                        rs.getString("nationalitate"),
                        rs.getInt("nr_bilete")));
            }
            auditService.logAction("statisticiPasageriNrBilete");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la obtinerea statisticilor pasageri.", e);
        }
        return rezultate;
    }

    // -----------------------------------------------------------------
    //  JOIN 3: Top N destinatii dupa numarul de zboruri programate
    //  Tabele: zboruri JOIN aeroporturi (aeroportul de sosire)
    // -----------------------------------------------------------------
    public List<String> getTopDestinatii(int n) {
        String sql =
                "SELECT a.oras, a.tara, COUNT(*) AS nr_zboruri " +
                "FROM zboruri z " +
                "JOIN aeroporturi a ON z.aeroport_sosire_cod = a.cod " +
                "GROUP BY a.oras, a.tara " +
                "ORDER BY nr_zboruri DESC " +
                "LIMIT ?";

        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, n);
            ResultSet rs = ps.executeQuery();
            int loc = 1;
            while (rs.next()) {
                rezultate.add(String.format("#%d %-20s (%s) — %d zboruri",
                        loc++,
                        rs.getString("oras"),
                        rs.getString("tara"),
                        rs.getInt("nr_zboruri")));
            }
            auditService.logAction("statisticiTopDestinatii");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la obtinerea top destinatii.", e);
        }
        return rezultate;
    }
}
