package fr.dao;

import java.sql.Connection;
import java.util.List;

import fr.entities.Command;

public interface CommandDao {
    Command findById(String id, Connection connection);
    List<Command> findAll(Connection connection);
    void saveCommand(Command command, Connection connection);
    void updateCommand(Command command, Connection connection);
    void deleteCommand(Command command, Connection connection);
}
