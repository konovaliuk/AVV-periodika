package controller.validation;

import model.User;

import java.util.HashMap;
import java.util.Map;

public class LoginMatching extends AbstractMatching implements MatchingBehavior<User> {
    public LoginMatching(String language) {
        super(language);
    }

    @Override
    public Map<String, Boolean> match(User user) {
        Map<String, Boolean> validationInfo = new HashMap<>();
        return validationInfo;
    }

}
