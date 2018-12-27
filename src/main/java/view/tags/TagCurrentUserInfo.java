package view.tags;

import common.LoggerLoader;
import model.UserInfo;
import org.apache.log4j.Logger;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class TagCurrentUserInfo extends TagSupport {
    private static final Logger LOGGER = LoggerLoader.getLogger(TagCurrentUserInfo.class);
    private static final String PARAM_NAME_USER = "user_info";

    @Override
    public int doStartTag() throws JspTagException {
        UserInfo userInfo = (UserInfo) pageContext.getSession().getAttribute(PARAM_NAME_USER);
        if (userInfo != null) {
            JspWriter out = pageContext.getOut();
            try {
                out.write("Hello, ");
                out.write(userInfo.getFirstName());
                out.write("!");
            } catch (IOException e) {
                LOGGER.error(e);
                throw new JspTagException(e);
            }
        }

        return SKIP_BODY;
    }
}
