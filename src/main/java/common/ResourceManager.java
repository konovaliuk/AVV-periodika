package common;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ResourceBundle;

public enum ResourceManager {
    RM_DATABASE("database"),
    RM_DAO_USER("dao_user"),
    RM_DAO_USER_TYPE("dao_user_type"),
    RM_DAO_USER_INFO("dao_user_info"),
    RM_DAO_PERIODICAL_CATEGORY("dao_periodical_category"),
    RM_DAO_PERIODICAL("dao_periodical"),
    RM_DAO_PERIODICAL_INFO("dao_periodical_info"),
    RM_VIEW_MESSAGES("view_messages"),
    RM_VIEW_PAGES("view_pages");
    public static final String URL_ROOT = "path.url.root";
    public static final String URL_MAIN = "path.url.main";
    public static final String URL_LOGIN = "path.url.login";
    public static final String URL_LOGOUT = "path.url.logout";
    public static final String URL_REGISTER = "path.url.register";
    public static final String URL_PERIODICAL = "path.url.periodical";
    public static final String URL_CATALOG = "path.url.catalog";
    public static final String PAGE_MAIN = "path.page.main";
    public static final String PAGE_LOGIN = "path.page.login";
    public static final String PAGE_REGISTER = "path.page.register";
    public static final String PAGE_CATALOG = "path.page.catalog";
    public static final String PAGE_PERIODICAL = "path.page.periodical";
    public static final String MESSAGE_COMMAND_EXECUTION_ERROR = "message.command_execution_error";
    public static final String MESSAGE_PERIODICAL_NOT_FOUND = "message.periodical.not_found";
    public static final String MESSAGE_PERIODICAL_SAVE_SUCCESS = "message.periodical.save_success";
    public static final String MESSAGE_PERIODICAL_SAVE_ERROR = "message.periodical.save_error";
    public static final String MESSAGE_PERIODICAL_DELETE_SUCCESS = "message.periodical.delete_success";
    public static final String MESSAGE_PERIODICAL_DELETE_ERROR = "message.periodical.delete_error";
    public static final String MESSAGE_CATEGORY_NOT_FOUND = "message.category.not_found";
    public static final String MESSAGE_CATEGORY_SAVE_SUCCESS = "message.category.save_success";
    public static final String MESSAGE_CATEGORY_SAVE_ERROR = "message.category.save_error";
    public static final String MESSAGE_CATEGORY_DELETE_SUCCESS = "message.category.delete_success";
    public static final String MESSAGE_CATEGORY_DELETE_ERROR = "message.category.delete_error";
    public static final String MESSAGE_VALIDATION_ERROR = "message.validation_error";
    public static final String MESSAGE_ALREADY_LOGGED_IN = "message.already_logged_in";
    public static final String MESSAGE_LOGIN_ERROR = "message.user.login_error";
    public static final String MESSAGE_LOGIN_WELCOME = "message.user.login_welcome";
    public static final String MESSAGE_REGISTER_WELCOME = "message.user.login_welcome";
    public static final String MESSAGE_REGISTER_ERROR = "message.user.login_error";
    public static final String MESSAGE_REGISTER_EXIST = "message.register_login_exist";
    public static final String MESSAGE_WRONG_PARAMETERS = "message.wrong_parameters";
    public static final String KEY_NEWSPAPER = "text.category.type_newspaper";
    public static final String KEY_MAGAZINE = "text.category.type_magazine";
    public static final String KEY_NEWSPAPERS_CATEGORIES = "text.catalog.newspapers_categories";
    public static final String KEY_MAGAZINES_CATEGORIES = "text.catalog.magazines_categories";
    public static final String KEY_LANGUAGE_DEFAULT = "language.default";
    public static final String MESSAGE_WRONG_COMMAND = "message.wrong_command";
    public static final String DEFAULT_ITEMS_PER_PAGE = "default.items_per_page";
    public static final String CATEGORY_TYPE_NEWSPAPER = "periodical_category_type.newspaper";
    public static final String CATEGORY_TYPE_MAGAZINE = "periodical_category_type.magazine";
    public static final String PATTERN_TITLE_REGEXP = "pattern.title.regexp";
    public static final String DB_POOL_DATASOURCE_NAME = "db.pool.datasource_name";
    public static final String USER_TYPE_ADMIN_ID = "admin.id";
    public static final String USER_TYPE_CLIENT_ID = "client.id";
    private ResourceBundle resource;

    ResourceManager(String bundleName) {
        resource = ResourceBundle.getBundle(bundleName);
    }

    public String get(String key) {
        return resource.getString(key);
    }

    public int getInt(String key) {
        return NumberUtils.toInt(resource.getString(key), 0);
    }

    public long getLong(String key) {
        return NumberUtils.toLong(resource.getString(key), 0);
    }
}
