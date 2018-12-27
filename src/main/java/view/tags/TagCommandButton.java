package view.tags;

import common.LoggerLoader;
import org.apache.log4j.Logger;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class TagCommandButton extends TagSupport {
    private static final Logger LOGGER = LoggerLoader.getLogger(TagCommandButton.class);
    private static final String ATTR_NAME_CURRENT_URI = "javax.servlet.forward.request_uri";
    private String command;
    private String path;

    public void setCommand(String command) {
        this.command = command;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int doStartTag() throws JspTagException {
        String currentPath = (String) pageContext.getRequest().getAttribute(ATTR_NAME_CURRENT_URI);
        boolean hasPath = path != null;
        boolean hasCommand = command != null && !command.isEmpty();
        boolean hasRef = hasPath || hasCommand;
        try {
            JspWriter out = pageContext.getOut();
            out.write("<li");
            if (hasPath && currentPath != null && currentPath.equalsIgnoreCase(path)) {
                out.write(" class=\"active\"");
            }
            out.write(">");
            out.write("<a");
            if (hasRef) {
                out.write(" href=\"");
                if (hasPath) {
                    out.write(path);
                }
                if (hasCommand) {
                    out.write("?command=");
                    out.write(command);
                }
                out.write("\"");
            }
            out.write(">");
        } catch (IOException e) {
            LOGGER.error(e);
            throw new JspTagException(e);
        }
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspTagException {
        try {
            pageContext.getOut().write("</a></li>");
        } catch (IOException e) {
            LOGGER.error(e);
            throw new JspTagException(e.getMessage());
        }
        return EVAL_PAGE;
    }
}
