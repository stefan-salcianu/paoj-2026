package com.pao.proiect.airline.repository;

import com.pao.proiect.airline.model.Aeroport;
import com.pao.proiect.airline.service.AuditService;
import com.pao.proiect.airline.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AeroportRepository implements Repository<Aeroport, String> {

    private final Connection con = DatabaseConnection.getInstance().getConnection();
    private final AuditService auditService = AuditService.getInstance();

    @Override
    public void save(Aeroport a) {
        String sql = "INSERT INTO aeroporturi (cod, nume, oras, tara) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getCod());
            ps.setString(2, a.getNume());
            ps.setString(3, a.getOras());
            ps.setString(4, a.getTara());
            ps.executeUpdate();
            auditService.logAction("adaugaAeroport");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea aeroportului: " + a.getCod(), e);
        }
    }

    @Override
    public Optional<Aeroport> findById(String cod) {
        String sql = "SELECT * FROM aeroporturi WHERE cod = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cod);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea aeroportului: " + cod, e);
        }
    }

    @Override
    public List<Aeroport> findAll() {
        String sql = "SELECT * FROM aeroporturi";
        List<Aeroport> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la obtinerea tuturor aeroporturilor.", e);
        }
        return list;
    }

    @Override
    public void update(Aeroport a) {
        String sql = "UPDATE aeroporturi SET nume = ?, oras = ?, tara = ? WHERE cod = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getNume());
            ps.setString(2, a.getOras());
            ps.setString(3, a.getTara());
            ps.setString(4, a.getCod());
            ps.executeUpdate();
            auditService.logAction("actualizeazaAeroport");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea aeroportului: " + a.getCod(), e);
        }
    }

    @Override
    public void delete(String cod) {
        String sql = "DELETE FROM aeroporturi WHERE cod = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cod);
            ps.executeUpdate();
            auditService.logAction("stergeAeroport");
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea aeroportului: " + cod, e);
        }
    }

    private Aeroport mapRow(ResultSet rs) throws SQLException {
        return new Aeroport(
                rs.getString("cod"),
                rs.getString("nume"),
                rs.getString("oras"),
                rs.getString("tara")
        );
    }
}
