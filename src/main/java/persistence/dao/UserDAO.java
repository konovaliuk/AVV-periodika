package persistence.dao;

import model.User;
import persistence.database.DbContext;

public interface UserDAO extends GenericDAO<User, Long> {
    User findByLogin(String login, DbContext context) throws DAOException;
}
