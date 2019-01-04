package persistence.dao;

import persistence.dao.impl.PeriodicalCategoryDAOImpl;
import persistence.dao.impl.PeriodicalDAOImpl;
import persistence.dao.impl.PeriodicalInfoDAOImpl;
import persistence.dao.impl.UserDAOImpl;
import persistence.dao.impl.UserInfoDAOImpl;
import persistence.dao.impl.UserTypeDAOImpl;

public class DAOFactory {
    public static UserDAO getUserDAO() {
        return new UserDAOImpl();
    }

    public static UserTypeDAO getUserTypeDAO() {
        return new UserTypeDAOImpl();
    }

    public static UserInfoDAO getUserInfoDAO() {
        return new UserInfoDAOImpl();
    }

    public static PeriodicalCategoryDAO getPeriodicalCategoryDAO() {
        return new PeriodicalCategoryDAOImpl();
    }

    public static PeriodicalDAO getPeriodicalDAO() {
        return new PeriodicalDAOImpl();
    }

    public static PeriodicalInfoDAO getPeriodicalInfoDAO() {
        return new PeriodicalInfoDAOImpl();
    }
}
