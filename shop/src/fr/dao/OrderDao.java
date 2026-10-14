package fr.dao;

import java.sql.Connection;
import java.util.List;

import fr.entities.Order;

public interface OrderDao {
    Order findById(long formationId, String commandId, long userConnectedId, Connection connection);
    List<Order> findAll(Connection connection);
    void saveOrder(Order order, Connection connection);
    void updateOrder(Order order, Connection connection);
    void deleteOrder(Order order, Connection connection);
}
