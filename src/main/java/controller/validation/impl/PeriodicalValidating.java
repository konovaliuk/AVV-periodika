package controller.validation.impl;

import controller.validation.AbstractValidating;
import controller.validation.ValidationBehavior;
import controller.validation.ValidationData;
import model.Entity;
import model.Periodical;

import java.math.BigDecimal;

import static common.ResourceManager.PATTERN_TITLE_REGEXP;
import static common.ViewConstants.*;

public class PeriodicalValidating extends AbstractValidating implements ValidationBehavior<Periodical> {
    public PeriodicalValidating(String language) {
        super(language);
    }

    @Override
    public ValidationData match(Periodical periodical) {
        ValidationData validationInfo = new ValidationData();
        if (notMatch(resource.getString(PATTERN_TITLE_REGEXP), periodical.getTitle())) {
            validationInfo.setError(INPUT_PERIODICAL_TITLE);
        }
        if (periodical.getCategoryId() == null || periodical.getCategoryId() == Entity.NULL_ID) {
            validationInfo.setError(INPUT_PERIODICAL_CATEGORY_ID);
        }
        if (periodical.getMinSubscriptionPeriod() == null || periodical.getMinSubscriptionPeriod() <= 0) {
            validationInfo.setError(INPUT_PERIODICAL_MIN_PERIOD);
        }
        if (periodical.getIssuesPerPeriod() == null || periodical.getIssuesPerPeriod() <= 0) {
            validationInfo.setError(INPUT_PERIODICAL_ISSUES);
        }
        if (periodical.getPricePerPeriod() == null || periodical.getPricePerPeriod().compareTo(BigDecimal.ZERO) <= 0) {
            validationInfo.setError(INPUT_PERIODICAL_PRICE);
        }
        return validationInfo;
    }

}
