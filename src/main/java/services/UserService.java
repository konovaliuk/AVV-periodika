package services;

import common.LoggerLoader;
import controller.validation.Matcher;
import controller.validation.UserMatching;
import model.User;
import model.UserInfo;
import model.UserType;
import org.apache.log4j.Logger;
import persistence.dao.DAOException;
import persistence.dao.DAOFactory;
import persistence.dao.UserDAO;
import persistence.database.DBContext;

import static common.ResourceManager.RM_DAO_USER_TYPE;
import static common.ResourceManager.USER_TYPE_CLIENT_ID;
import static common.ViewConstants.INPUT_USER_LOGIN;

public class UserService {
    private static final Logger LOGGER = LoggerLoader.getLogger(UserService.class);

    public static UserInfo serveLogin(User user) {
        final UserInfo userInfo = DBContext
                .executeTransactionOrNull(context -> DAOFactory.getUserInfoDAO().findByLogin(user.getLogin(), context));
        if (userInfo != null && userInfo.getPassword().equals(user.getPassword())) {
            userInfo.setPassword(null);
            return userInfo;
        }
        //todo add log
        return null;
    }

    //    public static UserType findUserTypeClient() {
    //        return findUserType(RM_DAO_USER_TYPE.getLong(USER_TYPE_CLIENT_ID));
    //    }
    //
    //    public static UserType findUserTypeAdmin() {
    //        return findUserType(RM_DAO_USER_TYPE.getLong(USER_TYPE_ADMIN_ID));
    //    }
    //
    //    public static UserType findUserType(Long id) {
    //        return DBContext.executeTransactionOrNull(context -> DAOFactory.getUserTypeDAO().findById(id, context));
    //    }
    //

    public static void serveRegisterUser(StateHolderSaveEntity<User> state) {
        DBContext dbContext = new DBContext();
        UserDAO userDAO = DAOFactory.getUserDAO();
        User user = state.getEntity();
        state.setValidationInfo(Matcher.match(user, new UserMatching(state.getLanguage())));
        if (state.getValidationInfo().containsValue(false)) {
            state.setResultState(StateHolderSaveEntity.State.ERROR_WRONG_PARAMETERS);
            return;
        }
        try {
            if (userDAO.findByLogin(user.getLogin(), dbContext) != null) {
                state.getValidationInfo().put(INPUT_USER_LOGIN, false);
                state.setResultState(StateHolderSaveEntity.State.ERROR_WRONG_PARAMETERS);
                return;
            }
            UserType userTypeClient =
                    DAOFactory.getUserTypeDAO().findById(RM_DAO_USER_TYPE.getLong(USER_TYPE_CLIENT_ID), dbContext);
            if (userTypeClient == null) {
                state.setResultState(StateHolderSaveEntity.State.ERROR_ENTITY_NOT_SAVED);
                return;
            }
            user.setUserTypeId(userTypeClient.getId());
            User newUser = userDAO.create(user, dbContext);
            if (newUser == null) {
                state.setResultState(StateHolderSaveEntity.State.ERROR_ENTITY_NOT_SAVED);
                return;
            }
            state.setEntity(newUser);
            state.setResultState(StateHolderSaveEntity.State.SUCCESS);
        } catch (DAOException e) {
            LOGGER.error(e);
            state.setResultState(StateHolderSaveEntity.State.ERROR_ENTITY_NOT_SAVED);
        } finally {
            dbContext.closeConnection();
        }
    }
}
