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
import persistence.database.DbContext;
import services.states.GenericStateSave;

import static common.ResourceManager.MESSAGE_REGISTER_EXIST;
import static common.ResourceManager.RM_DAO_USER_TYPE;
import static common.ResourceManager.USER_TYPE_CLIENT_ID;
import static common.ViewConstants.INPUT_USER_LOGIN;

public class UserService {
    private static final Logger LOGGER = LoggerLoader.getLogger(UserService.class);

    UserService() {
    }

    public User serveLogin(User user) {
        User userInfo = DbContext
                .executeTransactionOrNull(context -> DAOFactory.getFactory()
                        .getUserDAO()
                        .findByLogin(user.getLogin(), context));
        if (userInfo != null && userInfo.getPassword().equals(user.getPassword())) {
            LOGGER.info("User logged in " + userInfo.getLogin());
            userInfo.setPassword(null);
            return userInfo;
        }
        LOGGER.error("Unauthorized login attempt.");
        return null;
    }

    public void serveSaveUser(GenericStateSave<User> state) {
        User newUser = null;
        User user = state.getEntity();
        state.setValidationInfo(Validator.match(user, new UserValidating(state.getLanguage())));
        if (state.getValidationInfo().containsErrors()) {
            state.setResultState(GenericStateSave.State.ERROR_WRONG_PARAMETERS);
            return;
        }
        try (DbContext dbContext = new DbContext()) {
            UserDAO userDAO = DAOFactory.getFactory().getUserDAO();
            UserType userTypeClient =
                    DAOFactory.getFactory()
                            .getUserTypeDAO()
                            .findById(RM_DAO_USER_TYPE.getLong(USER_TYPE_CLIENT_ID), dbContext);
            if (userTypeClient == null) {
                state.setResultState(GenericStateSave.State.ERROR_ENTITY_NOT_SAVED);
                return;
            }
            if (user.notSaved()) {
                if (userDAO.findByLogin(user.getLogin(), dbContext) != null) {
                    state.getValidationInfo().setError(INPUT_USER_LOGIN, MESSAGE_REGISTER_EXIST);
                    state.setResultState(GenericStateSave.State.ERROR_WRONG_PARAMETERS);
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
