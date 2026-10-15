package fr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.entities.Formation;

public class FormationDaoImpl implements FormationDao {

    @Override
    public Formation findById(long id, Connection connection) {
        String sql = "SELECT id_formation, name_formation, description, duration, type, price "
            + "FROM FORMATION WHERE id_formation = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapFormation(rs);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to find formation with id " + id, e);
        }
        return null;
    }

    @Override
    public List<Formation> findAll(Connection connection) {
        String sql = "SELECT id_formation, name_formation, description, duration, type, price "
            + "FROM FORMATION ORDER BY id_formation";
        List<Formation> formations = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                formations.add(mapFormation(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to find all formations", e);
        }
        return formations;
    }

    @Override
    public void saveFormation(Formation formation, Connection connection) {
        String sql = "INSERT INTO FORMATION "
            + "(id_formation, name_formation, description, duration, type, price) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, formation.getId());
            ps.setString(2, formation.getName());
            ps.setString(3, formation.getDescription());
            ps.setInt(4, formation.getDuration());
            ps.setBoolean(5, formation.isType());
            ps.setDouble(6, formation.getPrice());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to save formation " + formation.getId(), e);
        }
    }

    @Override
    public void updateFormation(Formation formation, Connection connection) {
        String sql = "UPDATE FORMATION SET name_formation = ?, description = ?, duration = ?, "
            + "type = ?, price = ? WHERE id_formation = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, formation.getName());
            ps.setString(2, formation.getDescription());
            ps.setInt(3, formation.getDuration());
            ps.setBoolean(4, formation.isType());
            ps.setDouble(5, formation.getPrice());
            ps.setLong(6, formation.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to update formation " + formation.getId(), e);
        }
    }

    @Override
    public void deleteFormation(Formation formation, Connection connection) {
        String sql = "DELETE FROM FORMATION WHERE id_formation = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, formation.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to delete formation " + formation.getId(), e);
        }
    }

    private Formation mapFormation(ResultSet rs) throws SQLException {
        return new Formation(
            rs.getLong("id_formation"),
            rs.getString("name_formation"),
            rs.getString("description"),
            rs.getInt("duration"),
            rs.getBoolean("type"),
            rs.getDouble("price")
        );
    }

    @Override
    public List<Formation> findByType(boolean type, Connection connection) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByType'");
    }
}
