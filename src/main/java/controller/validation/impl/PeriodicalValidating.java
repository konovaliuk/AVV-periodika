package controller.validation.impl;

import common.ResourceManager;
import controller.validation.AbstractValidating;
import controller.validation.ValidationBehavior;
import controller.validation.ValidationData;
import model.Periodical;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import static common.ResourceManager.PATTERN_TITLE_REGEXP;
import static common.ViewConstants.INPUT_PERIODICAL_CATEGORY_ID;
import static common.ViewConstants.INPUT_PERIODICAL_ISSUES;
import static common.ViewConstants.INPUT_PERIODICAL_PRICE;
import static common.ViewConstants.INPUT_PERIODICAL_TITLE;

public class PeriodicalValidating extends AbstractValidating implements ValidationBehavior<Periodical> {

    public PeriodicalValidating(String language) {
        super(language);
    }

    @Override
    public ValidationData match(Periodical periodical) {
        ValidationData validationInfo = new ValidationData();
        if (!Pattern.matches(resource.getString(PATTERN_TITLE_REGEXP), periodical.getTitle())) {
            validationInfo.setError(INPUT_PERIODICAL_TITLE);
        }
        if (periodical.getCategoryId() == null || periodical.getCategoryId() == ResourceManager.NULL_ID) {
            validationInfo.setError(INPUT_PERIODICAL_CATEGORY_ID);
        }
        if (periodical.getMinSubscriptionPeriod() == null || periodical.getMinSubscriptionPeriod() == 0) {
            validationInfo.setError(INPUT_PERIODICAL_CATEGORY_ID);
        }
        if (periodical.getIssuesPerPeriod() == null || periodical.getIssuesPerPeriod() == 0) {
            validationInfo.setError(INPUT_PERIODICAL_ISSUES);
        }
        if (periodical.getPricePerPeriod() == null || periodical.getPricePerPeriod().equals(BigDecimal.ZERO)) {
            validationInfo.setError(INPUT_PERIODICAL_PRICE);
        }
        return validationInfo;
    }

}
