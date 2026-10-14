package fr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.entities.Command;
import fr.entities.Formation;
import fr.entities.Order;
import fr.entities.UserConnected;

public class OrderDaoImpl implements OrderDao {
    private static final String SELECT_ORDER = "SELECT o.id_formation, o.id_command, "
        + "o.id_user_connected, o.quantity, c.date_command, u.login_user_connected, "
        + "u.passeword_user_connected, f.name_formation, f.description, f.duration, "
        + "f.type, f.price FROM ORDER_ o "
        + "JOIN COMMAND c ON c.id_command = o.id_command "
        + "JOIN USER_CONNECTED u ON u.id_user_connected = o.id_user_connected "
        + "JOIN FORMATION f ON f.id_formation = o.id_formation ";

    @Override
    public Order findById(long formationId, String commandId, long userConnectedId,
                          Connection connection) {
        String sql = SELECT_ORDER + "WHERE o.id_formation = ? AND o.id_command = ? "
            + "AND o.id_user_connected = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            bindOrderKey(ps, formationId, commandId, userConnectedId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapOrder(rs);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to find order for command " + commandId, e);
        }
        return null;
    }

    @Override
    public List<Order> findAll(Connection connection) {
        String sql = SELECT_ORDER + "ORDER BY o.id_command, o.id_formation, o.id_user_connected";
        List<Order> orders = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                orders.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to list orders", e);
        }
        return orders;
    }

    @Override
    public void saveOrder(Order order, Connection connection) {
        String sql = "INSERT INTO ORDER_ (id_formation, id_command, id_user_connected, quantity) "
            + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            bindOrderKey(ps, order);
            ps.setInt(4, order.getQuantity());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to save order for command "
                + order.getCommand().getId(), e);
        }
    }

    @Override
    public void updateOrder(Order order, Connection connection) {
        String sql = "UPDATE ORDER_ SET quantity = ? WHERE id_formation = ? AND id_command = ? "
            + "AND id_user_connected = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, order.getQuantity());
            bindOrderKey(ps, order.getFormation().getId(), order.getCommand().getId(),
                order.getUserConnected().getId(), 2);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to update order for command "
                + order.getCommand().getId(), e);
        }
    }

    @Override
    public void deleteOrder(Order order, Connection connection) {
        String sql = "DELETE FROM ORDER_ WHERE id_formation = ? AND id_command = ? "
            + "AND id_user_connected = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            bindOrderKey(ps, order);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to delete order for command "
                + order.getCommand().getId(), e);
        }
    }

    private void bindOrderKey(PreparedStatement ps, Order order) throws SQLException {
        bindOrderKey(ps, order.getFormation().getId(), order.getCommand().getId(),
            order.getUserConnected().getId(), 1);
    }

    private void bindOrderKey(PreparedStatement ps, long formationId, String commandId,
                              long userConnectedId) throws SQLException {
        bindOrderKey(ps, formationId, commandId, userConnectedId, 1);
    }

    private void bindOrderKey(PreparedStatement ps, long formationId, String commandId,
                              long userConnectedId, int firstParameter) throws SQLException {
        ps.setLong(firstParameter, formationId);
        ps.setString(firstParameter + 1, commandId);
        ps.setLong(firstParameter + 2, userConnectedId);
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Command command = new Command(rs.getString("id_command"), rs.getDate("date_command"));
        UserConnected userConnected = new UserConnected(
            rs.getLong("id_user_connected"),
            rs.getString("login_user_connected"),
            rs.getString("passeword_user_connected")
        );
        Formation formation = new Formation(
            rs.getLong("id_formation"),
            rs.getString("name_formation"),
            rs.getString("description"),
            rs.getInt("duration"),
            rs.getBoolean("type"),
            rs.getDouble("price")
        );
        return new Order(command, userConnected, formation, rs.getInt("quantity"));
    }
}
