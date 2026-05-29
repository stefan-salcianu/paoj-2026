package com.pao.proiect.airline.repository;

import com.pao.proiect.airline.model.Avion;
import com.pao.proiect.airline.service.AuditService;
import com.pao.proiect.airline.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AvionRepository implements Repository<Avion, String> {

    private final Connection con = DatabaseConnection.getInstance().getConnection();
    private final AuditService auditService = AuditService.getInstance();

    @Override
    public void save(Avion a) {
        String sql = "INSERT INTO avioane (id, model, numar_inregistrare, capacitate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getId());
            ps.setString(2, a.getModel());
            ps.setString(3, a.getNumarInregistrare());
            ps.setInt(4, a.getCapacitate());
            ps.executeUpdate();
            auditService.logAction("adaugaAvion");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea avionului: " + a.getId(), e);
        }
    }

    @Override
    public Optional<Avion> findById(String id) {
        String sql = "SELECT * FROM avioane WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea avionului: " + id, e);
        }
    }

    @Override
    public List<Avion> findAll() {
        String sql = "SELECT * FROM avioane";
        List<Avion> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {


            throw new RuntimeException("Eroare la obtinerea tuturor avioanelor.", e);
        }
        return list;
    }

    @Override
    public void update(Avion a) {
        String sql = "UPDATE avioane SET model = ?, numar_inregistrare = ?, capacitate = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getModel());
            ps.setString(2, a.getNumarInregistrare());
            ps.setInt(3, a.getCapacitate());
            ps.setString(4, a.getId());
            ps.executeUpdate();
            auditService.logAction("actualizeazaAvion");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea avionului: " + a.getId(), e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM avioane WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            auditService.logAction("stergeAvion");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea avionului: " + id, e);
        }
    }

    private Avion mapRow(ResultSet rs) throws SQLException {
        return new Avion(
                rs.getString("id"),
                rs.getString("model"),
                rs.getString("numar_inregistrare"),
                rs.getInt("capacitate")
        );
    }
}
