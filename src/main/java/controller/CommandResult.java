package controller;

import org.apache.commons.lang3.StringUtils;

import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ResourceManager.URL_ROOT;

public class CommandResult {
    private String page;
    private boolean redirection;

    private CommandResult(String page, boolean redirection) {
        this.page = StringUtils.defaultIfEmpty(page, RM_VIEW_PAGES.get(URL_ROOT));
        this.redirection = redirection || page == null || page.isEmpty();
    }

    public static CommandResult forward(String page) {
        return new CommandResult(page, false);
    }

    public static CommandResult redirect(String page) {
        return new CommandResult(page, true);
    }

    public String getPage() {
        return page;
    }

    public boolean isRedirection() {
        return redirection;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CommandResult{");
        sb.append("page='").append(page).append('\'');
        sb.append(", redirection=").append(redirection);
        sb.append('}');
        return sb.toString();
    }
}
