package controller.validation;


import java.util.Locale;
import java.util.ResourceBundle;

import static common.ResourceManager.KEY_LANGUAGE_DEFAULT;
import static common.ViewConstants.VIEW_PAGE_ELEMENTS_RESOURCE;

public abstract class AbstractMatching {
    protected ResourceBundle resource;

    public AbstractMatching(String language) {
        if (language == null || language.isEmpty()) {
            language = ResourceBundle.getBundle(VIEW_PAGE_ELEMENTS_RESOURCE).getString(KEY_LANGUAGE_DEFAULT);
        }
        Locale locale = Locale.forLanguageTag(language);
        resource = ResourceBundle.getBundle(VIEW_PAGE_ELEMENTS_RESOURCE, locale);
    }
}
