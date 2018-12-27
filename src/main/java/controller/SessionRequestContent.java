package controller;

import common.LoggerLoader;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionRequestContent {

    private static final Logger LOGGER = LoggerLoader.getLogger(SessionRequestContent.class);
    private static final String MESSAGE_STYLE_WARNING = "alert-warning";
    private static final String MESSAGE_STYLE_SUCCESS = "alert-success";
    private static final String MESSAGE_STYLE_DANGER = "alert-danger";
    private static final String ATTR_NAME_MESSAGE = "message_to_display";
    private static final String ATTR_NAME_MESSAGE_STYLE = "message_style";
    private HttpServletRequest request;
    private HttpServletResponse response;

    public SessionRequestContent(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public boolean existSessionAttribute(String name) {
        return getSessionAttribute(name) != null;
    }

    public void setMessageSuccess(String message) {
        setSessionAttribute(ATTR_NAME_MESSAGE, message);
        setSessionAttribute(ATTR_NAME_MESSAGE_STYLE, MESSAGE_STYLE_SUCCESS);
    }

    public void setMessageWarning(String message) {
        setSessionAttribute(ATTR_NAME_MESSAGE, message);
        setSessionAttribute(ATTR_NAME_MESSAGE_STYLE, MESSAGE_STYLE_WARNING);
    }

    public void setMessageDanger(String message) {
        setSessionAttribute(ATTR_NAME_MESSAGE, message);
        setSessionAttribute(ATTR_NAME_MESSAGE_STYLE, MESSAGE_STYLE_DANGER);
    }

    public String getServletPath() {
        return request.getServletPath();
    }

    public String getRequestParameter(String name) {
        return request.getParameter(name);
    }

    public String[] getRequestParameterValues(String name) {
        return request.getParameterValues(name);
    }

    public Object getRequestAttribute(String name) {
        return request.getAttribute(name);
    }

    public void setRequestAttribute(String name, Object value) {
        request.setAttribute(name, value);
    }

    public Object getSessionAttribute(String name) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getAttribute(name);
        }
        return null;
    }

    public void setSessionAttribute(String name, Object value, boolean create) {
        HttpSession session = request.getSession(create);
        if (session != null) {
            session.setAttribute(name, value);
        }
    }

    public void setSessionAttribute(String name, Object value) {
        setSessionAttribute(name, value, false);
    }

    public void removeSessionAttribute(String name) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(name);
        }
    }

    public void invalidateSession() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

}