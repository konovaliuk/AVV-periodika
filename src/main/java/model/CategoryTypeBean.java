package model;

import java.util.Locale;
import java.util.ResourceBundle;

import static common.ResourceManager.*;
import static common.ViewConstants.VIEW_PAGE_ELEMENTS_RESOURCE;

public class CategoryTypeBean {
    private Integer type;
    private ResourceBundle resource;

    public CategoryTypeBean() {
    }

    public void setLanguage(String language) {
        Locale locale = Locale.forLanguageTag(language);
        resource = ResourceBundle.getBundle(VIEW_PAGE_ELEMENTS_RESOURCE, locale);
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return getNameByType(type);
    }

    public String getPluralName() {
        return getPluralNameByType(type);
    }

    public Integer getNewspaperType() {
        return RM_DAO_PERIODICAL_CATEGORY.getInt(CATEGORY_TYPE_NEWSPAPER);
    }

    public Integer getMagazineType() {
        return RM_DAO_PERIODICAL_CATEGORY.getInt(CATEGORY_TYPE_MAGAZINE);
    }

    public String getNameByType(Integer type) {
        if (resource != null && type != null) {
            if (type.equals(getNewspaperType())) {
                return resource.getString(KEY_NEWSPAPER);
            }
            if (type.equals(getMagazineType())) {
                return resource.getString(KEY_MAGAZINE);
            }
        }
        return "";
    }

    public String getPluralNameByType(Integer type) {
        if (resource != null && type != null) {
            if (type.equals(getNewspaperType())) {
                return resource.getString(KEY_NEWSPAPERS_CATEGORIES);
            }
            if (type.equals(getMagazineType())) {
                return resource.getString(KEY_MAGAZINES_CATEGORIES);
            }
        }
        return "";
    }
}
