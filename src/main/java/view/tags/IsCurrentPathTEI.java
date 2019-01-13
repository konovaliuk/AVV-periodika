package view.tags;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class IsCurrentPathTEI extends TagExtraInfo {
    @Override
    public VariableInfo[] getVariableInfo(TagData data) {
        return new VariableInfo[]{
                new VariableInfo(IsCurrentPathTag.VAR_IS_CURRENT_PATH,
                                 "Boolean",
                                 true,
                                 VariableInfo.AT_END)};
    }
}
