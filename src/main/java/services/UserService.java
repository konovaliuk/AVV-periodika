package services;

import common.LoggerLoader;
import controller.validation.Validator;
import controller.validation.impl.UserValidating;
import model.User;
import model.UserType;
import org.apache.log4j.Logger;
import persistence.dao.DAOException;
import persistence.dao.DAOFactory;
import persistence.dao.UserDAO;
import persistence.database.DBContext;
import services.sto.StateHolderSaveEntity;

import static common.ResourceManager.MESSAGE_REGISTER_EXIST;
import static common.ResourceManager.RM_DAO_USER_TYPE;
import static common.ResourceManager.USER_TYPE_CLIENT_ID;
import static common.ViewConstants.INPUT_USER_LOGIN;

public class UserService {
    private static final Logger LOGGER = LoggerLoader.getLogger(UserService.class);

    public static User serveLogin(User user) {
        User userInfo = DBContext
                .executeTransactionOrNull(context -> DAOFactory.getUserDAO().findByLogin(user.getLogin(), context));
        if (userInfo != null && userInfo.getPassword().equals(user.getPassword())) {
            userInfo.setPassword(null);
            return userInfo;
        }
        LOGGER.info("Unauthorized login attempt.");
        return null;
    }

    public static void serveSaveUser(StateHolderSaveEntity<User> state) {
        User newUser = null;
        try (DBContext dbContext = new DBContext()) {
            UserDAO userDAO = DAOFactory.getUserDAO();
            User user = state.getEntity();
            state.setValidationInfo(Validator.match(user, new UserValidating(state.getLanguage())));
            if (state.getValidationInfo().containsErrors()) {
                state.setResultState(StateHolderSaveEntity.State.ERROR_WRONG_PARAMETERS);
                return;
            }
            UserType userTypeClient =
                    DAOFactory.getUserTypeDAO().findById(RM_DAO_USER_TYPE.getLong(USER_TYPE_CLIENT_ID), dbContext);
            if (userTypeClient == null) {
                state.setResultState(StateHolderSaveEntity.State.ERROR_ENTITY_NOT_SAVED);
                return;
            }
            if (user.getId() == null) {
                if (userDAO.findByLogin(user.getLogin(), dbContext) != null) {
                    state.getValidationInfo().setError(INPUT_USER_LOGIN, MESSAGE_REGISTER_EXIST);
                    state.setResultState(StateHolderSaveEntity.State.ERROR_WRONG_PARAMETERS);
                    return;
                }
                user.setUserTypeId(userTypeClient.getId());
                newUser = userDAO.create(user, dbContext);
            } else {
                newUser = userDAO.update(user, dbContext);
            }
        } catch (DAOException e) {
            LOGGER.error(e);
        }
        ServiceHelper.setSavedEntity(state, newUser);
    }
}
