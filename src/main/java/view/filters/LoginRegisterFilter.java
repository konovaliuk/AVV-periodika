package view.filters;

import common.LoggerLoader;
import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginRegisterFilter implements Filter {
    private static final Logger LOGGER = LoggerLoader.getLogger(LoginRegisterFilter.class);
    private static final String ATTR_NAME_USER = "user_info";
    private static final String URL_ROOT = "path.url.root";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (((HttpServletRequest) request).getSession().getAttribute(ATTR_NAME_USER) != null) {
            ((HttpServletResponse) response).sendRedirect(URL_ROOT);
        } else {
            chain.doFilter(request, response);
        }
    }
}
