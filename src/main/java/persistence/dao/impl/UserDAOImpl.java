package persistence.dao.impl;

import common.ResourceManager;
import model.User;
import persistence.dao.DAOException;
import persistence.dao.UserDAO;
import persistence.database.DBContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImpl extends AbstractDAO<User, Long> implements UserDAO {
    private static final ResourceManager RESOURCES = ResourceManager.RM_DAO_USER;
    private static final String SQL_SELECT_BY_LOGIN = "sql.select_by_login";
    private static final int KEY_ID = 1;
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
    private static final String SQL_EDIT_USER_TYPE_ID = "sql.edit.user_type_id";
    private static final String SQL_EDIT_FNAME = "sql.edit.first_name";
    private static final String SQL_EDIT_MNAME = "sql.edit.middle_name";
    private static final String SQL_EDIT_LNAME = "sql.edit.last_name";
    private static final String SQL_EDIT_EMAIL = "sql.edit.email";
    private static final String SQL_EDIT_ADDRESS = "sql.edit.address";
    private static final String SQL_EDIT_PHONE = "sql.edit.phone";
    private static final String SQL_EDIT_LOGIN = "sql.edit.login";
    private static final String SQL_EDIT_PSW = "sql.edit.password";
    private static final String SQL_UPDATE_ID = "sql.update.id";

    public UserDAOImpl() {
        super(RESOURCES);
    }

    @Override
    protected User valueOf(ResultSet rs) throws SQLException {
        return new User(rs.getLong(RESOURCES.getInt(SQL_SELECT_ID)),
                        rs.getLong(RESOURCES.getInt(SQL_SELECT_USER_TYPE_ID)),
                        rs.getString(RESOURCES.getInt(SQL_SELECT_FNAME)),
                        rs.getString(RESOURCES.getInt(SQL_SELECT_MNAME)),
                        rs.getString(RESOURCES.getInt(SQL_SELECT_LNAME)),
                        rs.getString(RESOURCES.getInt(SQL_SELECT_EMAIL)),
                        rs.getString(RESOURCES.getInt(SQL_SELECT_ADDRESS)),
                        rs.getString(RESOURCES.getInt(SQL_SELECT_PHONE)),
                        rs.getString(RESOURCES.getInt(SQL_SELECT_LOGIN)),
                        rs.getString(RESOURCES.getInt(SQL_SELECT_PSW)));
    }

    @Override
    protected Long valueOfGenKey(ResultSet rs) throws SQLException {
        return rs.getLong(KEY_ID);
    }

    @Override
    protected void setInsertStatementValues(PreparedStatement statement, User entity) throws SQLException {
        statement.setLong(RESOURCES.getInt(SQL_EDIT_USER_TYPE_ID), entity.getUserTypeId());
        statement.setString(RESOURCES.getInt(SQL_EDIT_FNAME), entity.getFirstName());
        statement.setString(RESOURCES.getInt(SQL_EDIT_MNAME), entity.getMiddleName());
        statement.setString(RESOURCES.getInt(SQL_EDIT_LNAME), entity.getLastName());
        statement.setString(RESOURCES.getInt(SQL_EDIT_EMAIL), entity.getEmail());
        statement.setString(RESOURCES.getInt(SQL_EDIT_ADDRESS), entity.getAddress());
        statement.setString(RESOURCES.getInt(SQL_EDIT_PHONE), entity.getPhone());
        statement.setString(RESOURCES.getInt(SQL_EDIT_LOGIN), entity.getLogin());
        statement.setString(RESOURCES.getInt(SQL_EDIT_PSW), entity.getPassword());
    }

    @Override
    protected void setUpdateStatementValues(PreparedStatement statement, User entity) throws SQLException {
        statement.setLong(RESOURCES.getInt(SQL_EDIT_USER_TYPE_ID), entity.getUserTypeId());
        statement.setString(RESOURCES.getInt(SQL_EDIT_FNAME), entity.getFirstName());
        statement.setString(RESOURCES.getInt(SQL_EDIT_MNAME), entity.getMiddleName());
        statement.setString(RESOURCES.getInt(SQL_EDIT_LNAME), entity.getLastName());
        statement.setString(RESOURCES.getInt(SQL_EDIT_EMAIL), entity.getEmail());
        statement.setString(RESOURCES.getInt(SQL_EDIT_ADDRESS), entity.getAddress());
        statement.setString(RESOURCES.getInt(SQL_EDIT_PHONE), entity.getPhone());
        statement.setString(RESOURCES.getInt(SQL_EDIT_LOGIN), entity.getLogin());
        statement.setString(RESOURCES.getInt(SQL_EDIT_PSW), entity.getPassword());
        statement.setLong(RESOURCES.getInt(SQL_UPDATE_ID), entity.getId());
    }

    @Override
    public User findByLogin(String login, DBContext context) throws DAOException {
        List<User> list = select(RESOURCES.get(SQL_SELECT_BY_LOGIN), context, login);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }
}
