package controller.validation.impl;

import controller.validation.AbstractValidating;
import controller.validation.ValidationBehavior;
import controller.validation.ValidationData;
import model.PeriodicalCategory;

import java.util.regex.Pattern;

import static common.ResourceManager.PATTERN_TITLE_REGEXP;
import static common.ViewConstants.INPUT_CATEGORY_NAME;

public class CategoryValidating extends AbstractValidating implements ValidationBehavior<PeriodicalCategory> {

    public CategoryValidating(String language) {
        super(language);
    }

    @Override
    public ValidationData match(PeriodicalCategory category) {
        ValidationData validationInfo = new ValidationData();
        validationInfo.setError(INPUT_CATEGORY_NAME,
                                String.valueOf(Pattern.matches(resource.getString(PATTERN_TITLE_REGEXP),
                                                               category.getName())));
        return validationInfo;
    }

}
