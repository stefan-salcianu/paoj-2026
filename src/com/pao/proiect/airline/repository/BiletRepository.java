package com.pao.proiect.airline.repository;

import com.pao.proiect.airline.model.Aeroport;
import com.pao.proiect.airline.model.Avion;
import com.pao.proiect.airline.exception.ZborIndisponibilException;
import com.pao.proiect.airline.model.Bilet;
import com.pao.proiect.airline.model.Pasager;
import com.pao.proiect.airline.model.Pilot;
import com.pao.proiect.airline.model.StatusZbor;
import com.pao.proiect.airline.model.Zbor;
import com.pao.proiect.airline.service.AuditService;
import com.pao.proiect.airline.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BiletRepository implements Repository<Bilet, String> {

    private final Connection con = DatabaseConnection.getInstance().getConnection();
    private final AuditService auditService = AuditService.getInstance();

    // JOIN complet: Bilet -> Pasager + Zbor -> Aeroport(x2) + Avion + Pilot(optional)
    private static final String SELECT_BASE =
            "SELECT b.id_bilet, b.clasa, b.pret, b.data_rezervare, " +
            "       p.id AS p_id, p.nume AS p_nume, p.prenume AS p_prenume, " +
            "       p.email AS p_email, p.pasaport_id, p.nationalitate, " +
            "       z.id_zbor, z.status, z.locuri_disponibile, z.data_plecare, z.data_sosire, " +
            "       z.avion_id, z.pilot_id, " +
            "       ap.cod AS ap_cod, ap.nume AS ap_nume, ap.oras AS ap_oras, ap.tara AS ap_tara, " +
            "       as2.cod AS as_cod, as2.nume AS as_nume, as2.oras AS as_oras, as2.tara AS as_tara, " +
            "       av.id AS av_id, av.model, av.numar_inregistrare, av.capacitate, " +
            "       pi.id AS pi_id, pi.nume AS pi_nume, pi.prenume AS pi_prenume, " +
            "       pi.email AS pi_email, pi.angajat_id, pi.salariu, pi.departament, " +
            "       pi.licenta, pi.ore_de_zbor " +
            "FROM bilete b " +
            "JOIN pasageri p      ON b.pasager_id = p.id " +
            "JOIN zboruri z       ON b.zbor_id = z.id_zbor " +
            "JOIN aeroporturi ap  ON z.aeroport_plecare_cod = ap.cod " +
            "JOIN aeroporturi as2 ON z.aeroport_sosire_cod  = as2.cod " +
            "JOIN avioane av      ON z.avion_id = av.id " +
            "LEFT JOIN piloti pi  ON z.pilot_id = pi.id ";

    @Override
    public void save(Bilet b) {
        String sql = "INSERT INTO bilete (id_bilet, pasager_id, zbor_id, clasa, pret, data_rezervare) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, b.getIdBilet());
            ps.setString(2, b.getPasager().getId());
            ps.setString(3, b.getZbor().getIdZbor());
            ps.setString(4, b.getClasa());
            ps.setDouble(5, b.getPret());
            ps.setTimestamp(6, Timestamp.valueOf(b.getDataRezervare()));
            ps.executeUpdate();
            auditService.logAction("rezervaBilet");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea biletului: " + b.getIdBilet(), e);
        }
    }

    @Override
    public Optional<Bilet> findById(String idBilet) {
        String sql = SELECT_BASE + "WHERE b.id_bilet = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idBilet);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea biletului: " + idBilet, e);
        }
    }

    @Override
    public List<Bilet> findAll() {
        List<Bilet> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SELECT_BASE);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la obtinerea tuturor biletelor.", e);
        }
        return list;
    }

    // Bilet este immutable in Java — nu se poate modifica dupa creare
    @Override
    public void update(Bilet bilet) {
        throw new UnsupportedOperationException("Biletul este immutable si nu poate fi modificat.");
    }

    @Override
    public void delete(String idBilet) {
        String sql = "DELETE FROM bilete WHERE id_bilet = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idBilet);
            ps.executeUpdate();
            auditService.logAction("anuleazaBilet");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea biletului: " + idBilet, e);
        }
    }

    public List<Bilet> findByPasager(String idPasager) {
        String sql = SELECT_BASE + "WHERE b.pasager_id = ?";
        List<Bilet> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idPasager);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la obtinerea biletelor pasagerului: " + idPasager, e);
        }
        return list;
    }

    public List<Bilet> findByZbor(String idZbor) {
        String sql = SELECT_BASE + "WHERE b.zbor_id = ?";
        List<Bilet> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idZbor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la obtinerea biletelor zborului: " + idZbor, e);
        }
        return list;
    }

    public Bilet rezervaBiletTransactional(String idBilet, Pasager pasager, Zbor zbor,
                                           String clasa, double pret)
            throws ZborIndisponibilException {

        if (zbor.getStatus() == StatusZbor.ANULAT) {
            throw new ZborIndisponibilException(zbor.getIdZbor(), "zborul a fost anulat");
        }
        if (zbor.getLocuriDisponibile() <= 0) {
            throw new ZborIndisponibilException(zbor.getIdZbor(), "nu mai sunt locuri disponibile");
        }

        java.time.LocalDateTime acum = java.time.LocalDateTime.now();

        try {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO bilete (id_bilet, pasager_id, zbor_id, clasa, pret, data_rezervare) " +
                    "VALUES (?, ?, ?, ?, ?, ?)")) {
                ps.setString(1, idBilet);
                ps.setString(2, pasager.getId());
                ps.setString(3, zbor.getIdZbor());
                ps.setString(4, clasa);
                ps.setDouble(5, pret);
                ps.setTimestamp(6, Timestamp.valueOf(acum));
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE zboruri SET locuri_disponibile = locuri_disponibile - 1 WHERE id_zbor = ?")) {
                ps.setString(1, zbor.getIdZbor());
                ps.executeUpdate();
            }

            con.commit();
            auditService.logAction("rezervaBiletTransactional");

            zbor.setLocuriDisponibile(zbor.getLocuriDisponibile() - 1);
            return new Bilet(idBilet, pasager, zbor, clasa, pret, acum);

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Eroare la rezervarea biletului: " + idBilet, e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void anuleazaBiletTransactional(String idBilet) {
        String zborId;
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT zbor_id FROM bilete WHERE id_bilet = ?")) {
            ps.setString(1, idBilet);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) throw new RuntimeException("Biletul '" + idBilet + "' nu exista.");
            zborId = rs.getString("zbor_id");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la gasirea biletului: " + idBilet, e);
        }

        try {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM bilete WHERE id_bilet = ?")) {
                ps.setString(1, idBilet);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE zboruri SET locuri_disponibile = locuri_disponibile + 1 WHERE id_zbor = ?")) {
                ps.setString(1, zborId);
                ps.executeUpdate();
            }

            con.commit();
            auditService.logAction("anuleazaBiletTransactional");

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Eroare la anularea biletului: " + idBilet, e);
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private Bilet mapRow(ResultSet rs) throws SQLException {
        Pasager pasager = new Pasager(
                rs.getString("p_id"), rs.getString("p_nume"), rs.getString("p_prenume"),
                rs.getString("p_email"), rs.getString("pasaport_id"), rs.getString("nationalitate")
        );

        Aeroport plecare = new Aeroport(
                rs.getString("ap_cod"), rs.getString("ap_nume"),
                rs.getString("ap_oras"), rs.getString("ap_tara")
        );
        Aeroport sosire = new Aeroport(
                rs.getString("as_cod"), rs.getString("as_nume"),
                rs.getString("as_oras"), rs.getString("as_tara")
        );
        Avion avion = new Avion(
                rs.getString("av_id"), rs.getString("model"),
                rs.getString("numar_inregistrare"), rs.getInt("capacitate")
        );

        Zbor zbor = new Zbor(
                rs.getString("id_zbor"), plecare, sosire,
                rs.getTimestamp("data_plecare").toLocalDateTime(),
                rs.getTimestamp("data_sosire").toLocalDateTime(),
                avion
        );
        zbor.setStatus(StatusZbor.valueOf(rs.getString("status")));
        zbor.setLocuriDisponibile(rs.getInt("locuri_disponibile"));

        String pilotId = rs.getString("pi_id");
        if (pilotId != null) {
            Pilot pilot = new Pilot(
                    pilotId,
                    rs.getString("pi_nume"), rs.getString("pi_prenume"),
                    rs.getString("pi_email"), rs.getString("angajat_id"),
                    rs.getDouble("salariu"), rs.getString("licenta"),
                    rs.getInt("ore_de_zbor")
            );
            zbor.setPilot(pilot);
        }

        return new Bilet(
                rs.getString("id_bilet"), pasager, zbor,
                rs.getString("clasa"), rs.getDouble("pret"),
                rs.getTimestamp("data_rezervare").toLocalDateTime()
        );
    }
}
