package fr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import fr.entities.Client;

public class ClientDaoImpl implements ClientDao {
    
    @Override 
    public Client findById(long id, Connection connection) {
        
        String sql = "SELECT id_client, name_client, surname_client, email_client, "
            + "adress_client, phone_client FROM CLIENT WHERE id_client = ?";

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
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Client> findAll(Connection connection) {
        String sql = "SELECT id_client, name_client, surname_client, email_client, "
            + "adress_client, phone_client FROM CLIENT ORDER BY id_client";
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
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateClient(Client client, Connection connection) {
        // TODO Auto-generated method stub
        
    }

    private Client mapClient(ResultSet rs) throws SQLException {
        return new Client(
            rs.getLong("id_client"),
            rs.getString("name_client"),
            rs.getString("surname_client"),
            rs.getString("email_client"),
            rs.getString("adress_client"),
            rs.getString("phone_client")
        );
    }
}
