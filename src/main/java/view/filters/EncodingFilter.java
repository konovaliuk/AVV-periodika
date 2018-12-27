package view.filters;

import common.LoggerLoader;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class EncodingFilter implements Filter {
    private static final Logger LOGGER = LoggerLoader.getLogger(EncodingFilter.class);
    private static final String PARAM_NAME_ENCODING = "encoding";
    private static final String DEFAULT_ENCODING = "UTF-8";
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = StringUtils.defaultString(filterConfig.getInitParameter(PARAM_NAME_ENCODING), DEFAULT_ENCODING);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            if (!encoding.equalsIgnoreCase(request.getCharacterEncoding())) {
                request.setCharacterEncoding(encoding);
            }
            if (!encoding.equalsIgnoreCase(response.getCharacterEncoding())) {
                response.setCharacterEncoding(encoding);
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e);
        }

        chain.doFilter(request, response);
    }
}
