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

    public ShopServiceImpl(ClientDao clientDAO, Connection connection) {
        this.clientDAO = clientDAO;
        this.connection = connection;
    }

    private Connection initConnection() {
        // Initialize and return the database connection
        // This is a placeholder; actual implementation will depend on your database setup
        try{
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:mariadb://localhost:3306/shop";
        String login = "root";
        String password = "";

        try {
            return connection = DriverManager.getConnection(url, login, password);
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to connect to the shop database", e);
        }
    }

    public ShopServiceImpl(ClientDao clientDAO) {
        this.clientDAO = clientDAO;
        this.connection = this.initConnection();
    }

    @Override
    public void addClient(Client client) {
        clientDAO.saveClient(client, connection);
    }   

    @Override
    public void updateClient(Client client) {
        clientDAO.updateClient(client, connection);
    }

    @Override
    public void deleteClient(Client client) {
        clientDAO.deleteClient(client, connection);
    }

    @Override
    public List<Client> listClients() {
        return clientDAO.findAll(connection);
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getClientById'");
    }

}
