package view.tags;

import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.jsp.tagext.TagSupport;

public class PaginationTag extends TagSupport {
    public static final String VAR_PAGE_NUM = "pageNum";
    private int pageCount;
    private int counter;

    public void setPageCount(String pageCount) {
        this.pageCount = NumberUtils.toInt(pageCount, 0);
        this.counter = this.pageCount;
    }

    @Override
    public int doStartTag() {
        counter = 1;
        if (counter <= pageCount) {
            pageContext.setAttribute(VAR_PAGE_NUM, counter);
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }

    public int doEndTag() {
        return EVAL_PAGE;
    }

    public int doAfterBody() {
        if (counter < pageCount) {
            pageContext.setAttribute(VAR_PAGE_NUM, ++counter);
            return EVAL_BODY_AGAIN;
        } else {
            return SKIP_BODY;
        }
    }
}

