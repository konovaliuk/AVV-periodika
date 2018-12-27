package model;

import java.util.StringJoiner;

import static common.ResourceManager.RM_DAO_USER_TYPE;
import static common.ResourceManager.USER_TYPE_ADMIN_ID;

public class UserInfo extends User implements Entity<Long> {
    private String user_type_name;
    private Boolean admin;

    public UserInfo(Long id,
                    Long userTypeId,
                    String firstName,
                    String middleName,
                    String lastName,
                    String email,
                    String address,
                    String phone,
                    String login,
                    String password,
                    String user_type_name) {
        super(id, userTypeId, firstName, middleName, lastName, email, address, phone, login, password);
        this.user_type_name = user_type_name;
        this.admin = userTypeId.equals(RM_DAO_USER_TYPE.getLong(USER_TYPE_ADMIN_ID));
    }

    public String getUser_type_name() {
        return user_type_name;
    }

    public Boolean getAdmin() {
        return this.admin;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserInfo.class.getSimpleName() + "[", "]")
                .add("user_type_name='" + user_type_name + "'").toString();
    }
}
