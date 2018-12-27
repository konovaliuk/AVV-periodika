package controller.validation;

import model.Periodical;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static common.ResourceManager.PATTERN_TITLE_REGEXP;
import static common.ViewConstants.INPUT_PERIODICAL_TITLE;

public class PeriodicalMatching extends AbstractMatching implements MatchingBehavior<Periodical> {

    public PeriodicalMatching(String language) {
        super(language);
    }

    @Override
    public Map<String, Boolean> match(Periodical periodical) {
        Map<String, Boolean> validationInfo = new HashMap<>();
        validationInfo.put(INPUT_PERIODICAL_TITLE,
                           Pattern.matches(resource.getString(PATTERN_TITLE_REGEXP), periodical.getTitle()));
        //        validationInfo.put(INPUT_USER_MIDDLE_NAME,
        //                Pattern.matches(MatchingPatterns.NAME, user.getMiddleName()));
        //        validationInfo.put(INPUT_USER_LAST_NAME,
        //                Pattern.matches(MatchingPatterns.NAME, user.getLastName()));
        //        validationInfo.put(INPUT_USER_EMAIL,
        //                Pattern.matches(MatchingPatterns.EMAIL, user.getEmail()));
        //        validationInfo.put(INPUT_USER_ADDRESS,
        //                Pattern.matches(MatchingPatterns.ADDRESS, user.getAddress()));
        //        validationInfo.put(INPUT_USER_PHONE,
        //                Pattern.matches(MatchingPatterns.PHONE, user.getPhone()));
        //        validationInfo.put(INPUT_USER_LOGIN,
        //                Pattern.matches(MatchingPatterns.LOGIN, user.getLogin()));
        //        validationInfo.put(INPUT_USER_PASSWORD,
        //                Pattern.matches(MatchingPatterns.PASSWORD, user.getPassword()));
        return validationInfo;
    }

}
