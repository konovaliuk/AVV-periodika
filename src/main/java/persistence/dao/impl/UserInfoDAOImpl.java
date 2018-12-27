package persistence.dao.impl;

import common.ResourceManager;
import model.UserInfo;
import persistence.dao.DAOException;
import persistence.dao.UserInfoDAO;
import persistence.database.DBContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserInfoDAOImpl extends AbstractReadonlyDAO<UserInfo, Long> implements UserInfoDAO {
    private static final ResourceManager RESOURCES = ResourceManager.RM_DAO_USER_INFO;
    private static final int KEY_ID = 1;
    private static final String SQL_SELECT_BY_LOGIN = "sql.select_by_login";
    private static final String SQL_SELECT_ID = "sql.select_all.id";
    private static final String SQL_SELECT_USER_TYPE_ID = "sql.select_all.user_type_id";
    private static final String SQL_SELECT_FNAME = "sql.select_all.first_name";
    private static final String SQL_SELECT_MNAME = "sql.select_all.middle_name";
    private static final String SQL_SELECT_LNAME = "sql.select_all.last_name";
    private static final String SQL_SELECT_EMAIL = "sql.select_all.email";
    private static final String SQL_SELECT_ADDRESS = "sql.select_all.address";
    private static final String SQL_SELECT_PHONE = "sql.select_all.phone";
    private static final String SQL_SELECT_LOGIN = "sql.select_all.login";
    private static final String SQL_SELECT_PSW = "sql.select_all.password";
    private static final String SQL_SELECT_USER_TYPE_NAME = "sql.select_all.user_type_name";

    public UserInfoDAOImpl() {
        super(RESOURCES);
    }

    @Override
    protected UserInfo valueOf(ResultSet rs) throws SQLException {
        return new UserInfo(rs.getLong(RESOURCES.getInt(SQL_SELECT_ID)),
                            rs.getLong(RESOURCES.getInt(SQL_SELECT_USER_TYPE_ID)),
                            rs.getString(RESOURCES.getInt(SQL_SELECT_FNAME)),
                            rs.getString(RESOURCES.getInt(SQL_SELECT_MNAME)),
                            rs.getString(RESOURCES.getInt(SQL_SELECT_LNAME)),
                            rs.getString(RESOURCES.getInt(SQL_SELECT_EMAIL)),
                            rs.getString(RESOURCES.getInt(SQL_SELECT_ADDRESS)),
                            rs.getString(RESOURCES.getInt(SQL_SELECT_PHONE)),
                            rs.getString(RESOURCES.getInt(SQL_SELECT_LOGIN)),
                            rs.getString(RESOURCES.getInt(SQL_SELECT_PSW)),
                            rs.getString(RESOURCES.getInt(SQL_SELECT_USER_TYPE_NAME)));
    }

    @Override
    protected Long valueOfGenKey(ResultSet rs) throws SQLException {
        return rs.getLong(KEY_ID);
    }

    @Override
    public UserInfo findByLogin(String login, DBContext context) throws DAOException {
        List<UserInfo> list = select(RESOURCES.get(SQL_SELECT_BY_LOGIN), context, login);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }
}
