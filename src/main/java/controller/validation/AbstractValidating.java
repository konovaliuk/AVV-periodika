package controller.validation;


import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static common.ResourceManager.KEY_LANGUAGE_DEFAULT;
import static common.ViewConstants.VIEW_PAGE_ELEMENTS_RESOURCE;

public abstract class AbstractValidating {
    protected ResourceBundle resource;

    public AbstractValidating(String language) {
        if (language == null || language.isEmpty()) {
            language = ResourceBundle.getBundle(VIEW_PAGE_ELEMENTS_RESOURCE).getString(KEY_LANGUAGE_DEFAULT);
        }
        Locale locale = Locale.forLanguageTag(language);
        resource = ResourceBundle.getBundle(VIEW_PAGE_ELEMENTS_RESOURCE, locale);
    }

    protected boolean matches(String reg, String value) {
        Matcher matcher = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(value);
        return matcher.matches();
    }
}
