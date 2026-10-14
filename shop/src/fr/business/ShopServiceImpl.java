package fr.business;

import fr.dao.ClientDao;
import fr.entities.Client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ShopServiceImpl implements ShopService {
    private final ClientDao clientDAO;
    private Connection connection;


    private Connection initConnection() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:mariadb://localhost:3306/shop";
        String login = "root";
        String password = "";

        	try {
    			return connection = DriverManager.getConnection(url,login,password);
    		} catch (SQLException e) {
    			e.printStackTrace();
    			return null;
    		}
    }
    
    public ShopServiceImpl(ClientDao clientDAO) {
        this.clientDAO = clientDAO;
        this.connection = this.initConnection();
    }

    @Override
    public void addClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        clientDAO.saveClient(client, connection);
    }

    @Override
    public void updateClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        clientDAO.updateClient(client, connection);
    }

    @Override
    public void deleteClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        clientDAO.deleteClient(client, connection);
    }

    @Override
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ClientDao getClientDAO() {
        return clientDAO;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Client getClientById(long id) {
        return clientDAO.findById(id, connection);
    }

    @Override 
    public List<Client> listClients(){
        return clientDAO.findAll(connection);
    }
}
