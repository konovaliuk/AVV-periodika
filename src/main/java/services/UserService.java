package services;

import common.LoggerLoader;
import model.User;
import model.UserInfo;
import model.UserType;
import org.apache.log4j.Logger;
import persistence.dao.DAOFactory;
import persistence.database.DBContext;

import static common.ResourceManager.RM_DAO_USER_TYPE;
import static common.ResourceManager.USER_TYPE_ADMIN_ID;
import static common.ResourceManager.USER_TYPE_CLIENT_ID;

public class UserService {
    private static final Logger LOGGER = LoggerLoader.getLogger(UserService.class);

    public static UserInfo checkLogin(User user) {
        final UserInfo userInfo = DBContext
                .executeTransactionOrNull(context -> DAOFactory.getUserInfoDAO().findByLogin(user.getLogin(), context));
        if (userInfo != null && userInfo.getPassword().equals(user.getPassword())) {
            userInfo.setPassword(null);
            return userInfo;
        }
        //todo add log
        return null;
    }

    public static UserType findUserTypeClient() {
        return findUserType(RM_DAO_USER_TYPE.getLong(USER_TYPE_CLIENT_ID));
    }

    public static UserType findUserTypeAdmin() {
        return findUserType(RM_DAO_USER_TYPE.getLong(USER_TYPE_ADMIN_ID));
    }

    public static UserType findUserType(Long id) {
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getUserTypeDAO().findById(id, context));
    }

    public static Boolean existUserByLogin(String login) {
        return DBContext
                .executeTransactionOrNull(context -> DAOFactory.getUserDAO().findByLogin(login, context) != null);
    }

    public static User registerUser(User user) {
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getUserDAO().create(user, context));
    }
}
