package com.pao.proiect.airline.repository;

import com.pao.proiect.airline.model.Pasager;
import com.pao.proiect.airline.service.AuditService;
import com.pao.proiect.airline.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PasagerRepository implements Repository<Pasager, String> {

    private final Connection con = DatabaseConnection.getInstance().getConnection();
    private final AuditService auditService = AuditService.getInstance();

    @Override
    public void save(Pasager p) {
        String sql = "INSERT INTO pasageri (id, nume, prenume, email, pasaport_id, nationalitate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getNume());
            ps.setString(3, p.getPrenume());
            ps.setString(4, p.getEmail());
            ps.setString(5, p.getPasaportId());
            ps.setString(6, p.getNationalitate());
            ps.executeUpdate();
            auditService.logAction("adaugaPasager");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea pasagerului: " + p.getId(), e);
        }
    }

    @Override
    public Optional<Pasager> findById(String id) {
        String sql = "SELECT * FROM pasageri WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea pasagerului: " + id, e);
        }
    }

    @Override
    public List<Pasager> findAll() {
        String sql = "SELECT * FROM pasageri";
        List<Pasager> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la obtinerea tuturor pasagerilor.", e);
        }
        return list;
    }

    @Override
    public void update(Pasager p) {
        String sql = "UPDATE pasageri SET nume = ?, prenume = ?, email = ?, pasaport_id = ?, nationalitate = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNume());
            ps.setString(2, p.getPrenume());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getPasaportId());
            ps.setString(5, p.getNationalitate());
            ps.setString(6, p.getId());
            ps.executeUpdate();
            auditService.logAction("actualizeazaPasager");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea pasagerului: " + p.getId(), e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM pasageri WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            auditService.logAction("stergePasager");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea pasagerului: " + id, e);
        }
    }

    private Pasager mapRow(ResultSet rs) throws SQLException {
        return new Pasager(
                rs.getString("id"),
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("email"),
                rs.getString("pasaport_id"),
                rs.getString("nationalitate")
        );
    }
}
