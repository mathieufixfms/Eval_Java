package fr.dao;

import java.sql.Connection;
import java.util.List;

import fr.entities.UserConnected;

public interface UserConnectedDao {
    UserConnected findById(long id, Connection connection);
    List<UserConnected> findAll(Connection connection);
    void saveUserConnected(UserConnected userConnected, Connection connection);
    void updateUserConnected(UserConnected userConnected, Connection connection);
    void deleteUserConnected(UserConnected userConnected, Connection connection);
}
