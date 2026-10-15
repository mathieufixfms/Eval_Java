package fr.business;

import fr.dao.ClientDao;
import fr.dao.CommandDao;
import fr.dao.CommandDaoImpl;
import fr.dao.OrderDao;
import fr.dao.OrderDaoImpl;
import fr.dao.UserConnectedDao;
import fr.dao.FormationDao;
import fr.entities.Client;
import fr.entities.Command;
import fr.entities.Formation;
import fr.entities.Order;
import fr.entities.UserConnected;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShopServiceImpl implements ShopService {

    private final ClientDao clientDAO;
    private final UserConnectedDao userConnectedDAO;
    private final FormationDao formationDAO;
    private final CommandDao commandDAO = new CommandDaoImpl();
    private final OrderDao orderDAO = new OrderDaoImpl();
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
        this.userConnectedDAO = null;
        this.connection = this.initConnection();
        this.formationDAO = null;
    }

    public ShopServiceImpl(ClientDao clientDao2, UserConnectedDao userConnectedDao) {
        this.clientDAO = clientDao2;
        this.userConnectedDAO = userConnectedDao;
        this.connection = this.initConnection();    
        this.formationDAO = null;
    }

    public ShopServiceImpl(ClientDao clientDao2, UserConnectedDao userConnectedDao, FormationDao formationDao) {
        this.clientDAO = clientDao2;
        this.userConnectedDAO = userConnectedDao;
        this.formationDAO = formationDao;
        this.connection = this.initConnection();
    }

    public ShopServiceImpl(ClientDao clientDAO, UserConnectedDao userConnectedDAO, FormationDao formationDAO, Connection connection) {
        this.clientDAO = clientDAO;
        this.userConnectedDAO = userConnectedDAO;
        this.formationDAO = formationDAO;
        this.connection = connection;
    }

    public ShopServiceImpl(FormationDao formationDAO) {
        this.clientDAO = null;
        this.userConnectedDAO = null;
        this.formationDAO = formationDAO;
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

    @Override
    public List<UserConnected> listUserConnected() {
        return new ArrayList<>(userConnectedDAO.findAll(connection));
    }

    @Override
    public List<Formation> listFormations() {
        return formationDAO.findAll(connection);
    }

    @Override
    public List<Order> listOrders(UserConnected user) {
        if (user == null) {
            throw new IllegalArgumentException("A connected user is required to list orders");
        }
        if (connection == null) {
            throw new IllegalStateException("Cannot list orders: database connection is unavailable");
        }
        return orderDAO.findByUserConnectedId(user.getId(), connection);
    }

    @Override
    public String placeOrder(UserConnected user, Map<Formation, Integer> items) {
        if (user == null) {
            throw new IllegalArgumentException("A connected user is required to place an order");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("An order must contain at least one formation");
        }
        if (connection == null) {
            throw new IllegalStateException("Cannot place order: database connection is unavailable");
        }
        for (Map.Entry<Formation, Integer> item : items.entrySet()) {
            if (item.getKey() == null || item.getValue() == null || item.getValue() <= 0) {
                throw new IllegalArgumentException("Order formations and quantities must be valid");
            }
        }

        final boolean originalAutoCommit;
        try {
            originalAutoCommit = connection.getAutoCommit();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to start order transaction", e);
        }

        String commandId = UUID.randomUUID().toString();
        RuntimeException failure = null;
        try {
            connection.setAutoCommit(false);
            Command command = new Command(commandId, new Date());
            commandDAO.saveCommand(command, connection);
            for (Map.Entry<Formation, Integer> item : items.entrySet()) {
                orderDAO.saveOrder(
                    new Order(command, user, item.getKey(), item.getValue()),
                    connection
                );
            }
            connection.commit();
        } catch (SQLException e) {
            failure = new IllegalStateException("Unable to place order " + commandId, e);
            rollbackOrder(failure);
        } catch (RuntimeException e) {
            failure = e;
            rollbackOrder(failure);
        } finally {
            try {
                connection.setAutoCommit(originalAutoCommit);
            } catch (SQLException e) {
                if (failure == null) {
                    failure = new IllegalStateException("Unable to restore database connection", e);
                } else {
                    failure.addSuppressed(e);
                }
            }
        }

        if (failure != null) {
            throw failure;
        }
        return commandId;
    }

    private void rollbackOrder(RuntimeException failure) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            failure.addSuppressed(e);
        }
    }
}
