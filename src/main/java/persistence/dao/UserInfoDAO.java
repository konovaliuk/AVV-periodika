package persistence.dao;

import model.UserInfo;
import persistence.database.DBContext;

public interface UserInfoDAO extends GenericReadonlyDAO<UserInfo, Long> {
    UserInfo findByLogin(String login, DBContext context) throws DAOException;
}
