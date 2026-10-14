package fr.business;

import java.util.List;

import fr.entities.Client;

public interface ShopService {
    void addClient(Client client);
    void updateClient(Client client);
    void deleteClient(Client client);
    Client getClientById(long id);
    List<Client> listClients();
    void closeConnection();

}
