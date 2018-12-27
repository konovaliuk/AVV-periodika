package view.tags;

import common.LoggerLoader;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class TagPagination extends TagSupport {
    private static final Logger LOGGER = LoggerLoader.getLogger(TagPagination.class);
    private int pageCount;
    private int activePage;
    private String refPrefix;
    private String refSuffix;

    public void setRefPrefix(String refPrefix) {
        this.refPrefix = refPrefix;
    }

    public void setRefSuffix(String refSuffix) {
        this.refSuffix = refSuffix;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = NumberUtils.toInt(pageCount, 0);
    }

    public void setActivePage(String activePage) {
        this.activePage = NumberUtils.toInt(activePage, 0);
    }

    @Override
    public int doStartTag() throws JspTagException {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= pageCount; i++) {
            sb.append("<li").append(i == activePage ? " class=\"active\"" : "").append(">\n").append("<a href=\"")
              .append(refPrefix).append(i).append(refSuffix).append("\">").append(i).append("</a></li>");
        }
        try {
            pageContext.getOut().write(sb.toString());
        } catch (IOException e) {
            LOGGER.error(e);
            throw new JspTagException(e);
        }
        return SKIP_BODY;
    }
}
