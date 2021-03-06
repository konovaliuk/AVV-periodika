package persistence.dao.implsql;

import common.ResourceManager;
import model.UserType;
import persistence.dao.UserTypeDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserTypeDAOImpl extends AbstractReadonlyDAO<UserType, Long> implements UserTypeDAO {
    private static final ResourceManager RESOURCES = ResourceManager.RM_DAO_USER_TYPE;
    private static final int KEY_ID = 1;
    private static final String SQL_SELECT_ID = "sql.select_all.id";
    private static final String SQL_SELECT_NAME = "sql.select_all.name";

    UserTypeDAOImpl() {
        super(RESOURCES);
    }

    @Override
    protected UserType valueOf(ResultSet rs) throws SQLException {
        return new UserType(rs.getLong(RESOURCES.getInt(SQL_SELECT_ID)),
                            rs.getString(RESOURCES.getInt(SQL_SELECT_NAME)));
    }

    @Override
    protected Long valueOfGenKey(ResultSet rs) throws SQLException {
        return rs.getLong(KEY_ID);
    }
}
