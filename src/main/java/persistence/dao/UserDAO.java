package persistence.dao;

import model.User;
import persistence.database.DBContext;

public interface UserDAO extends GenericDAO<User, Long> {

    User findByLogin(String login, DBContext context) throws DAOException;

}
