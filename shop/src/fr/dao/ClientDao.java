package fr.dao;

import java.sql.Connection;
import java.util.List;
import fr.entities.Client;

public interface ClientDao {
    Client findById(long id, Connection connection);
    List<Client> findAll(Connection connection);
    void saveClient(Client client, Connection connection);
    void updateClient(Client client, Connection connection);
    void deleteClient(Client client, Connection connection);

}
