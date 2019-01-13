package view.tags;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class PaginationTEI extends TagExtraInfo {
    @Override
    public VariableInfo[] getVariableInfo(TagData data) {
        return new VariableInfo[]{
                new VariableInfo(PaginationTag.VAR_PAGE_NUM,
                                 "Integer",
                                 true,
                                 VariableInfo.NESTED)};
    }
}
