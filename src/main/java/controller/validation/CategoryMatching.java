package controller.validation;

import model.PeriodicalCategory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static common.ResourceManager.PATTERN_TITLE_REGEXP;
import static common.ViewConstants.INPUT_PERIODICAL_TITLE;

public class CategoryMatching extends AbstractMatching implements MatchingBehavior<PeriodicalCategory> {

    public CategoryMatching(String language) {
        super(language);
    }

    @Override
    public Map<String, Boolean> match(PeriodicalCategory category) {
        Map<String, Boolean> validationInfo = new HashMap<>();
        validationInfo.put(INPUT_PERIODICAL_TITLE,
                           Pattern.matches(resource.getString(PATTERN_TITLE_REGEXP), category.getName()));
        return validationInfo;
    }

}
