package fr.dao;

import java.sql.Connection;
import java.util.List;
import fr.entities.Formation;

public interface FormationDao {
    Formation findById(long id, Connection connection);
    List<Formation> findAll(Connection connection);
    List<Formation> findByType(boolean type, Connection connection);
    void saveFormation(Formation formation, Connection connection);
    void updateFormation(Formation formation, Connection connection);
    void deleteFormation(Formation formation, Connection connection);
}
