package fr.business;

import java.util.List;
import java.util.Map;

import fr.entities.Client;
import fr.entities.Formation;
import fr.entities.Order;
import fr.entities.UserConnected;

public interface ShopService {
    void addClient(Client client);
    void updateClient(Client client);
    void deleteClient(Client client);
    Client getClientById(long id);
    List<Client> listClients();
    List<UserConnected>  listUserConnected();
    List<Formation> listFormations();
    List<Order> listOrders(UserConnected user);
    String placeOrder(UserConnected user, Map<Formation, Integer> items);
    void closeConnection();
   

}
