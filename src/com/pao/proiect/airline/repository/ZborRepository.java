package com.pao.proiect.airline.repository;

import com.pao.proiect.airline.model.Aeroport;
import com.pao.proiect.airline.model.Avion;
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

public class ZborRepository implements Repository<Zbor, String> {

    private final Connection con = DatabaseConnection.getInstance().getConnection();
    private final AuditService auditService = AuditService.getInstance();

    // JOIN complet: reconstruieste Zbor cu Aeroport(x2) + Avion + Pilot (optional)
    private static final String SELECT_BASE =
            "SELECT z.id_zbor, z.status, z.locuri_disponibile, z.data_plecare, z.data_sosire, " +
            "       z.avion_id, z.pilot_id, " +
            "       ap.cod AS ap_cod, ap.nume AS ap_nume, ap.oras AS ap_oras, ap.tara AS ap_tara, " +
            "       as2.cod AS as_cod, as2.nume AS as_nume, as2.oras AS as_oras, as2.tara AS as_tara, " +
            "       av.id AS av_id, av.model, av.numar_inregistrare, av.capacitate, " +
            "       pi.id AS pi_id, pi.nume AS pi_nume, pi.prenume AS pi_prenume, " +
            "       pi.email AS pi_email, pi.angajat_id, pi.salariu, pi.departament, " +
            "       pi.licenta, pi.ore_de_zbor " +
            "FROM zboruri z " +
            "JOIN aeroporturi ap  ON z.aeroport_plecare_cod = ap.cod " +
            "JOIN aeroporturi as2 ON z.aeroport_sosire_cod  = as2.cod " +
            "JOIN avioane av      ON z.avion_id = av.id " +
            "LEFT JOIN piloti pi  ON z.pilot_id = pi.id ";

    @Override
    public void save(Zbor z) {
        String sql = "INSERT INTO zboruri (id_zbor, aeroport_plecare_cod, aeroport_sosire_cod, " +
                     "data_plecare, data_sosire, avion_id, pilot_id, status, locuri_disponibile) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, z.getIdZbor());
            ps.setString(2, z.getAeroportPlecare().getCod());
            ps.setString(3, z.getAeroportSosire().getCod());
            ps.setTimestamp(4, Timestamp.valueOf(z.getDataPlecare()));
            ps.setTimestamp(5, Timestamp.valueOf(z.getDataSosire()));
            ps.setString(6, z.getAvion().getId());
            ps.setString(7, z.getPilot() != null ? z.getPilot().getId() : null);
            ps.setString(8, z.getStatus().name());
            ps.setInt(9, z.getLocuriDisponibile());
            ps.executeUpdate();
            auditService.logAction("adaugaZbor");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea zborului: " + z.getIdZbor(), e);
        }
    }

    @Override
    public Optional<Zbor> findById(String idZbor) {
        String sql = SELECT_BASE + "WHERE z.id_zbor = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idZbor);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea zborului: " + idZbor, e);
        }
    }

    @Override
    public List<Zbor> findAll() {
        List<Zbor> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SELECT_BASE);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la obtinerea tuturor zborurilor.", e);
        }
        return list;
    }

    @Override
    public void update(Zbor z) {
        String sql = "UPDATE zboruri SET aeroport_plecare_cod = ?, aeroport_sosire_cod = ?, " +
                     "data_plecare = ?, data_sosire = ?, avion_id = ?, pilot_id = ?, " +
                     "status = ?, locuri_disponibile = ? WHERE id_zbor = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, z.getAeroportPlecare().getCod());
            ps.setString(2, z.getAeroportSosire().getCod());
            ps.setTimestamp(3, Timestamp.valueOf(z.getDataPlecare()));
            ps.setTimestamp(4, Timestamp.valueOf(z.getDataSosire()));
            ps.setString(5, z.getAvion().getId());
            ps.setString(6, z.getPilot() != null ? z.getPilot().getId() : null);
            ps.setString(7, z.getStatus().name());
            ps.setInt(8, z.getLocuriDisponibile());
            ps.setString(9, z.getIdZbor());
            ps.executeUpdate();
            auditService.logAction("actualizeazaZbor");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea zborului: " + z.getIdZbor(), e);
        }
    }

    @Override
    public void delete(String idZbor) {
        String sql = "DELETE FROM zboruri WHERE id_zbor = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idZbor);
            ps.executeUpdate();
            auditService.logAction("stergeZbor");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea zborului: " + idZbor, e);
        }
    }

    public List<Zbor> findByDestinatie(String oras) {
        String sql = SELECT_BASE + "WHERE LOWER(as2.oras) = LOWER(?)";
        List<Zbor> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, oras);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
            auditService.logAction("cautareZboruriDestinatie");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea zborurilor spre: " + oras, e);
        }
        return list;
    }

    public void updateStatus(String idZbor, StatusZbor status) {
        String sql = "UPDATE zboruri SET status = ? WHERE id_zbor = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setString(2, idZbor);
            ps.executeUpdate();
            auditService.logAction("actualizeazaStatusZbor");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea statusului zborului: " + idZbor, e);
        }
    }

    public void updateLocuriDisponibile(String idZbor, int locuri) {
        String sql = "UPDATE zboruri SET locuri_disponibile = ? WHERE id_zbor = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, locuri);
            ps.setString(2, idZbor);
            ps.executeUpdate();
            auditService.logAction("actualizeazaLocuriZbor");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea locurilor zborului: " + idZbor, e);
        }
    }

    public void updatePilot(String idZbor, String idPilot) {
        String sql = "UPDATE zboruri SET pilot_id = ? WHERE id_zbor = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idPilot);
            ps.setString(2, idZbor);
            ps.executeUpdate();
            auditService.logAction("alocaPilotZbor");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la alocarea pilotului pentru zborul: " + idZbor, e);
        }
    }

    private Zbor mapRow(ResultSet rs) throws SQLException {
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

        // Pilotul este optional (LEFT JOIN)
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

        return zbor;
    }
}
