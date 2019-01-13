package view.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ResourceBundle;

import static common.ResourceManager.KEY_LANGUAGE_DEFAULT;
import static common.ViewConstants.ATTR_NAME_LANGUAGE;
import static common.ViewConstants.VIEW_PAGE_ELEMENTS_RESOURCE;

public class LanguageFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (((HttpServletRequest) request).getSession().getAttribute(ATTR_NAME_LANGUAGE) == null) {
            String defaultLanguage =
                    ResourceBundle.getBundle(VIEW_PAGE_ELEMENTS_RESOURCE).getString(KEY_LANGUAGE_DEFAULT);
            ((HttpServletRequest) request).getSession().setAttribute(ATTR_NAME_LANGUAGE, defaultLanguage);
        }
        chain.doFilter(request, response);
    }
}
