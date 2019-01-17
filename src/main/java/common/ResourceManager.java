package common;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ResourceBundle;

public enum ResourceManager {
    RM_DATABASE("database"),
    RM_DAO_USER("dao_user"),
    RM_DAO_USER_TYPE("dao_user_type"),
    RM_DAO_PERIODICAL_CATEGORY("dao_periodical_category"),
    RM_DAO_PERIODICAL("dao_periodical"),
    RM_DAO_SUBSCRIPTION("dao_subscription"),
    RM_DAO_SUBSCRIPTION_INFO("dao_subscription_info"),
    RM_DAO_PAYMENT("dao_payment"),
    RM_VIEW_MESSAGES("view_messages"),
    RM_VIEW_PAGES("view_pages");
    public static final String URL_ROOT = "path.url.root";
    public static final String URL_MAIN = "path.url.main";
    public static final String URL_LOGIN = "path.url.login";
    public static final String URL_LOGOUT = "path.url.logout";
    public static final String URL_REGISTER = "path.url.register";
    public static final String URL_PERIODICAL = "path.url.periodical";
    public static final String URL_SUBSCRIPTION = "path.url.subscription";
    public static final String URL_CATALOG = "path.url.catalog";
    public static final String URL_CABINET = "path.url.cabinet";
    public static final String URL_PAYMENT = "path.url.payment";
    public static final String PAGE_MAIN = "path.page.main";
    public static final String PAGE_LOGIN = "path.page.login";
    public static final String PAGE_REGISTER = "path.page.register";
    public static final String PAGE_CATALOG = "path.page.catalog";
    public static final String PAGE_PERIODICAL = "path.page.periodical";
    public static final String PAGE_SUBSCRIPTION = "path.page.subscription";
    public static final String PAGE_CABINET = "path.page.cabinet";
    public static final String PAGE_PAYMENT = "path.page.payment";
    public static final String MESSAGE_WRONG_COMMAND = "message.wrong_command";
    public static final String MESSAGE_WRONG_PARAMETERS = "message.wrong_parameters";
    public static final String MESSAGE_VALIDATION_ERROR = "message.validation_error";
    public static final String MESSAGE_COMMAND_EXECUTION_ERROR = "message.command_execution_error";
    public static final String MESSAGE_PERIODICAL_SAVE_SUCCESS = "message.periodical.save_success";
    public static final String MESSAGE_PERIODICAL_SAVE_ERROR = "message.periodical.save_error";
    public static final String MESSAGE_PERIODICAL_DELETE_SUCCESS = "message.periodical.delete_success";
    public static final String MESSAGE_PERIODICAL_DELETE_ERROR = "message.periodical.delete_error";
    public static final String MESSAGE_CATEGORY_SAVE_SUCCESS = "message.category.save_success";
    public static final String MESSAGE_CATEGORY_SAVE_ERROR = "message.category.save_error";
    public static final String MESSAGE_CATEGORY_DELETE_SUCCESS = "message.category.delete_success";
    public static final String MESSAGE_CATEGORY_DELETE_ERROR = "message.category.delete_error";
    public static final String MESSAGE_LOGIN_ERROR = "message.user.login_error";
    public static final String MESSAGE_LOGIN_WELCOME = "message.user.login_welcome";
    public static final String MESSAGE_REGISTER_WELCOME = "message.user.login_welcome";
    public static final String MESSAGE_REGISTER_ERROR = "message.user.login_error";
    public static final String MESSAGE_REGISTER_EXIST = "message.register_login_exist";
    public static final String MESSAGE_USER_SAVE_ERROR = "message.user.save_error";
    public static final String MESSAGE_USER_SAVE_SUCCESS = "message.user.save_success";
    public static final String MESSAGE_SUBSCRIPTION_SAVE_SUCCESS = "message.subscription.save_success";
    public static final String MESSAGE_SUBSCRIPTION_SAVE_ERROR = "message.subscription.save_error";
    public static final String MESSAGE_PAYMENT_SAVE_SUCCESS = "message.payment.save_success";
    public static final String MESSAGE_PAYMENT_SAVE_ERROR = "message.payment.save_error";
    public static final String MESSAGE_SUBSCRIPTION_DELETE_SUCCESS = "message.subscription.delete_success";
    public static final String MESSAGE_SUBSCRIPTION_DELETE_ERROR = "message.subscription.delete_error";
    public static final String MESSAGE_SUBSCRIPTION_WRONG_PERIOD_START = "message.subscription.period_start_next_month";
    public static final String MESSAGE_SUBSCRIPTION_WRONG = "message.subscription.wrong";
    public static final String KEY_LANGUAGE_DEFAULT = "language.default";
    public static final String DEFAULT_ITEMS_PER_PAGE = "default.items_per_page";
    public static final String PATTERN_TITLE_REGEXP = "pattern.title.regexp";
    public static final String PATTERN_NAME_REGEXP = "pattern.name.regexp";
    public static final String PATTERN_ADDRESS_REGEXP = "pattern.address.regexp";
    public static final String PATTERN_EMAIL_REGEXP = "pattern.email.regexp";
    public static final String PATTERN_PHONE_REGEXP = "pattern.phone.regexp";
    public static final String PATTERN_LOGIN_REGEXP = "pattern.login.regexp";
    public static final String PATTERN_PASSWORD_REGEXP = "pattern.password.regexp";
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
