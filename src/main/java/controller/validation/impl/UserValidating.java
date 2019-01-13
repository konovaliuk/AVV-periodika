package controller.validation.impl;

import controller.validation.AbstractValidating;
import controller.validation.ValidationBehavior;
import controller.validation.ValidationData;
import model.User;

import java.util.regex.Pattern;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class UserValidating extends AbstractValidating implements ValidationBehavior<User> {
    public UserValidating(String language) {
        super(language);
    }

    @Override
    public ValidationData match(User user) {
        ValidationData validationInfo = new ValidationData();
        if (!user.getFirstName().isEmpty() &&
            !matches(resource.getString(PATTERN_NAME_REGEXP), user.getFirstName())) {
            validationInfo.setError(INPUT_USER_FIRST_NAME);
        }
        if (!user.getMiddleName().isEmpty() &&
            !matches(resource.getString(PATTERN_NAME_REGEXP), user.getMiddleName())) {
            validationInfo.setError(INPUT_USER_MIDDLE_NAME);
        }
        if (!user.getLastName().isEmpty() &&
            !matches(resource.getString(PATTERN_NAME_REGEXP), user.getLastName())) {
            validationInfo.setError(INPUT_USER_LAST_NAME);
        }
        if (!user.getEmail().isEmpty() &&
            !matches(resource.getString(PATTERN_EMAIL_REGEXP), user.getEmail())) {
            validationInfo.setError(INPUT_USER_EMAIL);
        }
        if (!user.getPhone().isEmpty() && !Pattern.matches(resource.getString(PATTERN_PHONE_REGEXP), user.getPhone())) {
            validationInfo.setError(INPUT_USER_PHONE);
        }
        if (!user.getAddress().isEmpty() &&
            !matches(resource.getString(PATTERN_ADDRESS_REGEXP), user.getAddress())) {
            validationInfo.setError(INPUT_USER_ADDRESS);
        }
        if (!matches(resource.getString(PATTERN_LOGIN_REGEXP), user.getLogin())) {
            validationInfo.setError(INPUT_USER_LOGIN);
        }
        if (!matches(resource.getString(PATTERN_PASSWORD_REGEXP), user.getPassword())) {
            validationInfo.setError(INPUT_USER_PASSWORD);
        }
        return validationInfo;
    }

}
