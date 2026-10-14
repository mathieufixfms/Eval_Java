package fr;

import fr.business.ShopService;
import fr.business.ShopServiceImpl;
import fr.dao.ClientDao;
import fr.dao.ClientDaoImpl;
import fr.entities.Client;

public class App {
    public static void main(String[] args) {
        ClientDao dao = new ClientDaoImpl();
        ShopService service = new ShopServiceImpl(dao);

        try {
            System.out.println("Liste des clients :");
            for (Client client : service.listClients()) {
                System.out.println(client);
            }
        } finally {
            service.closeConnection();
        }
    }
}