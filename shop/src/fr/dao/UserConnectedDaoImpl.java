package fr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.entities.UserConnected;

public class UserConnectedDaoImpl implements UserConnectedDao {

    @Override
    public UserConnected findById(long id, Connection connection) {
        String sql = "SELECT id_user_connected, login_user_connected, passeword_user_connected "
            + "FROM USER_CONNECTED WHERE id_user_connected = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUserConnected(rs);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to find user with id " + id, e);
        }
        return null;
    }

    @Override
    public List<UserConnected> findAll(Connection connection) {
        String sql = "SELECT id_user_connected, login_user_connected, passeword_user_connected "
            + "FROM USER_CONNECTED ORDER BY id_user_connected";
        List<UserConnected> users = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(mapUserConnected(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to list users", e);
        }
        return users;
    }

    @Override
    public void saveUserConnected(UserConnected userConnected, Connection connection) {
        String sql = "INSERT INTO USER_CONNECTED "
            + "(id_user_connected, login_user_connected, passeword_user_connected) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userConnected.getId());
            ps.setString(2, userConnected.getLogin());
            ps.setString(3, userConnected.getPasseword());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to save user " + userConnected.getId(), e);
        }
    }

    @Override
    public void updateUserConnected(UserConnected userConnected, Connection connection) {
        String sql = "UPDATE USER_CONNECTED SET login_user_connected = ?, "
            + "passeword_user_connected = ? WHERE id_user_connected = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userConnected.getLogin());
            ps.setString(2, userConnected.getPasseword());
            ps.setLong(3, userConnected.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to update user " + userConnected.getId(), e);
        }
    }

    @Override
    public void deleteUserConnected(UserConnected userConnected, Connection connection) {
        String sql = "DELETE FROM USER_CONNECTED WHERE id_user_connected = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userConnected.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to delete user " + userConnected.getId(), e);
        }
    }

    private UserConnected mapUserConnected(ResultSet rs) throws SQLException {
        return new UserConnected(
            rs.getLong("id_user_connected"),
            rs.getString("login_user_connected"),
            rs.getString("passeword_user_connected")
        );
    }
}
