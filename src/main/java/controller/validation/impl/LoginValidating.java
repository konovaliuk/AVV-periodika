package controller.validation.impl;

import controller.validation.AbstractValidating;
import controller.validation.ValidationBehavior;
import controller.validation.ValidationData;
import model.User;

import java.util.regex.Pattern;

import static common.ResourceManager.PATTERN_LOGIN_REGEXP;
import static common.ViewConstants.INPUT_USER_LOGIN;

public class LoginValidating extends AbstractValidating implements ValidationBehavior<User> {
    public LoginValidating(String language) {
        super(language);
    }

    @Override
    public ValidationData match(User user) {
        ValidationData validationInfo = new ValidationData();
        if (!Pattern.matches(resource.getString(PATTERN_LOGIN_REGEXP), user.getLogin())) {
            validationInfo.setError(INPUT_USER_LOGIN);
        }
        //        if (!Pattern.matches(resource.getString(PATTERN_PASSWORD_REGEXP), user.getPassword())) {
        //            validationInfo.setError(INPUT_USER_PASSWORD);
        //        }
        return validationInfo;
    }

}
