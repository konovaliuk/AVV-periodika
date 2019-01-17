package controller.validation.impl;

import controller.validation.AbstractValidating;
import controller.validation.ValidationBehavior;
import controller.validation.ValidationData;
import model.PeriodicalCategory;

import static common.ResourceManager.PATTERN_TITLE_REGEXP;
import static common.ViewConstants.INPUT_CATEGORY_NAME;

public class CategoryValidating extends AbstractValidating implements ValidationBehavior<PeriodicalCategory> {
    public CategoryValidating(String language) {
        super(language);
    }

    @Override
    public ValidationData match(PeriodicalCategory category) {
        ValidationData validationInfo = new ValidationData();
        if (notMatch(resource.getString(PATTERN_TITLE_REGEXP), category.getName())) {
            validationInfo.setError(INPUT_CATEGORY_NAME);
        }
        return validationInfo;
    }

}
