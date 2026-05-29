package com.pao.proiect.airline.repository;

import com.pao.proiect.airline.model.Pilot;
import com.pao.proiect.airline.service.AuditService;
import com.pao.proiect.airline.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PilotRepository implements Repository<Pilot, String> {

    private final Connection con = DatabaseConnection.getInstance().getConnection();
    private final AuditService auditService = AuditService.getInstance();

    @Override
    public void save(Pilot p) {
        String sql = "INSERT INTO piloti (id, nume, prenume, email, angajat_id, salariu, departament, licenta, ore_de_zbor) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getNume());
            ps.setString(3, p.getPrenume());
            ps.setString(4, p.getEmail());
            ps.setString(5, p.getAngajatId());
            ps.setDouble(6, p.getSalariu());
            ps.setString(7, p.getDepartament());
            ps.setString(8, p.getLicenta());
            ps.setInt(9, p.getOreDeZbor());
            ps.executeUpdate();
            auditService.logAction("adaugaPilot");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea pilotului: " + p.getId(), e);
        }
    }

    @Override
    public Optional<Pilot> findById(String id) {
        String sql = "SELECT * FROM piloti WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea pilotului: " + id, e);
        }
    }

    @Override
    public List<Pilot> findAll() {
        String sql = "SELECT * FROM piloti";
        List<Pilot> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la obtinerea tuturor pilotilor.", e);
        }
        return list;
    }

    @Override
    public void update(Pilot p) {
        String sql = "UPDATE piloti SET nume = ?, prenume = ?, email = ?, angajat_id = ?, " +
                     "salariu = ?, departament = ?, licenta = ?, ore_de_zbor = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNume());
            ps.setString(2, p.getPrenume());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getAngajatId());
            ps.setDouble(5, p.getSalariu());
            ps.setString(6, p.getDepartament());
            ps.setString(7, p.getLicenta());
            ps.setInt(8, p.getOreDeZbor());
            ps.setString(9, p.getId());
            ps.executeUpdate();
            auditService.logAction("actualizeazaPilot");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea pilotului: " + p.getId(), e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM piloti WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            auditService.logAction("stergePilot");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea pilotului: " + id, e);
        }
    }

    private Pilot mapRow(ResultSet rs) throws SQLException {
        return new Pilot(
                rs.getString("id"),
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("email"),
                rs.getString("angajat_id"),
                rs.getDouble("salariu"),
                rs.getString("licenta"),
                rs.getInt("ore_de_zbor")
        );
    }
}
