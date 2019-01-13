package model;

import java.util.Locale;
import java.util.ResourceBundle;

import static common.ViewConstants.VIEW_PAGE_ELEMENTS_RESOURCE;

public class CategoryTypeBean {
    private static final String KEY_NEWSPAPER = "text.category.type_newspaper";
    private static final String KEY_MAGAZINE = "text.category.type_magazine";
    private static final String KEY_NEWSPAPERS_CATEGORIES = "text.catalog.newspapers_categories";
    private static final String KEY_MAGAZINES_CATEGORIES = "text.catalog.magazines_categories";
    private CategoryType type;
    private ResourceBundle resource;

    public CategoryTypeBean() {
    }

    public void setLanguage(String language) {
        Locale locale = Locale.forLanguageTag(language);
        resource = ResourceBundle.getBundle(VIEW_PAGE_ELEMENTS_RESOURCE, locale);
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public CategoryType getNewspaperType() {
        return CategoryType.NEWSPAPER;
    }

    public CategoryType getMagazineType() {
        return CategoryType.MAGAZINE;
    }

    public String getName() {
        return getNameByTypeAndKind(type, false);
    }

    public String getPluralName() {
        return getNameByTypeAndKind(type, true);
    }

    public String getNameByType(CategoryType type) {
        return getNameByTypeAndKind(type, false);
    }

    public String getNameByTypeAndKind(CategoryType type, boolean plural) {
        if (resource == null || type == null) {
            return "";
        }
        switch (type) {
            case NEWSPAPER:
                return resource.getString(plural ? KEY_NEWSPAPERS_CATEGORIES : KEY_NEWSPAPER);
            case MAGAZINE:
                return resource.getString(plural ? KEY_MAGAZINES_CATEGORIES : KEY_MAGAZINE);
            default:
                return "";
        }
    }
}
