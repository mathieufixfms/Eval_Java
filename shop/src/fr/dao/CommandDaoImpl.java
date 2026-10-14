package fr.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.entities.Command;

public class CommandDaoImpl implements CommandDao {

    @Override
    public Command findById(String id, Connection connection) {
        String sql = "SELECT id_command, date_command FROM COMMAND WHERE id_command = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCommand(rs);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to find command with id " + id, e);
        }
        return null;
    }

    @Override
    public List<Command> findAll(Connection connection) {
        String sql = "SELECT id_command, date_command FROM COMMAND ORDER BY id_command";
        List<Command> commands = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                commands.add(mapCommand(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to list commands", e);
        }
        return commands;
    }

    @Override
    public void saveCommand(Command command, Connection connection) {
        String sql = "INSERT INTO COMMAND (id_command, date_command) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, command.getId());
            ps.setDate(2, new Date(command.getDate_command().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to save command " + command.getId(), e);
        }
    }

    @Override
    public void updateCommand(Command command, Connection connection) {
        String sql = "UPDATE COMMAND SET date_command = ? WHERE id_command = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new Date(command.getDate_command().getTime()));
            ps.setString(2, command.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to update command " + command.getId(), e);
        }
    }

    @Override
    public void deleteCommand(Command command, Connection connection) {
        String sql = "DELETE FROM COMMAND WHERE id_command = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, command.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to delete command " + command.getId(), e);
        }
    }

    private Command mapCommand(ResultSet rs) throws SQLException {
        return new Command(rs.getString("id_command"), rs.getDate("date_command"));
    }
}
