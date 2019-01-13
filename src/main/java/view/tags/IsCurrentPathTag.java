package view.tags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class IsCurrentPathTag extends TagSupport {
    public static final String VAR_IS_CURRENT_PATH = "isCurrentPath";
    private static final String ATTR_NAME_CURRENT_URI = "javax.servlet.forward.request_uri";
    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int doStartTag() throws JspTagException {
        String currentPath = (String) pageContext.getRequest().getAttribute(ATTR_NAME_CURRENT_URI);
        pageContext.setAttribute(VAR_IS_CURRENT_PATH, (path != null && path.equalsIgnoreCase(currentPath)));
        return SKIP_BODY;
    }
}
