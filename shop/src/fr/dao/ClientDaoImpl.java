package fr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.entities.Client;
import fr.entities.UserConnected;

public class ClientDaoImpl implements ClientDao {

    @Override
    public Client findById(long id, Connection connection) {
        String sql = "SELECT c.id_client, c.name_client, c.surname_client, c.email_client, "
            + "c.adress_client, c.phone_client, u.id_user_connected, "
            + "u.login_user_connected, u.passeword_user_connected FROM CLIENT c "
            + "LEFT JOIN USER_CONNECTED u ON u.id_user_connected = c.id_user_connected "
            + "WHERE c.id_client = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapClient(rs);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to find client with id " + id, e);
        }
        return null;
    }

    @Override
    public void deleteClient(Client client, Connection connection) {
        String sql = "DELETE FROM CLIENT WHERE id_client = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, client.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to delete client " + client.getId(), e);
        }
    }

    @Override
    public List<Client> findAll(Connection connection) {
        String sql = "SELECT c.id_client, c.name_client, c.surname_client, c.email_client, "
            + "c.adress_client, c.phone_client, u.id_user_connected, "
            + "u.login_user_connected, u.passeword_user_connected FROM CLIENT c "
            + "LEFT JOIN USER_CONNECTED u ON u.id_user_connected = c.id_user_connected "
            + "ORDER BY c.id_client";
        List<Client> clients = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clients.add(mapClient(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to list clients", e);
        }
        return clients;
    }

    @Override
    public void saveClient(Client client, Connection connection) {
        String sql = "INSERT INTO CLIENT (id_client, name_client, surname_client, email_client, adress_client, phone_client, id_user_connected) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, client.getId());
            ps.setString(2, client.getName());
            ps.setString(3, client.getSurname());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getAddress());
            ps.setString(6, client.getPhoneNumber());
            ps.setLong(7, userConnectedId(client));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to save client " + client.getId(), e);
        }
    }

    private long userConnectedId(Client client) {
        UserConnected userConnected = client.getUserConnected();
        return userConnected.getId();
    }

    private Client mapClient(ResultSet rs) throws SQLException {
        UserConnected userConnected = null;
        long userId = rs.getLong("id_user_connected");
        if (!rs.wasNull()) {
            userConnected = new UserConnected(
                userId,
                rs.getString("login_user_connected"),
                rs.getString("passeword_user_connected")
            );
        }

        return new Client(
            rs.getLong("id_client"),
            rs.getString("name_client"),
            rs.getString("surname_client"),
            rs.getString("email_client"),
            rs.getString("adress_client"),
            rs.getString("phone_client"),
            userConnected
        );
    }

    @Override
    public void updateClient(Client client, Connection connection) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateClient'");
    }

}
